package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.state.CapacityState;

public class CapacityUpdateMessage extends NetworkMessage {

    @NetworkField
    public CapacityState capacity;
    
    

    public CapacityUpdateMessage() {
        super(NetworkMessageType.CAPACITY_UPDATE);
    }
    
    public CapacityUpdateMessage(CapacityState capacity) {
        super(NetworkMessageType.CAPACITY_UPDATE);
        this.capacity = capacity;
    }

}
