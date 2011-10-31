package com.irr310.common.event;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.WorldObject;


public class WorldObjectAddedEvent extends EngineEvent {

	final private WorldObject object;

	public WorldObjectAddedEvent(WorldObject object) {
		this.object = object;
	}
	
	@Override
	public void accept(ServerEngineEventVisitor visitor) {
				visitor.visit(this);
	}

	public WorldObject getObject() {
		return object;
	}

	
	
}
