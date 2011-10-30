package com.irr310.client.event;

import com.irr310.client.world.ShipProxy;

public class ShipAddEvent extends ClientEngineEvent {

	private final ShipProxy ship;

	public ShipAddEvent(ShipProxy ship) {
        this.ship = ship;
	}

	@Override
	public void accept(ClientEventVisitor visitor) {
		visitor.visit(this);
	}

	public ShipProxy getShip() {
        return ship;
    }
}
