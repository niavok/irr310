package com.irr310.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.irr310.client.game.ClientGameEngine;
import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.network.ClientNetworkEngine;
import com.irr310.client.network.request.LoginRequest;
import com.irr310.client.network.request.SignupRequest;
import com.irr310.client.script.ClientScriptEngine;
import com.irr310.common.Game;
import com.irr310.common.engine.Engine;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.network.protocol.CapacityUpdateMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.ShipListRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.server.CommandManager;
import com.irr310.server.Duration;
import com.irr310.server.ParameterAnalyser;

public class GameClient extends Game {
    private ClientGameEngine clientGameEngine;
    private ClientNetworkEngine clientNetworkEngine;
    private PhysicEngine physicEngine;
    private ClientScriptEngine scriptEngine;
    private ParameterAnalyser parameterAnalyser;
    private boolean stillRunning;
    private CommandManager commandManager;
    private GraphicEngine graphicEngine;
    private final World world;
    public Player localPlayer;

    public static GameClient instance = null;

    public static GameClient getInstance() {
        return instance;
    }

    public GameClient(ParameterAnalyser parameterAnalyser) {
        this.parameterAnalyser = parameterAnalyser;
        instance = this;
        super.setInstance(this);
        stillRunning = true;

        world = new World();
        
        // Start non logged
        localPlayer = null;

        clientGameEngine = new ClientGameEngine();
        physicEngine = new PhysicEngine();
        clientNetworkEngine = new ClientNetworkEngine("127.0.0.10", 22310);
        graphicEngine = new GraphicEngine();
        scriptEngine = new ClientScriptEngine();

        commandManager = new CommandManager();

    }

    @Override
    public World getWorld() {
        return world;
    }
    
    public void run() {
        // boolean currentStillRunning = stillRunning;

        clientGameEngine.start();
        physicEngine.start();
        clientNetworkEngine.start();
        graphicEngine.start();
        scriptEngine.start();

        // currentStillRunning = stillRunning;
        // std::string i;
        // std::string o;

        // Wait engines started
        while ((Engine.getRunningEngineCount()) < 5) {
            new Duration(100000000).sleep();
        }

        sendToAll(new StartEngineEvent());
        /*
         * AddShipEvent addShipEvent = new AddShipEvent();
         * addShipEvent.setType(AddShipEvent.Type.SIMPLE);
         * sendToAll(addShipEvent);
         */

        System.out.println("Irr310 - v0.1a");

        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(loginForm.getParent());
        loginForm.setVisible(true);
        
        
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
        clientGameEngine.pushEvent(e);
        physicEngine.pushEvent(e);
        clientNetworkEngine.pushEvent(e);
        graphicEngine.pushEvent(e);
        scriptEngine.pushEvent(e);
    }

    /*
     * public GameEngine getGameEngine() { return gameEngine; } public
     * PhysicEngine getPhysicEngine() { return physicEngine; } public
     * ServerNetworkEngine GetNetworkEngine() { return networkEngine; }
     */


    public void loginTask(final String login, final String password) {
        new Thread() {

            @Override
            public void run() {
                if (isLogged()) {
                    logout();
                }

                LoginRequest loginRequest = new LoginRequest(login, password);
                loginRequest.sendAndWait(clientNetworkEngine);

                LoginResponseMessage m = loginRequest.getResponseMessage();
                if (!m.success) {
                    System.out.println("login failed: " + m.reason);
                    return;
                }
                System.out.println("login successful");

                localPlayer = world.loadPlayer(m.player);
                
                clientNetworkEngine.send(new ShipListRequestMessage());

                /*
                 * for (ShipView ship :
                 * fetchShipListRequest.getResponseMessage().shipsList) {
                 * sendToAll(new AddShipEvent(ship)); }
                 */

            }
        }.start();

    }

    /*
     * public void loadShipTask(final ShipView ship) { new Thread() {
     * @Override public void run() { LoadShipRequest loadShipRequest = new
     * LoadShipRequest(ship); loadShipRequest.sendAndWait(network);
     * if(ship.getCameraList() > 0) { for (Camera camera : ship.getCameraList())
     * { network.sendRequest(new FollowCameraMessage(camera));
     * addSimpleCameraViewer(camera); } } } }.start(); }
     */

    public void signupTask(final String login, final String password) {
        new Thread() {

            @Override
            public void run() {

                if (isLogged()) {
                    return;
                }

                SignupRequest signupRequest = new SignupRequest(login, password);
                signupRequest.sendAndWait(clientNetworkEngine);

                SignupResponseMessage m = signupRequest.getResponseMessage();
                if (m.success) {
                    System.out.println("signup successful");
                } else {
                    System.out.println("signup failed: " + m.reason);
                }

            }
        }.start();
    }

    private void logout() {
    }

    public boolean isLogged() {
        return localPlayer != null;
    }

    public void updateCapacityTask(com.irr310.common.world.capacity.Capacity capacity) {
        clientNetworkEngine.send(new CapacityUpdateMessage(capacity.toView()));
    }

    

}
