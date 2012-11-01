package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.RocketCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class RocketController extends CapacityController {

    private final RocketCapacity capacity;
    private final Component component;
    private double thrustTime;
    
    public RocketController(Component component, RocketCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
        thrustTime = 0;
    }

    @Override
    public void update(double duration) {
        
        if(thrustTime > 0.1) {
           capacity.collisionSecurity = false;
        }
        
        if(thrustTime > capacity.thrustDuration) {
            capacity.currentThrust = 0; 
        } else {
            capacity.currentThrust = capacity.theoricalMaxThrust;
        }
        thrustTime += duration;
    }

    @Override
    public Component getComponent() {
        return component;
    }

}
