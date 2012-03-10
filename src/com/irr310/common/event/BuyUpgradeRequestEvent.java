package com.irr310.common.event;

import com.irr310.common.world.Player;
import com.irr310.common.world.upgrade.Upgrade;


public class BuyUpgradeRequestEvent extends EngineEvent {


    private final Upgrade upgrade;
    private final Player player;

    public BuyUpgradeRequestEvent(Upgrade upgrade, Player player) {
        this.upgrade = upgrade;
        this.player = player;
    }
    
	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Upgrade getUpgrade() {
        return upgrade;
    }
	
	public Player getPlayer() {
        return player;
    }
}
