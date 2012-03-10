package com.irr310.common.world.upgrade;

public class UpgradeOwnership {
    
    private int rank;
    private final Upgrade upgrade;
    
    public UpgradeOwnership(Upgrade upgrade) {
        this.upgrade = upgrade;
        rank = 0;
    }
    
    public int getRank() {
        return rank;
    }
    public Upgrade getUpgrade() {
        return upgrade;
    }
    
    
    public void setRank(int rank) {
        this.rank = rank;
    }
}
