package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.system.Component;

public class RocketPodController extends RocketWeaponController{

    public RocketPodController(Component component, RocketWeaponCapacity capacity) {
        super(component, capacity);
    }

    @Override
    protected void shot(int barrel) {
        genericShot(barrel);
    }

}
