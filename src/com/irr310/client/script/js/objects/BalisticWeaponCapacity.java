package com.irr310.client.script.js.objects;


public class BalisticWeaponCapacity extends Capacity {

    private final com.irr310.common.world.capacity.BalisticWeaponCapacity capacity;

    public BalisticWeaponCapacity(com.irr310.common.world.capacity.BalisticWeaponCapacity capacity) {
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
