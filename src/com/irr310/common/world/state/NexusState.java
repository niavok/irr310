package com.irr310.common.world.state;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.tools.Vec3;

@NetworkClass
public class NexusState {

    @NetworkField
    public long id;
    
    @NetworkField
    public Vec3 location;
    
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
        NexusState other = (NexusState) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    
}
