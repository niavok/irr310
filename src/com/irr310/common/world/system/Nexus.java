package com.irr310.common.world.system;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;

public class Nexus extends WorldSystemEntity {

    private double radius;
    private Vec3 location;
    private Faction mOwner;

    public Nexus(WorldSystem worldSystem, long id) {
        super(worldSystem, id);
    }

    public void setRadius(double radius) {
        this.radius = radius;
        
    }
    
    public double getRadius() {
        return radius;
    }

    public void setLocation(Vec3 location) {
        this.location = location;
    }

    public void setOwner(Faction faction) {
        this.mOwner = faction;
    }

    public Vec3 getLocation() {
        return location;
    }

    public Faction getOwner() {
        return mOwner;
    }
}
