package com.irr310.common.event;


public interface EngineEvent<T extends EngineEventVisitor>  {
	
	
	public void accept(T visitor);
}
