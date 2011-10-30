package com.irr310.server.event;

import com.irr310.common.engine.EngineEvent;

public abstract class ServerEngineEvent extends EngineEvent {
	
	
	public abstract void accept(ServerEngineEventVisitor visitor);
}
