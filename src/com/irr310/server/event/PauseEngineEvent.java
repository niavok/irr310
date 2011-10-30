package com.irr310.server.event;


public class PauseEngineEvent extends ServerEngineEvent {

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
