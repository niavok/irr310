package com.irr310.common.world.upgrade;

import com.irr310.common.world.Player;

public class UpgradeOwnership {
    
    private int rank;
    private final Upgrade upgrade;
    private final Player player;
    
    public UpgradeOwnership(Upgrade upgrade, Player player) {
        this.upgrade = upgrade;
        this.player = player;
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

    public Player getPlayer() {
        return player;
    }
}
