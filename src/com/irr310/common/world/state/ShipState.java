package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkOptionalField;

@NetworkClass
public class ShipState {

    @NetworkField
    public long id;

    @NetworkListField(LinkState.class)
    public List<LinkState> links = new ArrayList<LinkState>();

    @NetworkListField(ComponentState.class)
    public List<ComponentState> components = new ArrayList<ComponentState>();

    //@NetworkOptionalField
    //public KernelCapacity kernel;

    @NetworkOptionalField
    public FactionState owner;

}
