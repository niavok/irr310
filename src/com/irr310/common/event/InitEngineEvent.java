package com.irr310.common.event;


public class InitEngineEvent extends EngineEvent {

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
