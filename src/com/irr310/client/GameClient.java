package com.irr310.client;

import com.irr310.client.game.Player;
import com.irr310.client.network.ClientNetworkEngine;
import com.irr310.client.network.LoginRequest;
import com.irr310.client.network.SignupRequest;


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

	public void login(String login, String password) {
		if(isLogged()) {
			logout();
		}
		
		LoginRequest loginRequest = new LoginRequest(login, password);
		loginRequest.sendAndWait(network);
		
	}
	
	public void signup(String login, String password) {
	    if(isLogged()) {
           return;
        }
        
        SignupRequest signupRequest = new SignupRequest(login, password);
        signupRequest.sendAndWait(network);
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
