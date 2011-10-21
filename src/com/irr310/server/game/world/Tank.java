package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;


public class Tank  extends Component implements Container  {

	List<WorldObject> contents = new ArrayList<WorldObject>();
	
	double capacity;
	double capacityAvailable;
	
	public Tank( ) {
	}

	@Override
	public boolean assign(WorldObject object) {
		if(isUnusable()) {
			return false;
		}
		
		
		
		
		return false;
	}

}
