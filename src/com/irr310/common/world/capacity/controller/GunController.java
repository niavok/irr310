package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.GunCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;

public class GunController extends CapacityController {

    private final GunCapacity capacity;
    private final Component component;

    public GunController(Component component, GunCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
    }

    @Override
    public void update(double duration) {
        if(capacity.fire) {
            System.err.println("Gun want fire !");          
            
            capacity.fire = false;
        }
    }

}
