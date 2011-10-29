package com.irr310.common.network.protocol;

public class LoginRequestMessage extends NetworkMessage {

    @NetworkParam
    public String login;

    @NetworkParam
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
