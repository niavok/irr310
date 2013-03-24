package com.irr310.common.event.world;

import com.irr310.common.world.view.FactionProductionView;
import com.irr310.common.world.view.FactionView;

public class FactionProductionStateEvent extends WorldEvent {

    
    final private FactionProductionView factionProduction;

    public FactionProductionStateEvent(FactionProductionView factionProduction) {
        this.factionProduction = factionProduction;
    }
    
    public FactionProductionView getFactionProduction() {
        return factionProduction;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
