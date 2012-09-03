package com.irr310.common.world.zone;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.zone.Part.CollisionShape;
import com.irr310.server.GameServer;

public class Monolith extends CelestialObject {

    public Monolith(long id, String name) {
        super(id, name);
        setPhysicalResistance(0.5);
        setDurabilityMax(1000);
        setDurability(1000);
        setSkin("monolith");
        
        Part part = new Part(GameServer.pickNewId(), this);
        part.setCollisionShape(CollisionShape.SPHERE);
        this.addPart(part);
        
        part.setShape(new Vec3(10, 10, 10));
        
    }

}
