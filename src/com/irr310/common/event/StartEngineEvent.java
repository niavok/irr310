package com.irr310.common.event;


public class StartEngineEvent extends EngineEvent{

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
