package com.irr310.common.event.world;

import com.irr310.common.event.EngineEvent;



public abstract class WorldEvent implements EngineEvent<WorldEventVisitor>  {
	
	
	public abstract void accept(WorldEventVisitor visitor);
}
