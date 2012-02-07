package com.irr310.client.script.js.objects;

import com.irr310.client.GameClient;

public class Capacity {

    private final com.irr310.common.world.capacity.Capacity capacity;
    
    public Capacity(com.irr310.common.world.capacity.Capacity capacity) {
        this.capacity = capacity;
    }
    
    protected void sendUpdate() {
        GameClient.getInstance().updateCapacityTask(capacity);
    }
}
