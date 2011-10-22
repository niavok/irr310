package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.server.GameServer;
import com.irr310.server.Vect3;
import com.irr310.server.event.WorldObjectAddedEvent;

public class World {

	List<WorldObject> objects;
	
	public World() {
		objects = new ArrayList<WorldObject>();
	}
	
	public void placeShipSafely(Ship playerShip, Vect3 vect3) {
		
		
		// TODO Auto-generated method stub
		
	}

	public void addObject(WorldObject o) {
		objects.add(o);
		GameServer.getInstance().sendToAll(new WorldObjectAddedEvent(o));
	}

}
