package com.irr310.common.network.protocol;

public class LoginRequestMessage extends NetworkMessage {

    @NetworkParam
    public String login;

    @NetworkParam
    public String password;

    public LoginRequestMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
