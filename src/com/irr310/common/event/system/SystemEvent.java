package com.irr310.common.event.system;

import com.irr310.common.event.EngineEvent;


public abstract class SystemEvent implements EngineEvent<SystemEventVisitor> {
	
	
	public abstract void accept(SystemEventVisitor visitor);
}
