package com.irr310.server.upgrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.WeaponCapacity;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class UpgradeFactory {

    private static Map<String, UpgradeEffect> effectMap = new HashMap<String, UpgradeEffect>();
    private static final double GUN_BASE_DAMAGE = 50;
    private static final double GUN_BASE_FIRERATE = 1;
    private static final double GUN_BASE_RANGE = 1500;
    private static final double GUN_BASE_ACCURACY = 10;
    private static final double GUN_BASE_COOLINGSPEED = 10;
    private static final double GUN_BASE_HEATINGSPEED = 5;
    private static final double GUN_BASE_ARMOR_PENETRATION = 0.1;

    private static final double SHOTGUN_BASE_DAMAGE = 20;
    private static final double SHOTGUN_BASE_FIRERATE = 0.5;
    private static final double SHOTGUN_BASE_RANGE = 400;
    private static final double SHOTGUN_BASE_ACCURACY = 150;
    private static final double SHOTGUN_BASE_COOLINGSPEED = 15;
    private static final double SHOTGUN_BASE_HEATINGSPEED = 5;
    private static final double SHOTGUN_BASE_ARMOR_PENETRATION = 0.2;

    public static void initUpgrades() {

        addUpgrade(new WeaponGunEffect());
        addUpgrade(new WeaponShotgunEffect());
        addUpgrade(new WeaponCannonEffect());
        addUpgrade(new WeaponLaserEffect());

        addUpgrade(new WeaponDamageUpgradeEffect());
        addUpgrade(new WeaponFirerateUpgradeEffect());
        addUpgrade(new WeaponCoolingUpgradeEffect());
        addUpgrade(new WeaponRangeUpgradeEffect());
        addUpgrade(new WeaponAccuracyUpgradeEffect());
        addUpgrade(new WeaponArmorPenetrationUpgradeEffect());
    }

    private static void addUpgrade(UpgradeEffect upgradeEffect) {
        Upgrade upgrade = upgradeEffect.generateUpgrade();
        Game.getInstance().getWorld().addUpgrade(upgrade);
        effectMap.put(upgrade.getTag(), upgradeEffect);

    }

    private static void apply(UpgradeOwnership playerUpgrade) {
        UpgradeEffect upgradeEffect = effectMap.get(playerUpgrade.getUpgrade().getTag());
        upgradeEffect.apply(playerUpgrade);

    }

    public static void refresh(Player player) {
        initWorldUpgradables(player);
        for (Upgrade upgrade : Game.getInstance().getWorld().getAvailableUpgrades()) {
            apply(player.getUpgradeState(upgrade));
        }

    }

    private static void initWorldUpgradables(Player player) {

        List<Ship> shipList = player.getShipList();
        for (Ship ship : shipList) {
            for (Component component : ship.getComponents()) {
                List<WeaponCapacity> capacities = component.getCapacitiesByClass(WeaponCapacity.class);
                for (WeaponCapacity gunCapacity : capacities) {
                    if (gunCapacity.getName().equals("gun")) {
                        gunCapacity.damage = GUN_BASE_DAMAGE;
                        gunCapacity.accuracy = GUN_BASE_ACCURACY;
                        gunCapacity.coolingSpeed = GUN_BASE_COOLINGSPEED;
                        gunCapacity.firerate = GUN_BASE_FIRERATE;
                        gunCapacity.heatingSpeed = GUN_BASE_HEATINGSPEED;
                        gunCapacity.range = GUN_BASE_RANGE;
                        gunCapacity.armorPenetration = GUN_BASE_ARMOR_PENETRATION;
                    } else if (gunCapacity.getName().equals("shotgun")) {
                        gunCapacity.damage = SHOTGUN_BASE_DAMAGE;
                        gunCapacity.accuracy = SHOTGUN_BASE_ACCURACY;
                        gunCapacity.coolingSpeed = SHOTGUN_BASE_COOLINGSPEED;
                        gunCapacity.firerate = SHOTGUN_BASE_FIRERATE;
                        gunCapacity.heatingSpeed = SHOTGUN_BASE_HEATINGSPEED;
                        gunCapacity.range = SHOTGUN_BASE_RANGE;
                        gunCapacity.armorPenetration = GUN_BASE_ARMOR_PENETRATION;
                    }
                }
            }
        }

    }

    public static void refresh() {
        for (Player player : Game.getInstance().getWorld().getPlayers()) {
            refresh(player);
        }
    }
}
