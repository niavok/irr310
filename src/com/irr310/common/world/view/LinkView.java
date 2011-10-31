package com.irr310.common.world.view;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;

@NetworkClass
public class LinkView {

    @NetworkField
    public long slot1Id;
    
    @NetworkField
    public long slot2Id;
    
}
