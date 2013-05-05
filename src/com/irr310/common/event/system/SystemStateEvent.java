package com.irr310.common.event.system;

import com.irr310.common.world.state.WorldSystemState;

public class SystemStateEvent extends SystemEvent {

    private WorldSystemState systemState;

    public SystemStateEvent(WorldSystemState systemState) {
        this.systemState = systemState;
    }
    
    public WorldSystemState getSystemState() {
        return systemState;
    }
    
    @Override
    public void accept(SystemEventVisitor visitor) {
        visitor.visit(this);
    }
}
