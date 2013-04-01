package com.irr310.common.world.capacity;

import com.irr310.common.world.World;
import com.irr310.common.world.state.CapacityState;
import com.irr310.common.world.system.Ship;

public class ContactDetectorCapacity extends Capacity {

    public double minImpulse;
    public Capacity triggerTarget;
    public String triggerCode;
    public double minTime;
    public Ship sourceShip;
    

    public ContactDetectorCapacity(World world, long id) {
        super(world, id);
        minImpulse = 0;
        minTime = 0;
        triggerTarget = null;
        triggerCode = null;
        sourceShip = null;
    }

    @Override
    public CapacityState toState() {
        CapacityState view = new CapacityState();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.LINEAR_ENGINE.ordinal();

        //TODO
        return view;
    }

    @Override
    public void fromState(CapacityState view) {
        setName(view.name);
        //TODO
    }

    

}
