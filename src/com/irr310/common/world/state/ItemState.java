package com.irr310.common.world.state;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;

@NetworkClass
public class ItemState {

    static public final int STOCKED = 0;
    static public final int RESERVED = 1;
    static public final int DEPLOYED = 2;
    static public final int DEPLOYING = 3;
    static public final int DESTROYED = 4;
    
    @NetworkField
    public long id;
 
    @NetworkField
    public ProductState product;
    
    @NetworkField
    public int state;

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
        ItemState other = (ItemState) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    
}
