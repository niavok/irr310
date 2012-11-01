package com.irr310.common.world.item;

import com.irr310.common.world.Faction;
import com.irr310.server.GameServer;


public class BuildingItemFactory {

    public static NexusItem createNexus(Faction owner) {
        return new NexusItem(GameServer.pickNewId(), owner);
    }

}
