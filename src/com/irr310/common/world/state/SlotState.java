package com.irr310.common.world.state;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.tools.Vec3;

@NetworkClass
public class SlotState {

    @NetworkField
    public long id;
    
    @NetworkField
    public Vec3 position;
    
    @NetworkField
    public long componentId;
    
    @NetworkField
    public long partId;
    
}
