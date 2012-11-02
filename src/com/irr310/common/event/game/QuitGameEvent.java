package com.irr310.common.event.game;



public class QuitGameEvent extends GameEvent {

	@Override
	public void accept(GameEventVisitor visitor) {
		visitor.visit(this);
	}

}
