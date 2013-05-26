package com.irr310.common.event.world;


public class QueryWorldMapStateEvent extends WorldEvent {

    private int depth;

    public QueryWorldMapStateEvent(int depth) {
        this.depth = depth;
    }
    
    public int getDepth() {
        return depth;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
