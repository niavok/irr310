package com.irr310.common.event;

import com.irr310.common.engine.CollisionDescriptor;


public class CollisionEvent extends EngineEvent {


    private final CollisionDescriptor collisionDescriptor;

    public CollisionEvent(CollisionDescriptor collisionDescriptor) {
        this.collisionDescriptor = collisionDescriptor;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }
    
    public CollisionDescriptor getCollisionDescriptor() {
        return collisionDescriptor;
    }
}
