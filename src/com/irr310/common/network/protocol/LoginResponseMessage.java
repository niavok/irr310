package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkParam;

public class LoginResponseMessage extends NetworkMessage {

    @NetworkParam
    public boolean success;

    @NetworkParam
    public String reason;

    public LoginResponseMessage() {
        super(NetworkMessageType.LOGIN_RESPONSE);
    }
    
    public LoginResponseMessage(long responseIndex, boolean success, String reason) {
        super(NetworkMessageType.LOGIN_RESPONSE);
        setResponseIndex(responseIndex);
        this.success = success;
        this.reason = reason;
    }

    
}
