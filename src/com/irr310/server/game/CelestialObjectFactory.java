package com.irr310.server.game;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Asteroid;
import com.irr310.common.world.system.Loot;
import com.irr310.server.GameServer;

public class CelestialObjectFactory {

    public static Asteroid createAsteroid(double size) {
        Asteroid asteroid = new Asteroid(GameServer.pickNewId(), "asteroid");
        
        asteroid.getFirstPart().setShape(new Vec3(size, size, size));
        asteroid.getFirstPart().setMass(size*20);
        
        asteroid.setDurabilityMax(5*size);
        asteroid.setDurability(5*size);
        
        return asteroid;
    }
    
    public static Loot createLoot(int value) {
        Loot loot = new Loot(GameServer.pickNewId(), ""+value+" $");
        loot.setValue(value);
        loot.getFirstPart().setShape(new Vec3(1+value/50., 1+value/50., 1+value/50.));
        loot.getFirstPart().setMass(value*20.);
        
        return loot;
    }
    
}
