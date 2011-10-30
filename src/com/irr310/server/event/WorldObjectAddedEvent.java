package com.irr310.server.event;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.WorldObject;


public class WorldObjectAddedEvent extends ServerEngineEvent {

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
