package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkParam;

public class HelloMessage extends NetworkMessage {

	public HelloMessage() {
        super(NetworkMessageType.HELLO);
    }

    @NetworkParam
	public String version;
	
	@NetworkParam
	public String userAgent;
}
