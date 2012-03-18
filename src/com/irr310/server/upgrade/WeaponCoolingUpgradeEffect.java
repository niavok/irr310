package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.WeaponCapacity;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;

public class WeaponCoolingUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();
        
        for(Ship ship: shipList) {
            for(Component component: ship.getComponents()) {
                List<WeaponCapacity> capacities = component.getCapacitiesByClass(WeaponCapacity.class);
                for (WeaponCapacity gunCapacity : capacities) {
                    
                    double lastCoolingspeed = gunCapacity.coolingSpeed;
                    
                    if(playerUpgrade.getRank() > 0) {
                        gunCapacity.coolingSpeed /=  (1 + 0.1 * Math.pow(2,playerUpgrade.getRank()));
                    }
                    if(gunCapacity.coolingSpeed != lastCoolingspeed) {
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
        weaponCoolingUpgrade.setGlobalDescription("Increase the cooling speed of all your weapons.");
        weaponCoolingUpgrade.setTag("weapon_upgrade.cooling");
        weaponCoolingUpgrade.setName("Weapon cooling");
        weaponCoolingUpgrade.addRank(50, "20% cooling speed increase.");
        weaponCoolingUpgrade.addRank(200, "40% cooling increase.");
        weaponCoolingUpgrade.addRank(800, "80% cooling increase.");
        weaponCoolingUpgrade.addRank(1600, "160% cooling increase.");
        weaponCoolingUpgrade.addRank(6400, "320% cooling increase.");
        return weaponCoolingUpgrade;
    }

}
