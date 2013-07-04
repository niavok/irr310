package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.tools.Vec3;

@NetworkClass
public class ComponentState {

    @NetworkField
    public long id;
    
    @NetworkField
    public String name;
    
    @NetworkField
    public String skin;
    
    @NetworkListField(PartState.class)
    public List<PartState> parts = new ArrayList<PartState>();

    @NetworkListField(SlotState.class)
    public List<SlotState> slots = new ArrayList<SlotState>();
    
    @NetworkListField(CapacityState.class)
    public List<CapacityState> capacities = new ArrayList<CapacityState>();
    
    @NetworkField
    public Vec3 shipPosition;
    
    @NetworkField
    public Vec3 shipRotation;
    
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

    // Helpers
    public PartState getFirstPart() {
        return parts.get(0);
    }
    
}
