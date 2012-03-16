package com.irr310.common.world.item;

import com.irr310.common.tools.Vec3;

public class ItemSlot {

    private final Vec3 position;
    private Item content;
    
    public ItemSlot(Vec3 position) {
        this.position = position;
        this.content = null;
    }
    
    
    public Vec3 getPosition() {
        return position;
    }
    
    public void setContent(Item content) {
        this.content = content;
    }
    
    public Item getContent() {
        return content;
    }

    public boolean isEmpty() {
        return content == null;
    }

}
