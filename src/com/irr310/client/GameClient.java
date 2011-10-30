package com.irr310.client;

import com.irr310.client.game.Player;
import com.irr310.client.network.ClientNetworkEngine;
import com.irr310.client.network.LoginRequest;
import com.irr310.client.network.SignupRequest;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;

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

    public void login(final String login, final String password) {
        new Thread() {

            @Override
            public void run() {
                if (isLogged()) {
                    logout();
                }

                LoginRequest loginRequest = new LoginRequest(login, password);
                loginRequest.sendAndWait(network);

                LoginResponseMessage m = loginRequest.getResponseMessage();
                if (m.success) {
                    System.out.println("login successful");
                } else {
                    System.out.println("login failed: " + m.reason);
                }

            }
        }.start();

    }

    public void signup(final String login, final String password) {
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
