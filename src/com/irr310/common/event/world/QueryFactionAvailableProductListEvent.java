package com.irr310.common.event.world;

import com.irr310.common.world.view.FactionView;

public class QueryFactionAvailableProductListEvent extends WorldEvent {

    
    final private FactionView faction;

    public QueryFactionAvailableProductListEvent(FactionView faction) {
        this.faction = faction;
    }
    
    public FactionView getFaction() {
        return faction;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
