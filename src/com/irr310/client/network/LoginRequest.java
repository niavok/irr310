package com.irr310.client.network;

import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;

public class LoginRequest extends NetworkRequest<LoginResponseMessage>{

	public LoginRequest(String login, String password) {
		super(new LoginRequestMessage(login, password), LoginResponseMessage.class);
	}

	
	
}
