package com.irr310.common.event;

import com.irr310.common.world.Player;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class UpgradeStateChanged extends EngineEvent {

    private final UpgradeOwnership playerUpgrade;
    private final Player player;

    public UpgradeStateChanged(UpgradeOwnership playerUpgrade, Player player) {
        this.playerUpgrade = playerUpgrade;
        this.player = player;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    
    public Player getPlayer() {
        return player;
    }
    
    public UpgradeOwnership getPlayerUpgrade() {
        return playerUpgrade;
    }
}
