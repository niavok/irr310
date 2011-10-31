package com.irr310.common.event;


public abstract class EngineEvent  {
	
	
	public abstract void accept(ServerEngineEventVisitor visitor);
}
