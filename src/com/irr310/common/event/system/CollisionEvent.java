package com.irr310.common.event.system;

import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.EngineEventVisitor;


public class CollisionEvent extends SystemEvent {


    private final CollisionDescriptor collisionDescriptor;

    public CollisionEvent(CollisionDescriptor collisionDescriptor) {
        this.collisionDescriptor = collisionDescriptor;
    }

    @Override
    public void accept(SystemEventVisitor visitor) {
        visitor.visit(this);
    }
    
    public CollisionDescriptor getCollisionDescriptor() {
        return collisionDescriptor;
    }
}
