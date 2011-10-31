package com.irr310.common.event;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.WorldObject;

public class WorldShipAddedEvent extends EngineEvent {

	final private Ship ship;
	private final Vect3 position;

	public WorldShipAddedEvent(Ship ship, Vect3 position) {
		this.ship = ship;
		this.position = position;
	}

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Ship getShip() {
		return ship;
	}
	
	public Vect3 getPosition() {
		return position;
	}

}
