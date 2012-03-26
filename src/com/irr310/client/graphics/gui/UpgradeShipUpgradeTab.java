package com.irr310.client.graphics.gui;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;


public class UpgradeShipUpgradeTab extends ClassicalUpgradeTab{

    
    public UpgradeShipUpgradeTab(GraphicEngine engine) {
        super(engine, "Ship upgrades", UpgradeCategory.SHIP_UPGRADE);
    }
}
