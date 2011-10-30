package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;

public class HelloMessage extends NetworkMessage {

	public HelloMessage() {
        super(NetworkMessageType.HELLO);
    }

    @NetworkField
	public String version;
	
	@NetworkField
	public String userAgent;
}
