package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class FactionProductionState {

    @NetworkField
    public long factionId;
    
    @NetworkField
    public long maintenanceAmount;

    @NetworkField
    public long factoryCapacity;

    @NetworkField
    public long incomingCapacity;

    @NetworkField
    public long nextFactoryCapacityIncreaseTicks;

    @NetworkField
    public long factoryRentCapacity;

    @NetworkField
    public long factoryTotalCapacity;

    @NetworkListField(ProductionTaskState.class)
    public List<ProductionTaskState> productionTaskQueue = new ArrayList<ProductionTaskState>();
}
