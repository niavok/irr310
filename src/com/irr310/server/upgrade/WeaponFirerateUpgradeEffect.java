package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.GunCapacity;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;

public class WeaponFirerateUpgradeEffect extends UpgradeEffect {

    private static final double GUN_BASE_FIRERATE = 1;
    
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
                    
                    double lastFirerate = gunCapacity.firerate;
                    System.err.println("last firerate: "+lastFirerate);
                    
                    if(playerUpgrade.getRank() == 0) {
                        gunCapacity.firerate = GUN_BASE_FIRERATE;
                    } else {
                        gunCapacity.firerate = GUN_BASE_FIRERATE * (1 + 0.1 * Math.pow(2,playerUpgrade.getRank()));
                    }
                    System.err.println("new firerate: "+gunCapacity.firerate);
                    if(gunCapacity.firerate != lastFirerate) {
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
        weaponFirerateUpgrade.setGlobalDescription("Increase the firerate of all your weapons.");
        weaponFirerateUpgrade.setTag("weapon_upgrade.firerate");
        weaponFirerateUpgrade.setName("Weapon firerate");
        weaponFirerateUpgrade.addRank(50, "20% firerate increase.");
        weaponFirerateUpgrade.addRank(200, "40% firerate increase.");
        weaponFirerateUpgrade.addRank(800, "80% firerate increase.");
        weaponFirerateUpgrade.addRank(1600, "160% firerate increase.");
        weaponFirerateUpgrade.addRank(6400, "320% firerate increase.");
        return weaponFirerateUpgrade;
    }

}
