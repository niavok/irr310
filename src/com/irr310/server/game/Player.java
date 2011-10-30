package com.irr310.server.game;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.tools.Hash;

public class Player extends GameEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    private String login;
    private String password;
    private String passwordSalt;
    
    
	public Player(String login) {
        this.login = login;
	}
	
	public void changePassword(String newPassword) {
	    this.passwordSalt = RandomStringUtils.randomAscii(PASSWORD_SALT_LENGTH);
        this.password = Hash.calculateHash(newPassword, this.passwordSalt);
	}

    public String getLogin() {
        return login;
    }

    public boolean checkPassword(String password) {
        return Hash.calculateHash(password, this.passwordSalt).equals(this.password);
    }

}
