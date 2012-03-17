package com.irr310.client.script.js.objects;

import com.irr310.common.tools.Log;

public class GunCapacity extends Capacity {

    private final com.irr310.common.world.capacity.GunCapacity capacity;

    public GunCapacity(com.irr310.common.world.capacity.GunCapacity capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    public void fire(boolean fire) {
        if (capacity.fire != fire) {
            capacity.fire = fire;
            sendUpdate();
        }
    }
   
}
