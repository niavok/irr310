package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.irr310.common.tools.Hash;
import com.irr310.common.world.item.ItemSlot;
import com.irr310.common.world.item.ShipSchema;
import com.irr310.common.world.state.PlayerState;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class Player extends WorldEntity {

    private static final int PASSWORD_SALT_LENGTH = 32;
    
    private String login;
    private String password;
    private String passwordSalt;
//    private List<ItemOld> inventory;
    private List<UpgradeOwnership> upgrades;
    private int  money;

    private double lastInterrest;

    private ShipSchema shipShema;

    private boolean human;

    private Faction faction;

    private boolean local;

    
	public Player(World world, long id, String login) {
	    super(world, id);
        this.login = login;
        
        upgrades = new ArrayList<UpgradeOwnership>();
//        inventory = new ArrayList<ItemOld>();
        money = 0;
        lastInterrest = 0;
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

    public void setHuman(boolean human) {
        this.human = human;
    }

    public PlayerState toState() {
        PlayerState playerView = new PlayerState();
        playerView.id = getId();
        playerView.login = login;
        playerView.faction = faction.toState();
        return playerView;
    }

    public void fromState(PlayerState playerState) {
        login = playerState.login;
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
//            Game.getInstance().sendToAll(new MoneyChangedEvent(amount, this, true));
        }
    }
    
    public void retireMoney(int amount) {
        if(amount > 0) {
            money -= amount;
//            Game.getInstance().sendToAll(new MoneyChangedEvent(amount, this, true));
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
    
//    public List<ItemOld> getInventory() {
//        return inventory;
//    }
    
//    public void giveItem(ItemOld item) {
//        inventory.add(item);
////        Game.getInstance().sendToAll(new InventoryChangedEvent(this, item, ChangeType.ADDED));
//    }
//    
//    public void retireItem(ItemOld item) {
//        if(item.isUsed()) {
//            for (ItemSlot slot : shipShema.getItemSlots()) {
//                if(slot.getContent() == item) {
//                    slot.setContent(null);
//                }
//            }
//        }
//        inventory.remove(item);
////        Game.getInstance().sendToAll(new InventoryChangedEvent(this, item, ChangeType.REMOVE));
//    }

//    public Ship getPreferredShip() {
//        return getShipList().get(0);
//    }
    
    public ShipSchema getShipSchema() {
        return shipShema;
    }
    
    public void setShipShema(ShipSchema shipShema) {
        this.shipShema = shipShema;
    }

//    public void removeItemByName(String string) {
//        ItemOld itemToRemove = null;
//        
//        //Look for a not used item
//        for (ItemOld item : inventory) {
//            if(item.getName().equals(string) && !item.isUsed()) {
//                itemToRemove = item;
//            }
//        }
//        if(itemToRemove != null) {
//            retireItem(itemToRemove);
//            return;
//        }
//        
//        // Look an used item
//        for (ItemOld item : inventory) {
//            if(item.getName().equals(string)) {
//                itemToRemove = item;
//            }
//        }
//        if(itemToRemove != null) {
//            retireItem(itemToRemove);
//        }
//    }

    public boolean isHuman() {
        return human;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
    
    public boolean isLocal() {
        return local;
    }
}
