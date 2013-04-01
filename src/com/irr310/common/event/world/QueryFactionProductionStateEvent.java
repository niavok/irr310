package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionState;

public class QueryFactionProductionStateEvent extends WorldEvent {

    
    final private FactionState faction;

    public QueryFactionProductionStateEvent(FactionState faction) {
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
