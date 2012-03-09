package com.irr310.server;

import com.irr310.common.Game;
import com.irr310.common.world.World;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;

public class UpgradeFactory {

    public static void initUpgrades() {
        World world = Game.getInstance().getWorld();
        
        
        
        {
            Upgrade weaponDamageUpgrade = new Upgrade();
            weaponDamageUpgrade.setCategory(UpgradeCategory.WEAPON);
            weaponDamageUpgrade.setGlobalDescription("");
            weaponDamageUpgrade.setTag("weapon.damage");
            weaponDamageUpgrade.setName("Weapon damage");
            weaponDamageUpgrade.addRank(500, "20% damage increase");
            weaponDamageUpgrade.addRank(2000, "40% damage increase");
            weaponDamageUpgrade.addRank(8000, "80% damage increase");
            weaponDamageUpgrade.addRank(16000, "160% damage increase");
            weaponDamageUpgrade.addRank(64000, "320% damage increase");
            world.addUpgrade(weaponDamageUpgrade);
        }
        
        {
            Upgrade weaponCoolingUpgrade = new Upgrade();
            weaponCoolingUpgrade.setCategory(UpgradeCategory.WEAPON);
            weaponCoolingUpgrade.setGlobalDescription("");
            weaponCoolingUpgrade.setTag("weapon.cooling");
            weaponCoolingUpgrade.setName("Weapon cooling");
            weaponCoolingUpgrade.addRank(500, "20% cooling increase");
            weaponCoolingUpgrade.addRank(2000, "40% cooling increase");
            weaponCoolingUpgrade.addRank(8000, "80% cooling increase");
            weaponCoolingUpgrade.addRank(16000, "160% cooling increase");
            weaponCoolingUpgrade.addRank(64000, "320% cooling increase");
            world.addUpgrade(weaponCoolingUpgrade);
        }
        
        {
            Upgrade weaponFirerateUpgrade = new Upgrade();
            weaponFirerateUpgrade.setCategory(UpgradeCategory.WEAPON);
            weaponFirerateUpgrade.setGlobalDescription("Increase the firerate of all your weapons");
            weaponFirerateUpgrade.setTag("weapon.firerate");
            weaponFirerateUpgrade.setName("Weapon firerate");
            weaponFirerateUpgrade.addRank(500, "20% firerate increase");
            weaponFirerateUpgrade.addRank(2000, "40% firerate increase");
            weaponFirerateUpgrade.addRank(8000, "80% firerate increase");
            weaponFirerateUpgrade.addRank(16000, "160% firerate increase");
            weaponFirerateUpgrade.addRank(64000, "320% firerate increase");
            world.addUpgrade(weaponFirerateUpgrade);
        }
        
    }
}
