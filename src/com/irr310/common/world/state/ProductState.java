package com.irr310.common.world.state;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;

@NetworkClass
public class ProductState {

    public final static int TYPE_COMPONENT = 0;
    public final static int TYPE_SHIP = 1;
    
    @NetworkField
    public String id;
    
    @NetworkField
    public String name;
    
    @NetworkField
    public String code;
    
    @NetworkField
    public String description;
    
    @NetworkField
    public int type;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        ProductState other = (ProductState) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    
}
