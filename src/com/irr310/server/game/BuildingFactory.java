package com.irr310.server.game;

import com.irr310.common.world.system.Nexus;
import com.irr310.server.GameServer;

public class BuildingFactory {

    public static Nexus createNexus() {
        Nexus nexus = new Nexus(GameServer.pickNewId(), "nexus");
        
        return nexus;
    }
    
}
