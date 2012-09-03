package com.irr310.client.graphics.fragments;

import com.irr310.client.graphics.UiEngine;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;


public class UpgradeWeaponUpgradeTab extends ClassicalUpgradeTab{

    
    public UpgradeWeaponUpgradeTab(UiEngine engine) {
        super(engine, "Weapon upgrades", UpgradeCategory.WEAPON_UPGRADE);
    }
}
