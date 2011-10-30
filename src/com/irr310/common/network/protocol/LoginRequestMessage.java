package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;

public class LoginRequestMessage extends NetworkMessage {

    @NetworkField
    public String login;

    @NetworkField
    public String password;

    public LoginRequestMessage() {
        super(NetworkMessageType.LOGIN_REQUEST);
    }
    
    public LoginRequestMessage(String login, String password) {
        super(NetworkMessageType.LOGIN_REQUEST);
        this.login = login;
        this.password = password;
    }
}
