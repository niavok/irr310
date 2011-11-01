package com.irr310.common;

import com.irr310.common.event.EngineEvent;
import com.irr310.common.world.World;

public abstract class Game {

    private static Game instance = null;
    
    public static Game getInstance() {
        return instance;
    }

    public abstract World getWorld();

    public static void setInstance(Game instance) {
        Game.instance = instance;
    }

    public abstract void sendToAll(EngineEvent event);
    
}
