package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;

public class ShotgunController extends BalisticWeaponController{

    public ShotgunController(Component component, BalisticWeaponCapacity capacity) {
        super(component, capacity);
    }

    @Override
    protected void shot(int barrel) {
        for(int i = 0; i < 50; i++) {
            genericShot(barrel);
        }
    }

}
