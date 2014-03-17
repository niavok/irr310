package com.irr310.common.world.capacity;

import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;

public class ContactDetectorCapacity extends Capacity {

    public double minImpulse;
    public Capacity triggerTarget;
    public String triggerCode;
    public double minTime;
    public Ship sourceShip;
    

    public ContactDetectorCapacity(WorldSystem worldSystem, long id) {
        super(worldSystem, id);
        minImpulse = 0;
        minTime = 0;
        triggerTarget = null;
        triggerCode = null;
        sourceShip = null;
    }
}
