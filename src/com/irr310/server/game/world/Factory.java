package com.irr310.server.game.world;

import com.irr310.server.Vect3;


public class Factory extends SimpleComponent {

	public Factory() {
		
		uniquePart.setMass(48d);
		uniquePart.setShape(new Vect3(4, 4, 3));

		addSlot(uniquePart, new Vect3(0, 0., 1.5));
		addSlot(uniquePart, new Vect3(0, 0., -1.5));
		addSlot(uniquePart, new Vect3(0, 2., 0));
		addSlot(uniquePart, new Vect3(0, -2., 0));
		addSlot(uniquePart, new Vect3(2, 0., 0));
		addSlot(uniquePart, new Vect3(-2, 0., 0));
		
		
	}

}
