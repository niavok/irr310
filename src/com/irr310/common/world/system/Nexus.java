package com.irr310.common.world.system;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.system.Part.CollisionShape;
import com.irr310.server.GameServer;

public class Nexus extends CelestialObject {

    public Nexus(World world, long id, String name) {
        super(world, id, name);
        setPhysicalResistance(0.5);
        setDurabilityMax(1000);
        setDurability(1000);
        setSkin("nexus");
        
        Part part = new Part(GameServer.pickNewId(), this);
        part.setCollisionShape(CollisionShape.SPHERE);
        this.addPart(part);
        
        part.setShape(new Vec3(10, 10, 10));
        
    }

}
