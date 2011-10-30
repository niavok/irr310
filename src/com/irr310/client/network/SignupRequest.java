package com.irr310.client.network;

import com.irr310.common.network.protocol.SignupRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;

public class SignupRequest extends NetworkRequest {

	public SignupRequest(String login, String password) {
		super(new SignupRequestMessage(login, password), new SignupResponseMessage());
	}

	
	
}
