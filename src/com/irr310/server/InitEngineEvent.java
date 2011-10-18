package com.irr310.server;

public class InitEngineEvent extends EngineEvent {

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
