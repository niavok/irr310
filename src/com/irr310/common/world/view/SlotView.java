package com.irr310.common.world.view;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.tools.Vect3;

@NetworkClass
public class SlotView {

    @NetworkField
    public long id;
    
    @NetworkField
    public Vect3 position;
    
    @NetworkField
    public long componentId;
    
    @NetworkField
    public long partId;
    
}
