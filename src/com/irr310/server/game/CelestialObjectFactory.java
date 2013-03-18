package com.irr310.server.game;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.system.Asteroid;
import com.irr310.common.world.system.Loot;
import com.irr310.server.GameServer;

public class CelestialObjectFactory {

    private final World world;

    public CelestialObjectFactory(World world) {
        this.world = world;
    }
    
    
    public Asteroid createAsteroid(double size) {
        Asteroid asteroid = new Asteroid(world, GameServer.pickNewId(), "asteroid");
        
        asteroid.getFirstPart().setShape(new Vec3(size, size, size));
        asteroid.getFirstPart().setMass(size*20);
        
        asteroid.setDurabilityMax(5*size);
        asteroid.setDurability(5*size);
        
        return asteroid;
    }
    
    public Loot createLoot(int value) {
        Loot loot = new Loot(world, GameServer.pickNewId(), ""+value+" $");
        loot.setValue(value);
        loot.getFirstPart().setShape(new Vec3(1+value/50., 1+value/50., 1+value/50.));
        loot.getFirstPart().setMass(value*20.);
        
        return loot;
    }
    
}
