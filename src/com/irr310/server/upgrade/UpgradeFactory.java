package com.irr310.server.upgrade;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.world.Player;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class UpgradeFactory {

    private static Map<String, UpgradeEffect> effectMap = new HashMap<String, UpgradeEffect>();
    
    public static void initUpgrades() {
        
        addUpgrade(new WeaponDamageUpgradeEffect());
        addUpgrade(new WeaponFirerateUpgradeEffect());
        addUpgrade(new WeaponCoolingUpgradeEffect());
        
        addUpgrade(new WeaponGunEffect());
        addUpgrade(new WeaponShotgunEffect());
        addUpgrade(new WeaponCannonEffect());
        addUpgrade(new WeaponLaserEffect());
        
    }

    private static void addUpgrade(UpgradeEffect upgradeEffect) {
            Upgrade upgrade = upgradeEffect.generateUpgrade();
            Game.getInstance().getWorld().addUpgrade(upgrade);
            effectMap.put(upgrade.getTag(), upgradeEffect);
        
    }

    public static void apply(UpgradeOwnership playerUpgrade) {
        UpgradeEffect upgradeEffect = effectMap.get(playerUpgrade.getUpgrade().getTag());
        upgradeEffect.apply(playerUpgrade);
        
    }

    public static void refresh(Player player) {
        for(Upgrade upgrade : Game.getInstance().getWorld().getAvailableUpgrades()) {
            apply(player.getUpgradeState(upgrade));
        }
        
    }
    public static void refresh() {
        for(Player player: Game.getInstance().getWorld().getPlayers()) {
            refresh(player);
        }
    }
}
