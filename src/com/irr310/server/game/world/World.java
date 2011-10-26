package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.server.GameServer;
import com.irr310.server.Vect3;
import com.irr310.server.event.WorldObjectAddedEvent;
import com.irr310.server.event.WorldShipAddedEvent;

public class World {

	List<WorldObject> objects;
	List<Ship> ships;
	
	public World() {
		objects = new ArrayList<WorldObject>();
		ships = new ArrayList<Ship>();
	}
	
	public void addObject(WorldObject o) {
		objects.add(o);
		GameServer.getInstance().sendToAll(new WorldObjectAddedEvent(o));
	}

	public void addShip(Ship ship, Vect3 position) {
		ships.add(ship);
		GameServer.getInstance().sendToAll(new WorldShipAddedEvent(ship, position));
		
		/*System.out.println("Add ship, "+ship.getComponents().size()+" components");
		for(Component component: ship.getComponents()) {
			System.out.println("Add component '"+component.getClass().getSimpleName()+"' in world at position "+component.getShipPosition().toString());
			component.getTransform().setTranslation(vect3.plus(component.getShipPosition()));
			addObject(component);
			
		}*/
		
		//for()
		
		
		
	}
	
	
	
	
	

}
