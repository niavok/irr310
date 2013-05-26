package com.irr310.common.event.system;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.Ship;

public class ShipDeployedSystemEvent extends SystemEvent {

	final private Ship ship;
	private final TransformMatrix transform;

	public ShipDeployedSystemEvent(Ship ship, TransformMatrix transform) {
		this.ship = ship;
		this.transform = transform;
	}

	@Override
	public void accept(SystemEventVisitor visitor) {
		visitor.visit(this);
	}

	public Ship getShip() {
		return ship;
	}
	
	public TransformMatrix getTransform() {
		return transform;
	}

}
