package com.irr310.server.event;

public abstract class EngineEvent {
	
	
	public abstract void accept(EngineEventVisitor visitor);
}
