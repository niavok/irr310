package com.irr310.common.event.system;

import com.irr310.common.world.state.FactionState;

public class QuerySystemStateEvent extends SystemEvent {

    public QuerySystemStateEvent() {
    }
    
    @Override
    public void accept(SystemEventVisitor visitor) {
        visitor.visit(this);
    }
}
