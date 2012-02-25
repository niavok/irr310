package com.irr310.common.event;


public class WorldReadyEvent extends EngineEvent {


    public WorldReadyEvent() {
    }

    @Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

}
