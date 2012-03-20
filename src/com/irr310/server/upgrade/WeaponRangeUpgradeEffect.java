package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.WeaponCapacity;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class WeaponRangeUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();
        
        for(Ship ship: shipList) {
            for(Component component: ship.getComponents()) {
                List<WeaponCapacity> capacities = component.getCapacitiesByClass(WeaponCapacity.class);
                for (WeaponCapacity gunCapacity : capacities) {
                    
                    double lastRange = gunCapacity.range;
                    
                    if(playerUpgrade.getRank() > 0) {
                        gunCapacity.range *= (1 +  0.1 * 3 * Math.pow(2,playerUpgrade.getRank()));
                    }
                    if(gunCapacity.range != lastRange) {
                        //TODO: network !
                    }
                    
                }
            }
        }
    }

    @Override
    public Upgrade generateUpgrade() {
        Upgrade weaponFirerateUpgrade = new Upgrade();
        weaponFirerateUpgrade.setCategory(UpgradeCategory.WEAPON_UPGRADE);
        weaponFirerateUpgrade.setGlobalDescription("Increase the range of all your weapons.");
        weaponFirerateUpgrade.setTag("weapon_upgrade.range");
        weaponFirerateUpgrade.setName("Weapon range");
        weaponFirerateUpgrade.addRank(50, "60% range increase.");
        weaponFirerateUpgrade.addRank(200, "120% range increase.");
        weaponFirerateUpgrade.addRank(800, "240% range increase.");
        weaponFirerateUpgrade.addRank(1600, "480% range increase.");
        weaponFirerateUpgrade.addRank(6400, "960% range increase.");
        return weaponFirerateUpgrade;
    }

}
