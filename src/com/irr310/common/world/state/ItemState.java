package com.irr310.common.world.state;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;

@NetworkClass
public class ItemState {

    @NetworkField
    public long id;
   
    @NetworkField
    public String name;
    
    @NetworkField
    public boolean used;
}
