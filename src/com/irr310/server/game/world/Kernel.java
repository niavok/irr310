package com.irr310.server.game.world;

import com.irr310.server.Vect3;

public class Kernel extends SimpleComponent {

	public Kernel() {

		uniquePart.setMass(1d);
		uniquePart.setShape(new Vect3(1, 1, 1));

		addSlot(uniquePart, new Vect3(0.5, 0., 0.));
		addSlot(uniquePart, new Vect3(-0.5, 0., 0.));
		addSlot(uniquePart, new Vect3(0., 0.5, 0.));
		addSlot(uniquePart, new Vect3(0., -0.5, 0.));
		addSlot(uniquePart, new Vect3(0., 0., 0.5));
		addSlot(uniquePart, new Vect3(0., 0., -0.5));
	}

}
