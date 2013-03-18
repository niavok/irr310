package com.irr310.common.world.capacity;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.view.CapacityView;

public class WingCapacity extends Capacity {

    public Vec3 breakAxis;
    public Vec3 thrustAxis;
    public double yield;
    public double friction;

    public WingCapacity(World world, long id) {
        super(world, id);
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
    
    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.WING.ordinal();

        view.pushDouble(yield);
        view.pushDouble(friction);
        view.pushVect3(breakAxis);
        view.pushVect3(thrustAxis);
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        setName(view.name);
        yield = view.popDouble();
        friction = view.popDouble();
        breakAxis = view.popVect3();
        thrustAxis = view.popVect3();
    }

    

}

