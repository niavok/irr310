package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;


public class Tank  extends SimpleComponent implements Container  {

	List<WorldObject> contents = new ArrayList<WorldObject>();
	
	double capacity;
	double capacityAvailable;
	
	public Tank( ) {
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
