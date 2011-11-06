package com.irr310.client.script.js.objects;

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
	
}
