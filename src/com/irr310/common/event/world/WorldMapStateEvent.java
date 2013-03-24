package com.irr310.common.event.world;

import com.irr310.common.world.view.FactionView;
import com.irr310.common.world.view.WorldMapView;

public class WorldMapStateEvent extends WorldEvent {

    
    final private WorldMapView map;

    public WorldMapStateEvent(WorldMapView map) {
        this.map = map;
    }
    
    public WorldMapView getWorldMap() {
        return map;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
