package com.irr310.server.game.world;

import com.irr310.server.Vect3;


public class BigPropeller extends LinearEngine {

	public BigPropeller() {
		uniquePart.setMass(48d);
		uniquePart.setShape(new Vect3(4, 2, 4));

		addSlot(uniquePart, new Vect3(0, 0., 2));
		addSlot(uniquePart, new Vect3(0, 0., -2));
		addSlot(uniquePart, new Vect3(2, 0., 0));
		addSlot(uniquePart, new Vect3(-2, 0., 0));
	}

}
