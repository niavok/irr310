package com.irr310.common.event;

import com.irr310.common.world.Player;
import com.irr310.common.world.WorldObject;

public class PlayerLoggedEvent extends EngineEvent {

    
    final private Player player;

    public PlayerLoggedEvent(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

}
