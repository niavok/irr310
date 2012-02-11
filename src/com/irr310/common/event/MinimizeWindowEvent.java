package com.irr310.common.event;


public class MinimizeWindowEvent extends EngineEvent {

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
