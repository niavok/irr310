package com.irr310.common.network.protocol;

import java.util.List;

import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;
import com.irr310.common.world.ShipView;

public class ShipListMessage extends NetworkMessage {

    @NetworkListField(ShipView.class)
    public List<ShipView> shipsList;

    public ShipListMessage() {
        super(NetworkMessageType.SHIP_LIST);
    }
    
    public ShipListMessage(long responseIndex,  List<ShipView> shipsList) {
        super(NetworkMessageType.SHIP_LIST);
        setResponseIndex(responseIndex);
        this.shipsList = shipsList;
    }

}
