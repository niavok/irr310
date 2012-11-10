package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Player;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class WeaponDamageUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
//        List<Ship> shipList = player.getShipList();
//
//        for (Ship ship : shipList) {
//            for (Component component : ship.getComponents()) {
//                List<BalisticWeaponCapacity> capacities = component.getCapacitiesByClass(BalisticWeaponCapacity.class);
//                for (BalisticWeaponCapacity gunCapacity : capacities) {
//                    double lastDamage = gunCapacity.damage;
//
//                    if (playerUpgrade.getRank() > 0) {
//                        gunCapacity.damage *=  (1 + 0.1 * Math.pow(2, playerUpgrade.getRank()));
//                    }
//                    if (gunCapacity.damage != lastDamage) {
//                        // TODO: network !
//                    }
//                }
//                
//                List<RocketWeaponCapacity> rocketCapacities = component.getCapacitiesByClass(RocketWeaponCapacity.class);
//                for (RocketWeaponCapacity rocketCapacity : rocketCapacities) {
//                    double lastDamage = rocketCapacity.explosionDamage;
//
//                    if (playerUpgrade.getRank() > 0) {
//                        rocketCapacity.explosionDamage *=  (1 + 0.1 * Math.pow(2, playerUpgrade.getRank()));
//                    }
//                    if (rocketCapacity.explosionDamage != lastDamage) {
//                        // TODO: network !
//                    }
//                }
//            }
//        }
    }

    @Override
    public Upgrade generateUpgrade() {
        Upgrade weaponDamageUpgrade = new Upgrade();
        weaponDamageUpgrade.setCategory(UpgradeCategory.WEAPON_UPGRADE);
        weaponDamageUpgrade.setGlobalDescription("Increase the damages of all your weapons.");
        weaponDamageUpgrade.setTag("weapon_upgrade.damage");
        weaponDamageUpgrade.setName("Weapon damage");
        weaponDamageUpgrade.addRank(50, "20% damage increase.");
        weaponDamageUpgrade.addRank(200, "40% damage increase.");
        weaponDamageUpgrade.addRank(800, "80% damage increase.");
        weaponDamageUpgrade.addRank(1600, "160% damage increase.");
        weaponDamageUpgrade.addRank(6400, "320% damage increase.");
        return weaponDamageUpgrade;
    }
}
