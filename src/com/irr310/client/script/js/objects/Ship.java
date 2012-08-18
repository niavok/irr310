package com.irr310.client.script.js.objects;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.Game;

public class Ship {

	private com.irr310.common.world.Ship ship;
	
	public Ship(long id) {
		ship = Game.getInstance().getWorld().getShipById(id);
	}

	public long getId() {
        return ship.getId();
    }
	
	public Component getComponentByName(String name) {
	    return new Component(ship.getComponentByName(name).getId());
	}
	
	public double getTheoricalMaxSpeed() {
	    return ship.getMaxSpeed(true);
	}
	
	public List<Component> getComponentsByName(String name) {
	    List<Component> components = new ArrayList<Component>();
	    for(com.irr310.common.world.Component component : ship.getComponents()) {
	        if(component.getName().equals(name)) {
	            components.add(new Component(component.getId()));
	        }
	    }
	    return components;
	}
	
}
