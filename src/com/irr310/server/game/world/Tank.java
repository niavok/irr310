package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.Vect3;


public class Tank  extends SimpleComponent implements Container  {

	List<WorldObject> contents = new ArrayList<WorldObject>();
	
	double capacity;
	double capacityAvailable;
	
	public Tank( ) {
		uniquePart.setMass(20d);
		uniquePart.setShape(new Vect3(4, 7, 4));

		addSlot(uniquePart, new Vect3(0, 0., 2));
		addSlot(uniquePart, new Vect3(0, 0., -2));
		addSlot(uniquePart, new Vect3(0, 3.5, 0));
		addSlot(uniquePart, new Vect3(0, -3.5, 0));
		addSlot(uniquePart, new Vect3(2, 0., 0));
		addSlot(uniquePart, new Vect3(-2, 0., 0));
		
	}

	@Override
	public boolean assign(Component component) {
		if(isUnusable()) {
			return false;
		}
		
		
		
		
		return false;
	}

	@Override
	public void remove(Component component) {
		// TODO Auto-generated method stub
		
	}

}
