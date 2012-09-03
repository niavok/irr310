package com.irr310.common.world.capacity;

import com.irr310.common.world.view.CapacityView;
import com.irr310.common.world.zone.Ship;

public class ContactDetectorCapacity extends Capacity {

    public double minImpulse;
    public Capacity triggerTarget;
    public String triggerCode;
    public double minTime;
    public Ship sourceShip;
    

    public ContactDetectorCapacity(long id) {
        super(id);
        minImpulse = 0;
        minTime = 0;
        triggerTarget = null;
        triggerCode = null;
        sourceShip = null;
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
