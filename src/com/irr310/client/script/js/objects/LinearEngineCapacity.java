package com.irr310.client.script.js.objects;

public class LinearEngineCapacity extends Capacity {

    private final com.irr310.common.world.capacity.LinearEngineCapacity capacity;

    public LinearEngineCapacity(com.irr310.common.world.capacity.LinearEngineCapacity capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    public void setTargetThrust(double thrust) {
        if (capacity.getTargetThrust() != thrust) {
            capacity.setTargetThrust(thrust);
            sendUpdate();
        }
    }

    public double getMaxThrust() {
        return capacity.getMaxThrust();
    }

    public double getMinThrust() {
        return capacity.getMinThrust();
    }

}
