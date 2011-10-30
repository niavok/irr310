package com.irr310.server.event;


public class QuitGameEvent extends ServerEngineEvent {

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
