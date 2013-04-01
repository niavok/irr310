package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionState;

public class ActionSellFactionFactoryCapacityEvent extends WorldEvent {

    
    final private FactionState faction;
    private final long count;

    public ActionSellFactionFactoryCapacityEvent(FactionState faction, long count) {
        this.faction = faction;
        this.count = count;
    }
    
    public FactionState getFaction() {
        return faction;
    }
    
    public long getCount() {
        return count;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
