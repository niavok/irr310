package com.irr310.server;

import java.util.HashMap;
import java.util.Map;

public class World {

	Map<String, WorldObject> objects;
	
	public World() {
		objects = new HashMap<String, WorldObject>();
	}
	
	public WorldObject getObjectByName(String name) {
		return objects.get(name);
	}

}
