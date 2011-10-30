package com.irr310.server.event;


public class StartEngineEvent extends ServerEngineEvent{

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
