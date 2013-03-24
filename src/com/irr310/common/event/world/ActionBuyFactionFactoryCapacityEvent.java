package com.irr310.common.event.world;

import com.irr310.common.world.view.FactionView;

public class ActionBuyFactionFactoryCapacityEvent extends WorldEvent {

    
    final private FactionView faction;
    private final long count;

    public ActionBuyFactionFactoryCapacityEvent(FactionView faction, long count) {
        this.faction = faction;
        this.count = count;
    }
    
    public FactionView getFaction() {
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
