package com.irr310.common.event.game;

import com.irr310.common.event.EngineEvent;


public abstract class GameEvent implements EngineEvent<GameEventVisitor>  {
	
	
	public abstract void accept(GameEventVisitor visitor);
}
