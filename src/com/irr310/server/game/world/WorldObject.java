package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.game.GameEntity;

public abstract class WorldObject extends GameEntity {

	private String name;
	protected List<Part> parts;
	
	public WorldObject() {
		name = "unamed object";
		parts = new ArrayList<Part>();
	}

	public String getName() {
		return getName();
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Part> getParts() {
		return parts;
	}
}
