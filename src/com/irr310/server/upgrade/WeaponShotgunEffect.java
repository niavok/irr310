package com.irr310.server.upgrade;

import com.irr310.common.world.Player;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.server.GameServer;

public class WeaponShotgunEffect extends UpgradeEffect {

    
    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        int currentWeaponCount = 0;
        for(Item item: player.getInventory()) {
            if(item.getName().equals("weapon.shotgun")) {
                currentWeaponCount ++;
            }
        }
        
        System.err.println("current count: "+currentWeaponCount);
        System.err.println("target count: "+playerUpgrade.getRank());
        
        if(currentWeaponCount > playerUpgrade.getRank()) {
            while(currentWeaponCount > playerUpgrade.getRank()){
                player.removeItemByName("weapon.shotgun");
                currentWeaponCount--;
            }
                
        } else {
            while(currentWeaponCount < playerUpgrade.getRank()){
                player.giveItem(new Item(GameServer.pickNewId(), "weapon.shotgun"));
                currentWeaponCount++;
            }
        }
        
    }

    @Override
    public Upgrade generateUpgrade() {
        Upgrade weaponDamageUpgrade = new Upgrade();
        weaponDamageUpgrade.setCategory(UpgradeCategory.WEAPONS);
        weaponDamageUpgrade.setGlobalDescription("Buy a shotgun.");
        weaponDamageUpgrade.setTag("weapon.shotgun");
        weaponDamageUpgrade.setName("Shotgun");
        
        for(int i = 0; i  < 21 ; i++) {
            weaponDamageUpgrade.addRank((int) (100 * Math.pow(1.5, i)), ""+(i+1)+" shotgun.");
        }
        return weaponDamageUpgrade;
    }
}
