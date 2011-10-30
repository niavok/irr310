package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkParam;

public class SignupResponseMessage extends NetworkMessage {

    @NetworkParam
    public boolean success;

    @NetworkParam
    public String reason;

    public SignupResponseMessage() {
        super(NetworkMessageType.SIGNUP_RESPONSE);
    }
    
    public SignupResponseMessage(long responseIndex, boolean success, String reason) {
        super(NetworkMessageType.SIGNUP_RESPONSE);
        setResponseIndex(responseIndex);
        this.success = success;
        this.reason = reason;
    }

    
}
