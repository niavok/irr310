package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.WorldMapState;

public class WorldMapStateEvent extends WorldEvent {

    
    final private WorldMapState map;

    public WorldMapStateEvent(WorldMapState map) {
        this.map = map;
    }
    
    public WorldMapState getWorldMap() {
        return map;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
