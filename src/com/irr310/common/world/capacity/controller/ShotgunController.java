package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.WeaponCapacity;

public class ShotgunController extends WeaponController{

    public ShotgunController(Component component, WeaponCapacity capacity) {
        super(component, capacity);
    }

    @Override
    protected void shot(int barrel) {
        for(int i = 0; i < 50; i++) {
            genericShot(barrel);
        }
    }

}
