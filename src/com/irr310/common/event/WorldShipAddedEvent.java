package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Ship;

public class WorldShipAddedEvent extends EngineEvent {

	final private Ship ship;
	private final Vec3 position;

	public WorldShipAddedEvent(Ship ship, Vec3 position) {
		this.ship = ship;
		this.position = position;
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Ship getShip() {
		return ship;
	}
	
	public Vec3 getPosition() {
		return position;
	}

}
