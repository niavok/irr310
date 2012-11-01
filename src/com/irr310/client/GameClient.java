package com.irr310.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.input.InputEngine;
import com.irr310.client.navigation.LoginManager;
import com.irr310.client.network.ClientNetworkEngine;
import com.irr310.common.Game;
import com.irr310.common.engine.Engine;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.LoadingGameEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldReadyEvent;
import com.irr310.common.network.protocol.CapacityUpdateMessage;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Map;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.item.BuildingItemFactory;
import com.irr310.common.world.item.NexusItem;
import com.irr310.common.world.system.System;
import com.irr310.server.Duration;
import com.irr310.server.GameServer;
import com.irr310.server.ParameterAnalyser;
import com.irr310.server.ServerGameEngine;
import com.irr310.server.ai.AIEngine;

public class GameClient extends Game {

    List<Engine> engineList = new CopyOnWriteArrayList<Engine>();

    List<Engine> worldEngineList = new ArrayList<Engine>();

    // private ClientGameEngine clientGameEngine;
    // private InputEngine inputEngine;
    private ClientNetworkEngine clientNetworkEngine;
    private PhysicEngine physicEngine;
    // private ClientScriptEngine scriptEngine;
    private UiEngine uiEngine;
    // private ParameterAnalyser parameterAnalyser;
    // private boolean stillRunning;
    // private CommandManager commandManager;

    private World world;

    public static GameClient instance = null;

    public static GameClient getInstance() {
        return instance;
    }

    public GameClient(ParameterAnalyser parameterAnalyser) {
        // this.parameterAnalyser = parameterAnalyser;
        instance = this;
        super.setInstance(this);
        // stillRunning = true;


        // clientNetworkEngine = new ClientNetworkEngine("127.0.0.10", 22310);
        // physicEngine = new PhysicEngine();
        uiEngine = new UiEngine();

        engineList.add(new InputEngine());
        /*if(ClientConfig.sound_isEnabled()) {
            engineList.add(new SoundEngine());
        }*/
        
        
        // engineList.add(new ClientGameEngine());
        // engineList.add(physicEngine);
        // engineList.add(clientNetworkEngine);
        engineList.add(uiEngine);
        
        
        // engineList.add(new ClientScriptEngine());

        //inputEngine = new InputEngine();
        // clientGameEngine = new ClientGameEngine();
        // scriptEngine = new ClientScriptEngine();

        // commandManager = new CommandManager();

    }

    @Override
    public World getWorld() {
        return world;
    }

    public void run() {

        Log.perfBegin("Start");
        Log.perfBegin("Start Engine");

        startAndWaitAllEngines();

        Log.perfEnd(); // Start Engine
        Log.perfBegin("Finish Start");
        sendToAll(new StartEngineEvent());

        java.lang.System.out.println("Irr310 Client - v0.1a");

        // autologin();
        /*
         * LoginForm loginForm = new LoginForm();
         * loginForm.setLocationRelativeTo(loginForm.getParent());
         * loginForm.setVisible(true);
         */

        // Reader reader = new InputStreamReader(System.in);
        // BufferedReader input = new BufferedReader(reader);

        Log.perfEnd(); // Finish Start

        /*
         * try { while (true) { System.out.print("> "); String command =
         * input.readLine(); String output = commandManager.execute(command); if
         * (output != null) { if (!output.isEmpty()) {
         * System.out.println(output); } } else {
         * System.out.println("Game : Exiting..."); break; } } } catch
         * (IOException e) { // Todo handle exception }
         */
        boolean waitStop = true;

        while (waitStop) {
            waitStop = false;
            for (Engine engine : engineList) {
                if (!engine.isStopped()) {
                    waitStop = true;
                    break;
                }
            }
            Duration.HUNDRED_MILLISECONDE.sleep();
        }

        java.lang.System.out.println("Game Client: Stopped");

    }

    public void stop() {

    }

    public void sendToAll(EngineEvent e) {
        for (Engine engine : engineList) {
            engine.pushEvent(e);
        }
    }

    /*private void autologin() {
        SignupTask signupTask = new SignupTask("default", "");
        signupTask.startAndWait();

        LoginTask loginTask = new LoginTask("default", "");
        loginTask.startAndWait();

    }*/

    public void updateCapacityTask(com.irr310.common.world.capacity.Capacity capacity) {
        if (clientNetworkEngine != null) {
            clientNetworkEngine.send(new CapacityUpdateMessage(capacity.toView()));
        }
    }

    public PhysicEngine getPhysicEngine() {
        return physicEngine;
    }

    public ClientNetworkEngine getNetWorkEngine() {
        return clientNetworkEngine;
    }

    public UiEngine getGraphicEngine() {
        return uiEngine;
    }

    public void playSoloGame() {
        sendToAll(new LoadingGameEvent("Create new game ..."));


        world = new World();
        
        // Physic
        physicEngine = new PhysicEngine();
        engineList.add(physicEngine);
        worldEngineList.add(physicEngine);

        // ClientGame
//        ClientGameEngine clientGameEngine = new ClientGameEngine();
//        engineList.add(clientGameEngine);
//        worldEngineList.add(clientGameEngine);

        // ServerGameEngine
        ServerGameEngine serverGameEngine = new ServerGameEngine();
        engineList.add(serverGameEngine);
        worldEngineList.add(serverGameEngine);
        
        // AIEngine
        AIEngine aiEngine = new AIEngine();
        engineList.add(aiEngine);
        worldEngineList.add(aiEngine);

        startAndWaitAllEngines();

        sendToAll(new StartEngineEvent());
        
        initWorld();
        
        Player player = createPlayer("player", "");
        LoginManager.localPlayer = player;

        sendToAll(new WorldReadyEvent());

        java.lang.System.out.println("Game begin");
    }

    private void initWorld() {
        
        Random random = new Random();
        
        Map map = getWorld().getMap();
        
        //Init map
        int factionCount = 5;
        int systemCount = 100;
        double mapSize = 1000;
        double mapMinDistance = mapSize/50;
        
        // Init zone positions
        
        int validSystem = 0;
        while(validSystem < systemCount) {
            
            //double distance = (1 - Math.sqrt(random.nextDouble())) * mapSize;
            double distance = (0.5 * (1 - Math.sqrt(random.nextDouble())) + 0.5 * random.nextDouble()) * mapSize;
            double azimut = random.nextDouble() * 2 * Math.PI;
            
            Vec2 location = new Vec2(0, distance).rotate(azimut);
            

            if(map.getZones().size() > 0) {
                
                System nearestSystem = map.nearestSystemTo(location);
                
                if(nearestSystem.getLocation().distanceTo(location) < mapMinDistance) {
                    // Too near to a existing system, retry
                    Log.trace("Too near to a existing system, retry :"+ nearestSystem.getLocation().distanceTo(location));
                    mapMinDistance--;
                    continue;
                } else {
                    Log.trace("Distance before :"+ nearestSystem.getLocation().distanceTo(location));
                    location = location.add(location.diff(nearestSystem.getLocation()).normalize().multiply(mapMinDistance/2) );
                    Log.trace("Distance after :"+ nearestSystem.getLocation().distanceTo(location));
                }
            }
            
            System system = new System(GameServer.pickNewId(), location);
            map.addZone(system);
            mapMinDistance++;
            
            validSystem++;
        }

        // Find home system
        double baseAzimut = random.nextDouble() * 2 * Math.PI;
        
        List<System> availableHome = new ArrayList<System>();
        
        for(int i = 0; i < factionCount; i++) {
            Vec2 location = new Vec2(0, mapSize/2).rotate(baseAzimut + i * 2 * Math.PI / factionCount);
            availableHome.add(map.nearestSystemTo(location));
        }
        
        
        // Init faction
        for(int i = 0; i < factionCount; i++) {
            // Pick home system
            int homeIndex = random.nextInt(factionCount - i);
            System system = availableHome.get(homeIndex);
            availableHome.remove(homeIndex);
            
            Faction faction = new Faction(GameServer.pickNewId());
            faction.setHomeSystem(system);
            
            getWorld().addFaction(faction);
            
            NexusItem nexus = BuildingItemFactory.createNexus(faction);
            
            
            Game.getInstance().getWorld().addItem(nexus);
            
            nexus.forceDeploy(system, system.getRandomEmptySpace(nexus.getDeployedRadius()));
        }
        

        
        
        map.dump();
    }

    private void startAndWaitAllEngines() {
        for (Engine engine : engineList) {
            if (!engine.isAlive()) {
                engine.start();
            }
        }

        // Wait engines started
        boolean waitStart = true;
        while (waitStart) {
            waitStart = false;
            for (Engine engine : engineList) {
                if (!engine.isRunning()) {
                    waitStart = true;
                    break;
                }
            }
            Duration.TEN_MILLISECONDE.sleep();
        }
    }

    public void gameOver() {
        java.lang.System.err.println("Game over");
        
        for (Engine engine : worldEngineList) {
            engine.pushEvent(new QuitGameEvent());
        }
        
        boolean waitStop = true;

        while (waitStop) {
            waitStop = false;
            for (Engine engine : worldEngineList) {
                if (!engine.isStopped() && !(engine instanceof ServerGameEngine) ) {
                    waitStop = true;
                    break;
                }
            }
            Duration.HUNDRED_MILLISECONDE.sleep();
        }
        
        for (Engine engine : worldEngineList) {
            engineList.remove(engine);
        }
        
        worldEngineList.clear();
        physicEngine = null;
        java.lang.System.err.println("Game cleaned");
    }

}
