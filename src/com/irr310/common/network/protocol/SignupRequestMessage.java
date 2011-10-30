package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkParam;

public class SignupRequestMessage extends NetworkMessage {

    @NetworkParam
    public String login;

    @NetworkParam
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
