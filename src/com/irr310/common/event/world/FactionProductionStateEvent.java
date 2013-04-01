package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionProductionState;

public class FactionProductionStateEvent extends WorldEvent {

    final private FactionProductionState factionProduction;

    public FactionProductionStateEvent(FactionProductionState factionProduction) {
        this.factionProduction = factionProduction;
    }

    public FactionProductionState getFactionProduction() {
        return factionProduction;
    }

    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
