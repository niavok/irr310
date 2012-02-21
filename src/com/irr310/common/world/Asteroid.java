package com.irr310.common.world;

import com.irr310.common.Game;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Part.CollisionShape;
import com.irr310.server.GameServer;

public class Asteroid extends CelestialObject {

    public Asteroid(long id, String name) {
        super(id, name);
        setPhysicalResistance(0.0);
        setDurabilityMax(250);
        setDurability(250);
        setSkin("asteroid");
        
        Part part = new Part(GameServer.pickNewId(), this);
        part.setMass(170.);
        part.setLinearDamping(0.01);
        part.setAngularDamping(0.01);
        part.setCollisionShape(CollisionShape.SPHERE);
        this.addPart(part);
        Game.getInstance().getWorld().addPart(part);
        
        part.setShape(new Vect3(3, 3, 3));
        
    }

}
