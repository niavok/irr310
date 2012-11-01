package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.system.Component;

public class GunController extends BalisticWeaponController{

    public GunController(Component component, BalisticWeaponCapacity capacity) {
        super(component, capacity);
    }

    @Override
    protected void shot(int barrel) {
        genericShot(barrel);
    }

}
