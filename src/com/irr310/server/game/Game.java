package com.irr310.server.game;

import com.irr310.common.world.World;

public class Game {

    private World mWorld;
    
    public Game() {
        mWorld = new World();
    }
    
    public void load() {
        
    }

    public World getWorld() {
        return mWorld;
    }

}
