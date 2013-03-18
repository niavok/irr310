package com.irr310.server.game;

import com.irr310.common.world.World;
import com.irr310.common.world.system.Nexus;
import com.irr310.server.GameServer;

public class BuildingFactory {

    private final World world;

    public BuildingFactory(World world) {
        this.world = world;
    }
    
    public Nexus createNexus() {
        Nexus nexus = new Nexus(world, GameServer.pickNewId(), "nexus");
        
        return nexus;
    }
    
}
