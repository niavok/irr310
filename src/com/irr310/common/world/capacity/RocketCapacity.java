package com.irr310.common.world.capacity;

import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.WorldSystem;

public class RocketCapacity extends Capacity {

    public boolean  collisionSecurity;
    public double currentThrust;
    public double theoricalMaxThrust;

    public ExplosiveCapacity explosive;
    public double thrustDuration;
    public double stability;

    public RocketCapacity(WorldSystem worldSystem, long id, Component component) {
        super(worldSystem, id, component);
        currentThrust = 0;
        theoricalMaxThrust = 0;
        stability = 0.5;
        collisionSecurity = true;
    }

    public double getCurrentThrust() {

        return currentThrust;
    }
}
