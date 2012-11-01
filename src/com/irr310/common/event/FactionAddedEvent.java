package com.irr310.common.event;

import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;

public class FactionAddedEvent extends EngineEvent {

    
    final private Faction faction;

    public FactionAddedEvent(Faction faction) {
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }
    
    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

}
