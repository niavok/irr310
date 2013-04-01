package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkOptionalField;
import com.irr310.common.world.state.PlayerState;

public class LoginResponseMessage extends NetworkMessage {

    @NetworkField
    public boolean success;

    @NetworkField
    public String reason;

    @NetworkOptionalField
    public PlayerState player;

    public LoginResponseMessage() {
        super(NetworkMessageType.LOGIN_RESPONSE);
    }
    
    public LoginResponseMessage(long responseIndex, boolean success, String reason, PlayerState player) {
        super(NetworkMessageType.LOGIN_RESPONSE);
        setResponseIndex(responseIndex);
        this.player = player;
        this.success = success;
        this.reason = reason;
    }

    
}
