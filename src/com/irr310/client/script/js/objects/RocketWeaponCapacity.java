package com.irr310.client.script.js.objects;



public class RocketWeaponCapacity extends Capacity {

    private final com.irr310.common.world.capacity.RocketWeaponCapacity capacity;

    public RocketWeaponCapacity(com.irr310.common.world.capacity.RocketWeaponCapacity capacity) {
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
