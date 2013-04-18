package com.irr310.common.world.item;

import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.server.GameServer;


public class BuildingItemFactory {

    private final World world;

    public BuildingItemFactory(World world) {
        this.world = world;
    }
    
//    public NexusItem createNexus(Faction owner) {
//        return new NexusItem(world, GameServer.pickNewId(), owner);
//    }

}
