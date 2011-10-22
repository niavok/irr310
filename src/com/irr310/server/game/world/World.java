package com.irr310.server.game.world;

import java.util.HashMap;
import java.util.Map;

import com.irr310.server.Vect3;

public class World {

	Map<String, WorldObject> objects;
	
	public World() {
		objects = new HashMap<String, WorldObject>();
	}
	
	public WorldObject getObjectByName(String name) {
		return objects.get(name);
	}

	public void placeShipSafely(Ship playerShip, Vect3 vect3) {
		
		
		// TODO Auto-generated method stub
		
	}

}
