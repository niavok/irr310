package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.GunCapacity;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class WeaponCoolingUpgradeEffect extends UpgradeEffect {

private static final double GUN_BASE_COOLINGSPEED = 10;
    
    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();
        
        for(Ship ship: shipList) {
            for(Component component: ship.getComponents()) {
                Capacity capacity = component.getCapacityByName("gun");
                if(capacity != null) {
                    GunCapacity gunCapacity = (GunCapacity) capacity;
                    System.err.println("gun capacity found");
                    
                    double lastCoolingspeed = gunCapacity.coolingSpeed;
                    System.err.println("last coolingSpeed: "+lastCoolingspeed);
                    
                    if(playerUpgrade.getRank() == 0) {
                        gunCapacity.coolingSpeed = GUN_BASE_COOLINGSPEED;
                    } else {
                        gunCapacity.coolingSpeed = GUN_BASE_COOLINGSPEED / (1 + 0.1 * Math.pow(2,playerUpgrade.getRank()));
                    }
                    System.err.println("new coolingSpeed: "+gunCapacity.coolingSpeed);
                    if(gunCapacity.coolingSpeed != lastCoolingspeed) {
                        //TODO: network !
                    }
                    
                }
            }
        }
    }

}
