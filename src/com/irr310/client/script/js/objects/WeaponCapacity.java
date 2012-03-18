package com.irr310.client.script.js.objects;


public class WeaponCapacity extends Capacity {

    private final com.irr310.common.world.capacity.WeaponCapacity capacity;

    public WeaponCapacity(com.irr310.common.world.capacity.WeaponCapacity capacity) {
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
