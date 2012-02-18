package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;

public class SignupRequestMessage extends NetworkMessage {

    @NetworkField
    public String login;

    @NetworkField
    public String password;

    public SignupRequestMessage() {
        super(NetworkMessageType.SIGNUP_REQUEST);
    }
    
    public SignupRequestMessage(String login, String password) {
        super(NetworkMessageType.SIGNUP_REQUEST);
        this.login = login;
        this.password = password;
    }
}
