package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.world.Player;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class WeaponAccuracyUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
//        List<Ship> shipList = player.getShipList();
//        for(Ship ship: shipList) {
//            for(Component component: ship.getComponents()) {
//                List<BalisticWeaponCapacity> capacities = component.getCapacitiesByClass(BalisticWeaponCapacity.class);
//                for (BalisticWeaponCapacity gunCapacity : capacities) {
//                    
//                    double lastAccuracy = gunCapacity.accuracy;
//                    
//                    if(playerUpgrade.getRank() > 0) {
//                        gunCapacity.accuracy /=  (1 + 0.1 * Math.pow(2,playerUpgrade.getRank()));
//                    }
//                    if(gunCapacity.accuracy != lastAccuracy) {
//                        //TODO: network !
//                    }
//                    
//                }
//                
//                List<RocketWeaponCapacity> rocketCapacities = component.getCapacitiesByClass(RocketWeaponCapacity.class);
//                for (RocketWeaponCapacity rocketCapacity : rocketCapacities) {
//                    double lastAccuracy = rocketCapacity.stability;
//                    
//                    if(playerUpgrade.getRank() > 0) {
//                        rocketCapacity.stability /=  (1 + 10* 0.1 * Math.pow(2,playerUpgrade.getRank()));
//                        Log.trace("new stability "+rocketCapacity.stability);
//                    }
//                    if(rocketCapacity.stability != lastAccuracy) {
//                        //TODO: network !
//                    }
//                }
//                
//            }
//        }
    }

    @Override
    public Upgrade generateUpgrade()  {
        Upgrade weaponCoolingUpgrade = new Upgrade();
        weaponCoolingUpgrade.setCategory(UpgradeCategory.WEAPON_UPGRADE);
        weaponCoolingUpgrade.setGlobalDescription("Increase the accuracy of all your weapons.");
        weaponCoolingUpgrade.setTag("weapon_upgrade.accuracy");
        weaponCoolingUpgrade.setName("Weapon accuracy");
        weaponCoolingUpgrade.addRank(50, "20% accuracy increase.");
        weaponCoolingUpgrade.addRank(200, "40% accuracy increase.");
        weaponCoolingUpgrade.addRank(800, "80% accuracy increase.");
        weaponCoolingUpgrade.addRank(1600, "160% accuracy increase.");
        weaponCoolingUpgrade.addRank(6400, "320% accuracy increase.");
        return weaponCoolingUpgrade;
    }

}
