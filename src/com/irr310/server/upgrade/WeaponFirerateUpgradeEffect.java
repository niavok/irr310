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

    
    
    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();
        
        for(Ship ship: shipList) {
            for(Component component: ship.getComponents()) {
                List<GunCapacity> capacities = component.getCapacitiesByClass(GunCapacity.class);
                for (GunCapacity gunCapacity : capacities) {
                    
                    double lastFirerate = gunCapacity.firerate;
                    
                    if(playerUpgrade.getRank() > 0) {
                        gunCapacity.firerate *= (1 + 0.1 * Math.pow(2,playerUpgrade.getRank()));
                    }
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
