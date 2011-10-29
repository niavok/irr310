package com.irr310.common.network.protocol;

public class HelloMessage extends NetworkMessage {

	public HelloMessage() {
        super(NetworkMessageType.HELLO);
    }

    @NetworkParam
	public String version;
	
	@NetworkParam
	public String userAgent;
}
