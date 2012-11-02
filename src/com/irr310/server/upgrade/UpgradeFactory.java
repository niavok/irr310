package com.irr310.server.upgrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.world.Player;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
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

    private static final double ROCKETPOD_BASE_DAMAGE = 100;
    private static final double ROCKETPOD_BASE_RADIUS = 50;
    private static final double ROCKETPOD_BASE_BLAST = 50;
    private static final double ROCKETPOD_BASE_COOLDOWN = 10;
    private static final double ROCKETPOD_BASE_FIRERATE = 3;
    private static final double ROCKETPOD_BASE_THRUST = 3;
    private static final double ROCKETPOD_BASE_THRUST_DURATION = 2;
    private static final double ROCKETPOD_BASE_STABILITY = 0.005;
    private static final double ROCKETPOD_BASE_ARMOR_PENETRATION = 0.05;
    
    private static final double REACTOR_BASE_MAX_THRUST = 20;
    private static final double REACTOR_BASE_MIN_THRUST = -8;
    private static final double REACTOR_BASE_VARIATION_SPEED = 16;
    
    public static void initUpgrades() {

        addUpgrade(new WeaponGunEffect());
        addUpgrade(new WeaponShotgunEffect());
        addUpgrade(new WeaponRocketPodEffect());
//        addUpgrade(new WeaponCannonEffect());
//        addUpgrade(new WeaponLaserEffect());

        addUpgrade(new WeaponDamageUpgradeEffect());
        addUpgrade(new WeaponFirerateUpgradeEffect());
        addUpgrade(new WeaponCoolingUpgradeEffect());
        addUpgrade(new WeaponRangeUpgradeEffect());
        addUpgrade(new WeaponAccuracyUpgradeEffect());
        addUpgrade(new WeaponArmorPenetrationUpgradeEffect());
        
        //Ship
        addUpgrade(new ReactorThrustUpgradeEffect());
        addUpgrade(new ReactorReverseThrustUpgradeEffect());
        addUpgrade(new ReactorReactivityUpgradeEffect());
    }

    private static void addUpgrade(UpgradeEffect upgradeEffect) {
        Upgrade upgrade = upgradeEffect.generateUpgrade();
//        Game.getInstance().getWorld().addUpgrade(upgrade);
        effectMap.put(upgrade.getTag(), upgradeEffect);

    }

    private static void apply(UpgradeOwnership playerUpgrade) {
        UpgradeEffect upgradeEffect = effectMap.get(playerUpgrade.getUpgrade().getTag());
        upgradeEffect.apply(playerUpgrade);

    }

    public static void refresh(Player player) {
        initWorldUpgradables(player);
//        for (Upgrade upgrade : Game.getInstance().getWorld().getAvailableUpgrades()) {
//            apply(player.getUpgradeState(upgrade));
//        }

    }

    private static void initWorldUpgradables(Player player) {

        List<Ship> shipList = player.getShipList();
        for (Ship ship : shipList) {
            for (Component component : ship.getComponents()) {
                List<BalisticWeaponCapacity> capacities = component.getCapacitiesByClass(BalisticWeaponCapacity.class);
                for (BalisticWeaponCapacity gunCapacity : capacities) {
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
                        gunCapacity.armorPenetration = SHOTGUN_BASE_ARMOR_PENETRATION;
                    }
                }
                
                List<RocketWeaponCapacity> rocketCapacities = component.getCapacitiesByClass(RocketWeaponCapacity.class);
                for (RocketWeaponCapacity rocketCapacity : rocketCapacities) {
                    rocketCapacity.explosionDamage = ROCKETPOD_BASE_DAMAGE;
                    rocketCapacity.explosionRadius = ROCKETPOD_BASE_RADIUS;
                    rocketCapacity.explosionBlast = ROCKETPOD_BASE_BLAST;
                    rocketCapacity.cooldown = ROCKETPOD_BASE_COOLDOWN;
                    rocketCapacity.thrust = ROCKETPOD_BASE_THRUST;
                    rocketCapacity.thrustDuration = ROCKETPOD_BASE_THRUST_DURATION;
                    rocketCapacity.stability = ROCKETPOD_BASE_STABILITY;
                    rocketCapacity.armorPenetration = ROCKETPOD_BASE_ARMOR_PENETRATION;
                    rocketCapacity.firerate = ROCKETPOD_BASE_FIRERATE;
                }
                
                List<LinearEngineCapacity> engineCapacities = component.getCapacitiesByClass(LinearEngineCapacity.class);
                for (LinearEngineCapacity engineCapacity : engineCapacities) {
                    if(engineCapacity.getName().equals("reactor")) {
                        engineCapacity.theoricalMaxThrust = REACTOR_BASE_MAX_THRUST;
                        engineCapacity.theoricalMinThrust = REACTOR_BASE_MIN_THRUST;
                        engineCapacity.theoricalVariationSpeed = REACTOR_BASE_VARIATION_SPEED;
                    }
                }
            }
        }

    }

    public static void refresh() {
//        for (Player player : Game.getInstance().getWorld().getPlayers()) {
//            refresh(player);
//        }
    }
}
