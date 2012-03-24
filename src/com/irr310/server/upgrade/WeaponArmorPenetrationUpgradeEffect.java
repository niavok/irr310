package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class WeaponArmorPenetrationUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();
        
        for(Ship ship: shipList) {
            for(Component component: ship.getComponents()) {
                List<BalisticWeaponCapacity> capacities = component.getCapacitiesByClass(BalisticWeaponCapacity.class);
                for (BalisticWeaponCapacity gunCapacity : capacities) {
                    
                    double lastAccuracy = gunCapacity.accuracy;
                    
                    if(playerUpgrade.getRank() > 0) {
                        gunCapacity.armorPenetration *=  (1 + 0.25* 0.1 * Math.pow(2,playerUpgrade.getRank()));
                        if(gunCapacity.armorPenetration > 1) {
                            gunCapacity.armorPenetration = 1;
                        }
                    }
                    if(gunCapacity.armorPenetration != lastAccuracy) {
                        //TODO: network !
                    }
                    
                }
            }
        }
    }

    @Override
    public Upgrade generateUpgrade()  {
        Upgrade weaponCoolingUpgrade = new Upgrade();
        weaponCoolingUpgrade.setCategory(UpgradeCategory.WEAPON_UPGRADE);
        weaponCoolingUpgrade.setGlobalDescription("Increase the armor penetration of all your weapons.");
        weaponCoolingUpgrade.setTag("weapon_upgrade.armor_penetration");
        weaponCoolingUpgrade.setName("Weapon armor penetration");
        weaponCoolingUpgrade.addRank(50, "5% armor penetration increase.");
        weaponCoolingUpgrade.addRank(200, "10% armor penetration increase.");
        weaponCoolingUpgrade.addRank(800, "20% armor penetration increase.");
        weaponCoolingUpgrade.addRank(1600, "40% armor penetration increase.");
        weaponCoolingUpgrade.addRank(6400, "80% armor penetration increase.");
        return weaponCoolingUpgrade;
    }

}
