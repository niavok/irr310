package com.irr310.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.engine.Engine;
import com.irr310.common.event.AddShipEvent;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.server.network.ServerNetworkEngine;
import com.irr310.server.ui.DebugGraphicEngine;

public class GameServer extends Game {
    private ServerGameEngine gameEngine;
    private ServerNetworkEngine networkEngine;
    private PhysicEngine physicEngine;
    private ParameterAnalyser parameterAnalyser;
    private boolean stillRunning;
    private CommandManager commandManager;
    private DebugGraphicEngine debugGraphicEngine;
    private World world;

    private List<Player> playerList = new ArrayList<Player>();
    private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();
    private Map<String, Player> playerLoginMap = new HashMap<String, Player>();

    public static GameServer instance = null;

    
    private static long nextId = 0;
    
    public  static synchronized long pickNewId() {
        return nextId++;
    }
    
    
    public static GameServer getInstance() {
        return instance;
    }

    public GameServer(ParameterAnalyser parameterAnalyser) {
        this.parameterAnalyser = parameterAnalyser;
        instance = this;
        super.setInstance(this);
        stillRunning = true;

        world = new World();

        gameEngine = new ServerGameEngine();
        physicEngine = new PhysicEngine();
        networkEngine = new ServerNetworkEngine();
        debugGraphicEngine = new DebugGraphicEngine();

        commandManager = new CommandManager();

    }

    public void run() {
        // boolean currentStillRunning = stillRunning;

        gameEngine.start();
        physicEngine.start();
        networkEngine.start();
        debugGraphicEngine.start();

        // currentStillRunning = stillRunning;
        // std::string i;
        // std::string o;

        // Wait engines started
        while ((Engine.getRunningEngineCount()) < 4) {
            new Duration(100000000).sleep();
        }

        sendToAll(new StartEngineEvent());
        /*
         * AddShipEvent addShipEvent = new AddShipEvent();
         * addShipEvent.setType(AddShipEvent.Type.SIMPLE);
         * sendToAll(addShipEvent);
         */

        System.out.println("Irr310 - v0.1a");

        Reader reader = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(reader);

        try {
            while (true) {
                System.out.print("> ");

                String command = input.readLine();

                String output = commandManager.execute(command);

                if (output != null) {
                    if (!output.isEmpty()) {
                        System.out.println(output);
                    }
                } else {
                    System.out.println("Game : Exiting...");
                    break;
                }

            }

        } catch (IOException e) {
            // Todo handle exception
        }

        int count;
        System.out.println("Game : Stopping");
        sendToAll(new QuitGameEvent());

        while ((count = Engine.getRunningEngineCount()) > 0) {
            System.out.println("Game : Wait for engine stop, still " + count + " engines");
            Duration.ONE_SECOND.sleep();
        }
        System.out.println("Game : Stopped");

    }

    public void stop() {

    }

    public void sendToAll(EngineEvent e) {
        gameEngine.pushEvent(e);
        physicEngine.pushEvent(e);
        networkEngine.pushEvent(e);
        debugGraphicEngine.pushEvent(e);
    }

    public ServerGameEngine getGameEngine() {
        return gameEngine;
    }

    public PhysicEngine getPhysicEngine() {
        return physicEngine;
    }

    public ServerNetworkEngine GetNetworkEngine() {
        return networkEngine;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Player createPlayer(String login, String password) {
        Player newPlayer = new Player(GameServer.pickNewId(), login);
        newPlayer.changePassword(password);
        playerLoginMap.put(login, newPlayer);
        // Ship playerShip = ShipFactory.createSimpleShip();

        AddShipEvent addShipEvent = new AddShipEvent(newPlayer);
        addShipEvent.setType(AddShipEvent.Type.SIMPLE);
        GameServer.getInstance().sendToAll(addShipEvent);

        /* world.addShip(playerShip, new Vect3(10.0,20.0,30.0)); */

        return newPlayer;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayerByLogin(String login) {
        return playerLoginMap.get(login);
    }

    public boolean isPlayerExist(String login) {
        return playerLoginMap.containsKey(login);
    }

}
