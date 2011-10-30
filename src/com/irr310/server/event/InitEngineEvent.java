package com.irr310.server.event;


public class InitEngineEvent extends ServerEngineEvent {

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
