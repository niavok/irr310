package com.irr310.common.world.zone;

import com.irr310.common.tools.Vec2;

public class Zone {

    private  Vec2 location;

    public Zone(Vec2 location) {
        this.location = location;
    }
    
    public Vec2 getLocation() {
        return location;
    }

}
