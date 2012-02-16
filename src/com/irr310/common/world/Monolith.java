package com.irr310.common.world;

import com.irr310.common.Game;
import com.irr310.common.tools.Vect3;
import com.irr310.server.GameServer;

public class Monolith extends CelestialObject {

    public Monolith(long id, String name) {
        super(id, name);
        
        Part part = new Part(GameServer.pickNewId());
        this.addPart(part);
        Game.getInstance().getWorld().addPart(part);
        
        part.setShape(new Vect3(10, 10, 10));
        
    }

}
