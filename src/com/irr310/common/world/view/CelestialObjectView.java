package com.irr310.common.world.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class CelestialObjectView {

    @NetworkField
    public long id;
    
    @NetworkField
    public String name;
    
    @NetworkField
    public String skin;
    
    @NetworkListField(PartView.class)
    public List<PartView> parts = new ArrayList<PartView>();
    
}
