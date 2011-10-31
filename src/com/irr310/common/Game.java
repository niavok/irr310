package com.irr310.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.event.AddShipEvent;
import com.irr310.common.world.World;
import com.irr310.server.GameServer;

public abstract class Game {

    private static Game instance = null;
    
    public static Game getInstance() {
        return instance;
    }

    public abstract World getWorld();

    public static void setInstance(Game instance) {
        Game.instance = instance;
    }
    
}
