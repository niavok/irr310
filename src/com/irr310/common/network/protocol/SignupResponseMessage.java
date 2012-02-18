package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;

public class SignupResponseMessage extends NetworkMessage {

    @NetworkField
    public boolean success;

    @NetworkField
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
