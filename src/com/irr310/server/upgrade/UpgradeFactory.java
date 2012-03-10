package com.irr310.server.upgrade;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.world.World;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class UpgradeFactory {

    private static Map<String, UpgradeEffect> effectMap = new HashMap<String, UpgradeEffect>();
    
    public static void initUpgrades() {
        World world = Game.getInstance().getWorld();
        
        
        
        {
            Upgrade weaponDamageUpgrade = new Upgrade();
            weaponDamageUpgrade.setCategory(UpgradeCategory.WEAPON);
            weaponDamageUpgrade.setGlobalDescription("Increase the damages of all your weapons.");
            weaponDamageUpgrade.setTag("weapon.damage");
            weaponDamageUpgrade.setName("Weapon damage");
            weaponDamageUpgrade.addRank(50, "20% damage increase.");
            weaponDamageUpgrade.addRank(200, "40% damage increase.");
            weaponDamageUpgrade.addRank(800, "80% damage increase.");
            weaponDamageUpgrade.addRank(1600, "160% damage increase.");
            weaponDamageUpgrade.addRank(6400, "320% damage increase.");
            world.addUpgrade(weaponDamageUpgrade);
            effectMap.put(weaponDamageUpgrade.getTag(), new WeaponDamageUpgradeEffect());
        }
        
        {
            Upgrade weaponCoolingUpgrade = new Upgrade();
            weaponCoolingUpgrade.setCategory(UpgradeCategory.WEAPON);
            weaponCoolingUpgrade.setGlobalDescription("Increase the cooling speed of all your weapons.");
            weaponCoolingUpgrade.setTag("weapon.cooling");
            weaponCoolingUpgrade.setName("Weapon cooling");
            weaponCoolingUpgrade.addRank(50, "20% cooling speed increase.");
            weaponCoolingUpgrade.addRank(200, "40% cooling increase.");
            weaponCoolingUpgrade.addRank(800, "80% cooling increase.");
            weaponCoolingUpgrade.addRank(1600, "160% cooling increase.");
            weaponCoolingUpgrade.addRank(6400, "320% cooling increase.");
            world.addUpgrade(weaponCoolingUpgrade);
            effectMap.put(weaponCoolingUpgrade.getTag(), new WeaponCoolingUpgradeEffect());
        }
        
        {
            Upgrade weaponFirerateUpgrade = new Upgrade();
            weaponFirerateUpgrade.setCategory(UpgradeCategory.WEAPON);
            weaponFirerateUpgrade.setGlobalDescription("Increase the firerate of all your weapons.");
            weaponFirerateUpgrade.setTag("weapon.firerate");
            weaponFirerateUpgrade.setName("Weapon firerate");
            weaponFirerateUpgrade.addRank(50, "20% firerate increase.");
            weaponFirerateUpgrade.addRank(200, "40% firerate increase.");
            weaponFirerateUpgrade.addRank(800, "80% firerate increase.");
            weaponFirerateUpgrade.addRank(1600, "160% firerate increase.");
            weaponFirerateUpgrade.addRank(6400, "320% firerate increase.");
            world.addUpgrade(weaponFirerateUpgrade);
            effectMap.put(weaponFirerateUpgrade.getTag(), new WeaponFirerateUpgradeEffect());
        }
        
    }

    public static void apply(UpgradeOwnership playerUpgrade) {
        UpgradeEffect upgradeEffect = effectMap.get(playerUpgrade.getUpgrade().getTag());
        upgradeEffect.apply(playerUpgrade);
        
    }
}
