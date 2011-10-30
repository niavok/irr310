package com.irr310.client.event;

import com.irr310.common.engine.EngineEvent;

public abstract class ClientEngineEvent extends EngineEvent {
	
	
	public abstract void accept(ClientEventVisitor visitor);
}
