package com.irr310.common.world.state;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;

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

}
