package com.irr310.server.upgrade;

import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class WeaponShotgunEffect extends UpgradeEffect {

    
    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
    }

    @Override
    public Upgrade generateUpgrade() {
        Upgrade weaponDamageUpgrade = new Upgrade();
        weaponDamageUpgrade.setCategory(UpgradeCategory.WEAPONS);
        weaponDamageUpgrade.setGlobalDescription("Buy a shotgun.");
        weaponDamageUpgrade.setTag("weapon.shotgun");
        weaponDamageUpgrade.setName("Shotgun");
        
        for(int i = 0; i  < 21 ; i++) {
            weaponDamageUpgrade.addRank((int) (100 * Math.pow(1.5, i)), ""+(i+1)+" shotgun.");
        }
        return weaponDamageUpgrade;
    }
}
