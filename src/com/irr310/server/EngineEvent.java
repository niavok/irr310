package com.irr310.server;

public abstract class EngineEvent {
	
	
	public abstract void accept(EngineEventVisitor visitor);
}
