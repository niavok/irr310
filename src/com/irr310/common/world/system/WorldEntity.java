package com.irr310.common.world.system;

import com.irr310.common.world.World;

public class WorldEntity {
	private final long id;
    private final World world;
	
	public WorldEntity(World world, long id) {
        this.world = world;
        this.id = id;
	}
	
	public long getId() {
        return id;
    }
	
	public World getWorld() {
        return world;
    }
}
