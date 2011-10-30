package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;

public class LoginResponseMessage extends NetworkMessage {

    @NetworkField
    public boolean success;

    @NetworkField
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
