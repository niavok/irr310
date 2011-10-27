package com.irr310.server.game.world;

import com.irr310.server.Vect3;


public class PVCell extends SimpleComponent {

	public PVCell() {
		
		uniquePart.setMass(5d);
		uniquePart.setShape(new Vect3(6, 6, 1));

		addSlot(uniquePart, new Vect3(0, 3., 0));
		addSlot(uniquePart, new Vect3(0, -3., 0));
		addSlot(uniquePart, new Vect3(3, 0., 0));
		addSlot(uniquePart, new Vect3(-3, 0., 0));
	}

}
