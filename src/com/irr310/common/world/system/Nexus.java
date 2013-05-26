package com.irr310.common.world.system;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.NexusState;

public class Nexus extends WorldEntity {

    private double radius;
    private Vec3 location;
    private Faction faction;

    public Nexus(World world, long id) {
        super(world, id);
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
        this.faction = faction;
    }

    public NexusState toState() {
        NexusState state = new NexusState();
        state.id = getId();
        state.location = location;
        state.radius = radius;
        return state;
    }
    
    public boolean isState(NexusState nexusState) {
        return getId() == nexusState.id;
    }

    public Vec3 getLocation() {
        return location;
    }

}
