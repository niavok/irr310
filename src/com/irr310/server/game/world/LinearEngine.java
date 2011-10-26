package com.irr310.server.game.world;

import com.irr310.server.Vect3;


public class LinearEngine extends SimpleComponent {

	public LinearEngine() {
		uniquePart.setMass(5d);
		uniquePart.setShape(new Vect3(1, 1, 1));

	}

}
