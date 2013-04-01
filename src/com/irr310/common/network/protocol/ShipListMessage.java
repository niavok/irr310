package com.irr310.common.network.protocol;

import java.util.List;

import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.state.ShipState;

public class ShipListMessage extends NetworkMessage {

    @NetworkListField(ShipState.class)
    public List<ShipState> shipsList;

    public ShipListMessage() {
        super(NetworkMessageType.SHIP_LIST);
    }
    
    public ShipListMessage(long responseIndex,  List<ShipState> shipsList) {
        super(NetworkMessageType.SHIP_LIST);
        setResponseIndex(responseIndex);
        this.shipsList = shipsList;
    }

}
