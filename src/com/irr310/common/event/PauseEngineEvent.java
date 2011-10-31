package com.irr310.common.event;


public class PauseEngineEvent extends EngineEvent {

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}