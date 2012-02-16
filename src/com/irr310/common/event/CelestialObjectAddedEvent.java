package com.irr310.common.event;

import com.irr310.common.world.CelestialObject;


public class CelestialObjectAddedEvent extends EngineEvent {

	final private CelestialObject object;

	public CelestialObjectAddedEvent(CelestialObject object) {
		this.object = object;
	}
	
	@Override
	public void accept(EngineEventVisitor visitor) {
				visitor.visit(this);
	}

	public CelestialObject getObject() {
		return object;
	}

	
	
}
