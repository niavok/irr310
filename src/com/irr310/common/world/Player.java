package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.Game;
import com.irr310.common.event.InventoryChangedEvent;
import com.irr310.common.event.InventoryChangedEvent.ChangeType;
import com.irr310.common.event.MoneyChangedEvent;
import com.irr310.common.tools.Hash;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.ItemSlot;
import com.irr310.common.world.item.ShipSchema;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.common.world.view.PlayerView;
import com.irr310.common.world.zone.GameEntity;
import com.irr310.common.world.zone.Ship;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class Player extends GameEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    
    private String login;
    private String password;
    private String passwordSalt;
    private List<Ship> shipList;
    private List<Item> inventory;
    private List<UpgradeOwnership> upgrades;
    private int  money;
    private V3DColor color;

    private double lastInterrest;

    private ShipSchema shipShema;

    private boolean human;

    
	public Player(long id, String login) {
	    super(id);
        this.login = login;
        shipList = new ArrayList<Ship>();
        upgrades = new ArrayList<UpgradeOwnership>();
        inventory = new ArrayList<Item>();
        money = 0;
        lastInterrest = 0;
        color = V3DColor.grey;
        human = false;
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
    
    public void setHuman(boolean human) {
        this.human = human;
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
        upgradeOwnership.setRank(upgrade.getInitialRank());
        upgrades.add(upgradeOwnership);
        
        
        return upgradeOwnership;
    }

    public void giveInterrest(double interrest) {
        double gain =  lastInterrest + interrest;
        int realGain = (int) gain;
        lastInterrest = gain -  realGain;
        giveMoney(realGain);
    }
    
    public List<Item> getInventory() {
        return inventory;
    }
    
    public void giveItem(Item item) {
        inventory.add(item);
        Game.getInstance().sendToAll(new InventoryChangedEvent(this, item, ChangeType.ADDED));
    }
    
    public void retireItem(Item item) {
        if(item.isUsed()) {
            for (ItemSlot slot : shipShema.getItemSlots()) {
                if(slot.getContent() == item) {
                    slot.setContent(null);
                }
            }
        }
        inventory.remove(item);
        Game.getInstance().sendToAll(new InventoryChangedEvent(this, item, ChangeType.REMOVE));
    }

    public Ship getPreferredShip() {
        return getShipList().get(0);
    }
    
    public ShipSchema getShipSchema() {
        return shipShema;
    }
    
    public void setShipShema(ShipSchema shipShema) {
        this.shipShema = shipShema;
    }

    public void removeItemByName(String string) {
        Item itemToRemove = null;
        
        //Look for a not used item
        for (Item item : inventory) {
            if(item.getName().equals(string) && !item.isUsed()) {
                itemToRemove = item;
            }
        }
        if(itemToRemove != null) {
            retireItem(itemToRemove);
            return;
        }
        
        // Look an used item
        for (Item item : inventory) {
            if(item.getName().equals(string)) {
                itemToRemove = item;
            }
        }
        if(itemToRemove != null) {
            retireItem(itemToRemove);
        }
    }

    public V3DColor getColor() {
        return color;
    }

    public void setColor(V3DColor color) {
        this.color = color;
    }

    public boolean isHuman() {
        return human;
    }
}
