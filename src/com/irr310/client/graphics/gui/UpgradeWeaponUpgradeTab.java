package com.irr310.client.graphics.gui;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;


public class UpgradeWeaponUpgradeTab extends ClassicalUpgradeTab{

    
    public UpgradeWeaponUpgradeTab(GraphicEngine engine) {
        super(engine, "Weapon upgrades", UpgradeCategory.WEAPON_UPGRADE);
    }
}
