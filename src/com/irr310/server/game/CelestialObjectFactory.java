package com.irr310.server.game;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Asteroid;
import com.irr310.common.world.CelestialObject;
import com.irr310.server.GameServer;

public class CelestialObjectFactory {

    public static Asteroid createAsteroid(double size) {
        Asteroid asteroid = new Asteroid(GameServer.pickNewId(), "asteroid");
        
        asteroid.getFirstPart().setShape(new Vect3(size, size, size));
        asteroid.getFirstPart().setMass(size*20);
        
        asteroid.setDurabilityMax(30*size);
        asteroid.setDurability(30*size);
        
        return asteroid;
    }
    
    
}
