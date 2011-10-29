package com.irr310.common.network.protocol;

public class LoginResponseMessage extends NetworkMessage {

	@NetworkParam
	public boolean success;
	
	@NetworkParam
	public boolean userExist;
}
