package com.irr310.common.event;

import com.irr310.common.world.system.Ship;

public class BindAIEvent extends EngineEvent {

	final private Ship ship;

	public BindAIEvent(Ship ship) {
		this.ship = ship;
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Ship getShip() {
		return ship;
	}

}
