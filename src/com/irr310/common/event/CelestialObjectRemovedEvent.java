package com.irr310.common.event;

import com.irr310.common.world.zone.CelestialObject;


public class CelestialObjectRemovedEvent extends EngineEvent {

    public enum Reason {
        LEAVE_OUT_WORLD, DESTROYED, LOOTED,
    }
    
	final private CelestialObject object;
    private final Reason reason;

	public CelestialObjectRemovedEvent(CelestialObject object, Reason reason) {
		this.object = object;
        this.reason = reason;
	}
	
	@Override
	public void accept(EngineEventVisitor visitor) {
				visitor.visit(this);
	}

	public CelestialObject getObject() {
		return object;
	}
	
	public Reason getReason() {
        return reason;
    }
	
	
}
