package com.irr310.server.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.tools.Hash;
import com.irr310.common.world.ShipView;
import com.irr310.server.game.world.Ship;

public class Player extends GameEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    private String login;
    private String password;
    private String passwordSalt;
    private List<Ship> shipList;
    
	public Player(String login) {
        this.login = login;
        shipList = new ArrayList<Ship>();
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

    public void giveShip(Ship ship) {
        shipList.add(ship);
    }
    
    public void removeShip(Ship ship) {
        shipList.remove(ship);
    }
    
    public List<Ship> getShipList() {
        return shipList;
    }

}
