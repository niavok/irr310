package com.irr310.server.game.world;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;

public class Slot extends GameEntity {

	private final Vect3 position;
	private final Component parentComponent;
	private final Part part;

	public Slot(Component parentComponent, Part part,  Vect3 position) {
		this.parentComponent = parentComponent;
		this.part = part;
		this.position = position;
	}

	public Vect3 getPosition() {
		return position;
	}

	public Component getComponent() {
		return parentComponent;
	}

	public Part getPart() {
		return part;
	}

	public Vect3 getAbsoluteShipPosition() {
		return parentComponent.getAbsoluteShipPosition(position);
	}
}
