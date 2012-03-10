package com.irr310.server.upgrade;

import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public abstract class UpgradeEffect {

    public abstract void apply(UpgradeOwnership playerUpgrade);

    
    public abstract Upgrade generateUpgrade();
}
