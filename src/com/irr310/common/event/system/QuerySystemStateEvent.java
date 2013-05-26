package com.irr310.common.event.system;

import com.irr310.common.world.state.WorldSystemState;


public class QuerySystemStateEvent extends SystemEvent {

    private WorldSystemState system;
    private int depth;

    public QuerySystemStateEvent() {
    }
    
    public QuerySystemStateEvent(WorldSystemState system, int depth) {
        this.system = system;
        this.depth = depth;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public WorldSystemState getSystem() {
        return system;
    }

    @Override
    public void accept(SystemEventVisitor visitor) {
        visitor.visit(this);
    }
}
