package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.tools.Hash;
import com.irr310.common.world.view.PlayerView;
import com.irr310.common.world.view.ShipView;

public class Player extends GameEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    private String login;
    private String password;
    private String passwordSalt;
    private List<Ship> shipList;
    
	public Player(long id, String login) {
	    super(id);
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

    public PlayerView toView() {
        PlayerView playerView = new PlayerView();
        playerView.id = getId();
        playerView.login = login;
        return playerView;
    }

    public void fromView(PlayerView playerView) {
    }

}
