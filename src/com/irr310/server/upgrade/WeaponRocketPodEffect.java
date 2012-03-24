package com.irr310.server.upgrade;

import com.irr310.common.world.Player;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.server.GameServer;

public class WeaponRocketPodEffect extends UpgradeEffect {

    
    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        int currentWeaponCount = 0;
        for(Item item: player.getInventory()) {
            if(item.getName().equals("weapon.rocketpod")) {
                currentWeaponCount ++;
            }
        }

        if(currentWeaponCount > playerUpgrade.getRank()) {
            while(currentWeaponCount > playerUpgrade.getRank()){
                player.removeItemByName("weapon.rocketpod");
                currentWeaponCount--;
            }
                
        } else {
            while(currentWeaponCount < playerUpgrade.getRank()){
                player.giveItem(new Item(GameServer.pickNewId(), "weapon.rocketpod",player));
                currentWeaponCount++;
            }
        }
        
    }

    @Override
    public Upgrade generateUpgrade() {
        Upgrade weaponDamageUpgrade = new Upgrade();
        weaponDamageUpgrade.setCategory(UpgradeCategory.WEAPONS);
        weaponDamageUpgrade.setGlobalDescription("Buy a rocket pod.");
        weaponDamageUpgrade.setTag("weapon.rocketpod");
        weaponDamageUpgrade.setName("Rocket Pod");
        
        for(int i = 0; i  < 21 ; i++) {
            weaponDamageUpgrade.addRank((int) (200 * Math.pow(1.5, i)), ""+(i+1)+" rocket pod.");
        }
        return weaponDamageUpgrade;
    }
}
