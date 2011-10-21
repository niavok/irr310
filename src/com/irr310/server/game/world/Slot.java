package com.irr310.server.game.world;

import com.irr310.server.game.GameEntity;

public class Slot extends GameEntity{

	private final int position;

	public Slot(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

}
