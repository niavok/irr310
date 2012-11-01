package com.irr310.server.ai;

import com.irr310.common.world.system.Ship;

public abstract class AIProcessor {

    private final Ship ship;

    public AIProcessor(Ship ship) {
        this.ship = ship;
    }

    public abstract void process();

    public Ship getShip() {
        return ship;
    }

}
