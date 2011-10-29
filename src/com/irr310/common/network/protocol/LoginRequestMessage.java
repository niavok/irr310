package com.irr310.common.network.protocol;

public class LoginRequestMessage extends NetworkMessage {

	@NetworkParam
	public String login;
	
	@NetworkParam
	public String password;
}
