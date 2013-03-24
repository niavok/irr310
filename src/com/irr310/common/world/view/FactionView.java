package com.irr310.common.world.view;

import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.i3d.Color;

@NetworkClass
public class FactionView {

    @NetworkField
    public long id;
    
    @NetworkField
    public long statersAmount;
    
    @NetworkField
    public long oresAmount;
    
    @NetworkField
    public long koliumAmount;
    
    @NetworkField
    public long neuridiumAmount;

    @NetworkListField(Long.class)
    public List<Long> knownSystemIds;

    @NetworkField
    public long homeSystemId;

    @NetworkField
    public Color color;

}
