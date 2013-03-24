package com.irr310.common.event.world;


public class QueryWorldMapStateEvent extends WorldEvent {

    public QueryWorldMapStateEvent() {
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
