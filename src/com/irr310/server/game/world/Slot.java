package com.irr310.server.game.world;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;

public class Slot extends GameEntity {

	private final Vect3 position;
	private final Component parentComponent;

	public Slot(Component parentComponent,  Vect3 position) {
		this.parentComponent = parentComponent;
		this.position = position;
	}

	public Vect3 getPosition() {
		return position;
	}

	public Component getComponent() {
		return parentComponent;
	}

}
