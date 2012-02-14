package com.irr310.common.world.capacity;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.view.CapacityView;

public class WingCapacity extends Capacity {

    public Vect3 breakAxis;
    public Vect3 thrustAxis;
    public double yield;
    public double friction;

    public WingCapacity(long id) {
        super(id, "wing");
        this.breakAxis = new Vect3(0, 0, 1);
        this.thrustAxis = new Vect3(0, 1, 0);
        yield = 0.5;
        friction = 1.0;
    }

    public Vect3 getBreakAxis() {
        return breakAxis;
    }
    
    public double getFriction() {
        return friction;
    }
    
    public Vect3 getThrustAxis() {
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
        yield = view.popDouble();
        friction = view.popDouble();
        breakAxis = view.popVect3();
        thrustAxis = view.popVect3();
    }

    

}

