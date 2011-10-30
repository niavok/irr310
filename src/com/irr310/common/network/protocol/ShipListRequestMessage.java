package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;

public class ShipListRequestMessage extends NetworkMessage {

    public ShipListRequestMessage() {
        super(NetworkMessageType.SHIP_LIST_REQUEST);
    }
    
}
