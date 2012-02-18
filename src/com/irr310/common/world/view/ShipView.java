package com.irr310.common.world.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkOptionalField;

@NetworkClass
public class ShipView {

    @NetworkField
    public long id;

    @NetworkListField(LinkView.class)
    public List<LinkView> links = new ArrayList<LinkView>();

    @NetworkListField(ComponentView.class)
    public List<ComponentView> components = new ArrayList<ComponentView>();

    //@NetworkOptionalField
    //public KernelCapacity kernel;

    @NetworkOptionalField
    public PlayerView owner;

}
