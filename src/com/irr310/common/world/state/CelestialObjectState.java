package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class CelestialObjectState {

    @NetworkField
    public long id;
    
    @NetworkField
    public String name;
    
    @NetworkField
    public String skin;
    
    @NetworkListField(PartState.class)
    public List<PartState> parts = new ArrayList<PartState>();
    
    
    @NetworkField
    public double durabilityMax;
    
    @NetworkField
    public double durability;
    
    @NetworkField
    public double quality;

    @NetworkField
    public double physicalResistance;
    
    @NetworkField
    public double heatResistance;
    
}
