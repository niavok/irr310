package com.irr310.client.network;

import com.irr310.common.network.protocol.SignupRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;

public class SignupRequest extends NetworkRequest<SignupResponseMessage> {

	public SignupRequest(String login, String password) {
		super(new SignupRequestMessage(login, password), SignupResponseMessage.class);
	}

	
	
}
