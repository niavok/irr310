package com.irr310.client.graphics.fragments;

import com.irr310.client.graphics.UiEngine;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;


public class UpgradeShipUpgradeTab extends ClassicalUpgradeTab{

    
    public UpgradeShipUpgradeTab(UiEngine engine) {
        super(engine, "Ship upgrades", UpgradeCategory.SHIP_UPGRADE);
    }
}
