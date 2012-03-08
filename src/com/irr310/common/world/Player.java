package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.Game;
import com.irr310.common.event.MoneyChangedEvent;
import com.irr310.common.tools.Hash;
import com.irr310.common.world.view.PlayerView;

public class Player extends GameEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    
    private String login;
    private String password;
    private String passwordSalt;
    private List<Ship> shipList;
    private int  money;
    private int  embeddedMoney;
    
	public Player(long id, String login) {
	    super(id);
        this.login = login;
        shipList = new ArrayList<Ship>();
        money = 0;
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
        playerView.money = money;
        return playerView;
    }

    public void fromView(PlayerView playerView) {
        login = playerView.login;
        money = playerView.money;
    }

    
    public int getMoney() {
        return money;
    }
    
    public void setMoney(int money) {
        this.money = money;
    }
    
    public int getEmbeddedMoney() {
        return embeddedMoney;
    }
    
    public void setEmbeddedMoney(int embeddedMoney) {
        this.embeddedMoney = embeddedMoney;
    }

    public void giveMoney(int amount, boolean embedded) {
        if(embedded) {
            embeddedMoney += amount;
        } else {
            money += amount;
        }
        Game.getInstance().sendToAll(new MoneyChangedEvent(amount, this, embedded, true));
    }
    
    public void retireMoney(int amount, boolean embedded) {
        if(embedded) {
            embeddedMoney -= amount;
        } else {
            money -= amount;
        }
    }
}
