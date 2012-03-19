package com.irr310.common.event;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Ship;

public class WorldShipAddedEvent extends EngineEvent {

	final private Ship ship;
	private final TransformMatrix transform;

	public WorldShipAddedEvent(Ship ship, TransformMatrix transform) {
		this.ship = ship;
		this.transform = transform;
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Ship getShip() {
		return ship;
	}
	
	public TransformMatrix getTransform() {
		return transform;
	}

}
