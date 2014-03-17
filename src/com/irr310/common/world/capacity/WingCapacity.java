package com.irr310.common.world.capacity;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.WorldSystem;

public class WingCapacity extends Capacity {

    public Vec3 breakAxis;
    public Vec3 thrustAxis;
    public double yield;
    public double friction;

    public WingCapacity(WorldSystem worldSystem, long id) {
        super(worldSystem, id);
        this.breakAxis = new Vec3(0, 0, 1);
        this.thrustAxis = new Vec3(0, 1, 0);
        yield = 0.2;
        friction = 1.5;
    }

    public Vec3 getBreakAxis() {
        return breakAxis;
    }
    
    public double getFriction() {
        return friction;
    }
    
    public Vec3 getThrustAxis() {
        return thrustAxis;
    }
    
    public double getYield() {
        return yield;
    }
}

