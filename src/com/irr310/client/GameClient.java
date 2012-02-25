package com.irr310.client;

import java.util.ArrayList;
import java.util.List;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.input.InputEngine;
import com.irr310.client.navigation.LoginTask;
import com.irr310.client.navigation.SignupTask;
import com.irr310.client.network.ClientNetworkEngine;
import com.irr310.common.Game;
import com.irr310.common.engine.Engine;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.LoadingGameEvent;
import com.irr310.common.network.protocol.CapacityUpdateMessage;
import com.irr310.common.tools.Log;
import com.irr310.common.world.World;
import com.irr310.server.Duration;
import com.irr310.server.ParameterAnalyser;

public class GameClient extends Game {

    List<Engine> engineList = new ArrayList<Engine>();
    // private ClientGameEngine clientGameEngine;
    // private InputEngine inputEngine;
    private ClientNetworkEngine clientNetworkEngine;
    private PhysicEngine physicEngine;
    // private ClientScriptEngine scriptEngine;
    private GraphicEngine graphicEngine;
    //private ParameterAnalyser parameterAnalyser;
    //private boolean stillRunning;
    //private CommandManager commandManager;

    private final World world;

    public static GameClient instance = null;

    public static GameClient getInstance() {
        return instance;
    }

    public GameClient(ParameterAnalyser parameterAnalyser) {
        //this.parameterAnalyser = parameterAnalyser;
        instance = this;
        super.setInstance(this);
        //stillRunning = true;

        world = new World();

        //clientNetworkEngine = new ClientNetworkEngine("127.0.0.10", 22310);
        physicEngine = new PhysicEngine();
        graphicEngine = new GraphicEngine();

        engineList.add(new InputEngine());
        //engineList.add(new ClientGameEngine());
        //engineList.add(physicEngine);
        //engineList.add(clientNetworkEngine);
        engineList.add(graphicEngine);
        //engineList.add(new ClientScriptEngine());

        // inputEngine = new InputEngine();
        // clientGameEngine = new ClientGameEngine();
        // scriptEngine = new ClientScriptEngine();

        //commandManager = new CommandManager();

    }

    @Override
    public World getWorld() {
        return world;
    }

    public void run() {

        Log.perfBegin("Start");
        Log.perfBegin("Start Engine");
        for (Engine engine : engineList) {
            engine.start();
        }
        // inputEngine.start();
        // clientGameEngine.start();
        // physicEngine.start();
        // clientNetworkEngine.start();
        // graphicEngine.start();
        // scriptEngine.start();

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
            Duration.HUNDRED_MILLISECONDE.sleep();
        }
        Log.perfEnd(); // Start Engine
        Log.perfBegin("Finish Start");
        sendToAll(new StartEngineEvent());

        System.out.println("Irr310 Client - v0.1a");

        //autologin();
        /*
         * LoginForm loginForm = new LoginForm();
         * loginForm.setLocationRelativeTo(loginForm.getParent());
         * loginForm.setVisible(true);
         */

        //Reader reader = new InputStreamReader(System.in);
        //BufferedReader input = new BufferedReader(reader);

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
      
        System.out.println("Game Client: Stopped");

    }

    public void stop() {

    }

    public void sendToAll(EngineEvent e) {
        for (Engine engine : engineList) {
            engine.pushEvent(e);
        }
    }

    private void autologin() {
        SignupTask signupTask = new SignupTask("f", "");
        signupTask.startAndWait();

        LoginTask loginTask = new LoginTask("f", "");
        loginTask.startAndWait();

    }

    public void updateCapacityTask(com.irr310.common.world.capacity.Capacity capacity) {
        clientNetworkEngine.send(new CapacityUpdateMessage(capacity.toView()));
    }

    public PhysicEngine getPhysicEngine() {
        return physicEngine;
    }

    public ClientNetworkEngine getNetWorkEngine() {
        return clientNetworkEngine;
    }

    public GraphicEngine getGraphicEngine() {
        return graphicEngine;
    }

    public void playSoloGame() {
        sendToAll(new LoadingGameEvent("Create new game ..."));
    }

}
