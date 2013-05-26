package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.tools.Vec2;
import com.irr310.i3d.Color;

@NetworkClass
public class WorldSystemState {

    @NetworkField
    public long id;
    
    @NetworkField
    public Vec2 location;

    @NetworkField
    public String name;

    @NetworkField
    public Long ownerId;

    @NetworkField
    public boolean homeSystem;

    @NetworkField
    public Color ownerColor;

    @NetworkListField(NexusState.class)
    public List<NexusState> nexuses = new ArrayList<NexusState>();
    
    @NetworkListField(NexusState.class)
    public List<ShipState> ships = new ArrayList<ShipState>();

    @NetworkField
    public double radius;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorldSystemState other = (WorldSystemState) obj;
        if (id != other.id)
            return false;
        return true;
    }

    
    
}
