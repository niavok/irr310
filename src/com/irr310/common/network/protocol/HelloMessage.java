package com.irr310.common.network.protocol;

public class HelloMessage extends NetworkMessage {

	@NetworkParam
	public String version;
	
	@NetworkParam
	public String userAgent;
}
