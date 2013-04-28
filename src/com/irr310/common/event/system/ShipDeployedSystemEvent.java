package com.irr310.common.event.system;

import com.irr310.common.world.system.Ship;


public class ShipDeployedSystemEvent extends SystemEvent {


    private final Ship ship;

    public ShipDeployedSystemEvent(Ship ship) {
        this.ship = ship;
    }

    @Override
    public void accept(SystemEventVisitor visitor) {
        visitor.visit(this);
    }

    public Ship getShip() {
        return ship;
    }
}
