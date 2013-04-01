package com.irr310.common.network.protocol;

import java.util.List;

import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.state.CelestialObjectState;
import com.irr310.common.world.state.ShipState;

public class WorldObjectListMessage extends NetworkMessage {

    @NetworkListField(CelestialObjectState.class)
    public List<CelestialObjectState> celestialObjectList;
    
    @NetworkListField(ShipState.class)
    public List<ShipState> shipsList;

    public WorldObjectListMessage() {
        super(NetworkMessageType.WORLD_OBJECT_LIST);
    }
    
    public WorldObjectListMessage(long responseIndex,  List<CelestialObjectState> celestialObjectList, List<ShipState> shipsList) {
        super(NetworkMessageType.WORLD_OBJECT_LIST);
        setResponseIndex(responseIndex);
        this.celestialObjectList = celestialObjectList;
        this.shipsList = shipsList;
    }

}
