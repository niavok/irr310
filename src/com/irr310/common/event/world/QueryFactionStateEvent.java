package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionState;

public class QueryFactionStateEvent extends WorldEvent {

    
    final private FactionState faction;

    public QueryFactionStateEvent(FactionState faction) {
        this.faction = faction;
    }
    
    public FactionState getFaction() {
        return faction;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
