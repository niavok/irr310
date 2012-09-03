package com.irr310.common.world.zone;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.zone.Part.CollisionShape;
import com.irr310.server.GameServer;

public class Loot extends CelestialObject {

    int value;
    
    public Loot(long id, String name) {
        super(id, name);
        setPhysicalResistance(100);
        setHeatResistance(100);
        setDurabilityMax(250);
        setDurability(250);
        setSkin("loot");
        
        Part part = new Part(GameServer.pickNewId(), this);
        part.setMass(170.);
        part.setLinearDamping(0.1);
        part.setAngularDamping(0.1);
        part.setCollisionShape(CollisionShape.SPHERE);
        this.addPart(part);
        
        
        part.setShape(new Vec3(1, 1, 1));
        
        value = 10;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }

}
