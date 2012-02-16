package com.irr310.common.network.protocol;

import java.util.List;

import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;
import com.irr310.common.world.view.CelestialObjectView;
import com.irr310.common.world.view.ShipView;

public class WorldObjectListMessage extends NetworkMessage {

    @NetworkListField(CelestialObjectView.class)
    public List<CelestialObjectView> celestialObjectList;
    
    @NetworkListField(ShipView.class)
    public List<ShipView> shipsList;

    public WorldObjectListMessage() {
        super(NetworkMessageType.WORLD_OBJECT_LIST);
    }
    
    public WorldObjectListMessage(long responseIndex,  List<CelestialObjectView> celestialObjectList, List<ShipView> shipsList) {
        super(NetworkMessageType.WORLD_OBJECT_LIST);
        setResponseIndex(responseIndex);
        this.celestialObjectList = celestialObjectList;
        this.shipsList = shipsList;
    }

}
