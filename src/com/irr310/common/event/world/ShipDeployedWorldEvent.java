package com.irr310.common.event.world;

import com.irr310.common.world.state.ItemState;


public class ShipDeployedWorldEvent extends WorldEvent {


    private final ItemState ship;

    public ShipDeployedWorldEvent(ItemState ship) {
        this.ship = ship;
    }

    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

    public ItemState getShip() {
        return ship;
    }
}
