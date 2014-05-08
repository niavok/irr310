package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.system.Component;

/**
 * A controller must be state less because only the capacity data is saved
 */
public abstract class CapacityController {

    public abstract void update(double duration);
    
    public abstract Component getComponent();

}
