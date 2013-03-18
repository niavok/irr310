package com.irr310.common.world.capacity;

import com.irr310.common.world.World;
import com.irr310.common.world.view.CapacityView;

public class RocketCapacity extends Capacity {

    public boolean  collisionSecurity;
    public double currentThrust;
    public double theoricalMaxThrust;

    public ExplosiveCapacity explosive;
    public double thrustDuration;
    public double stability;

    public RocketCapacity(World world, long id) {
        super(world, id);
        currentThrust = 0;
        theoricalMaxThrust = 0;
        stability = 0.5;
        collisionSecurity = true;
    }

    public double getCurrentThrust() {

        return currentThrust;
    }

    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.LINEAR_ENGINE.ordinal();

        //TODO
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        setName(view.name);
        //TODO
    }

    

}
