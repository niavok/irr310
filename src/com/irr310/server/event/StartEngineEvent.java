package com.irr310.server.event;


public class StartEngineEvent extends EngineEvent{

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
