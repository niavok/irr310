package com.irr310.common.event;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.DamageDescriptor;
import com.irr310.common.world.system.Part;
import com.irr310.common.world.system.RocketDescriptor;



public class RocketFiredEvent extends EngineEvent {

    private final RocketDescriptor rocket;
    private final Part source;
    private final TransformMatrix from;
    private final Vec3 initialSpeed;
    

    public RocketFiredEvent(Part source, RocketDescriptor rocket, TransformMatrix from, Vec3 initialSpeed) {
        this.source = source;
        this.rocket = rocket;
        this.from = from;
        this.initialSpeed = initialSpeed;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public RocketDescriptor getRocket() {
        return rocket;
    }

    public Part getSource() {
        return source;
    }

    public TransformMatrix getFrom() {
        return from;
    }

    public Vec3 getInitialSpeed() {
        return initialSpeed;
    }
    
   
}
