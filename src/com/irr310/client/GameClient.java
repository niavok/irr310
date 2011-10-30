package com.irr310.client;

import com.irr310.client.game.Player;
import com.irr310.client.network.ClientNetworkEngine;
import com.irr310.client.network.request.FetchShipListRequest;
import com.irr310.client.network.request.LoginRequest;
import com.irr310.client.network.request.SignupRequest;
import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.ShipListRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;
import com.irr310.common.world.ShipView;

public class GameClient {

    public static GameClient instance = null;

    public Player player;

    private ClientNetworkEngine network;

    public static GameClient getInstance() {
        return instance;
    }

    public GameClient() {
        instance = this;

        // Start non logged
        player = null;
        network = new ClientNetworkEngine("127.0.0.10", 22310);
    }

    public void loginTask(final String login, final String password) {
        new Thread() {

            @Override
            public void run() {
                if (isLogged()) {
                    logout();
                }

                LoginRequest loginRequest = new LoginRequest(login, password);
                loginRequest.sendAndWait(network);

                LoginResponseMessage m = loginRequest.getResponseMessage();
                if (!m.success) {
                    System.out.println("login failed: " + m.reason);
                    return;
                }
                System.out.println("login successful");

                network.send(new ShipListRequestMessage());
                
                /*for (ShipView ship : fetchShipListRequest.getResponseMessage().shipsList) {
                    sendToAll(new AddShipEvent(ship));
                }*/

            }
        }.start();

    }

    /*public void loadShipTask(final ShipView ship) {
        new Thread() {
        
            @Override
            public void run() {
            
        LoadShipRequest loadShipRequest = new LoadShipRequest(ship);
        loadShipRequest.sendAndWait(network);
        
        if(ship.getCameraList() > 0) {
            for (Camera camera : ship.getCameraList()) {
                network.sendRequest(new FollowCameraMessage(camera));
                addSimpleCameraViewer(camera);
            }
        }
            }
        }.start();
        
    }*/

    public void signupTask(final String login, final String password) {
        new Thread() {

            @Override
            public void run() {

                if (isLogged()) {
                    return;
                }

                SignupRequest signupRequest = new SignupRequest(login, password);
                signupRequest.sendAndWait(network);

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

    public ClientNetworkEngine getNetwork() {
        return network;
    }

    public boolean isLogged() {
        return player != null;
    }

}
