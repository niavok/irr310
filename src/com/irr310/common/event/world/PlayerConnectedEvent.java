package com.irr310.common.event.world;

import com.irr310.common.world.Player;

public class PlayerConnectedEvent extends WorldEvent {

    
    final private Player player;

    public PlayerConnectedEvent(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
