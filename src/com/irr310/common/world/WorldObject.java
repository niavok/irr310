package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;


public abstract class WorldObject extends GameEntity {

	private String name;
	protected List<Part> parts;
	
	public WorldObject(long id) {
	    super(id);
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
