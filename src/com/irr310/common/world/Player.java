package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.Game;
import com.irr310.common.event.MoneyChangedEvent;
import com.irr310.common.tools.Hash;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.common.world.view.PlayerView;

public class Player extends GameEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    
    private String login;
    private String password;
    private String passwordSalt;
    private List<Ship> shipList;
    private List<UpgradeOwnership> upgrades;
    private int  money;

    private double lastInterrest;
    
	public Player(long id, String login) {
	    super(id);
        this.login = login;
        shipList = new ArrayList<Ship>();
        upgrades = new ArrayList<UpgradeOwnership>();
        money = 0;
        lastInterrest = 0;
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
    
    public void giveMoney(int amount) {
        if(amount > 0) {
            money += amount;
            Game.getInstance().sendToAll(new MoneyChangedEvent(amount, this, true));
        }
    }
    
    public void retireMoney(int amount) {
        if(amount > 0) {
            money -= amount;
            Game.getInstance().sendToAll(new MoneyChangedEvent(amount, this, true));
        }
    }
    
    public List<UpgradeOwnership> getUpgrades() {
        return upgrades;
    }

    public UpgradeOwnership getUpgradeState(Upgrade upgrade) {
        for(UpgradeOwnership upgradeOwnership : upgrades) {
            if(upgradeOwnership.getUpgrade() == upgrade) {
                return upgradeOwnership;
            }
        }
        
        UpgradeOwnership upgradeOwnership = new UpgradeOwnership(upgrade, this);
        upgrades.add(upgradeOwnership);
        
        return upgradeOwnership;
    }

    public void giveInterrest(double interrest) {
        double gain =  lastInterrest + interrest;
        int realGain = (int) gain;
        lastInterrest = gain -  realGain;
        giveMoney(realGain);
    }
}
