package com.irr310.common.world.item;

import com.irr310.common.tools.Vec3;

public class ItemSlot {

    private final Vec3 position;
    
    public ItemSlot(Vec3 position) {
        this.position = position;
    }
    
    
    public Vec3 getPosition() {
        return position;
    }

}
