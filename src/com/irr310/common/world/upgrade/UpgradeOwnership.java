package com.irr310.common.world.upgrade;

public class UpgradeOwnership {
    
    private int rank;
    private final Upgrade upgrade;
    
    public UpgradeOwnership(Upgrade upgrade) {
        this.upgrade = upgrade;
    }
    
    public int getRank() {
        return rank;
    }
    public Upgrade getUpgrade() {
        return upgrade;
    }
    
}
