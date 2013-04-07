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
}
