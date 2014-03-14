package com.irr310.common.world.system;


public class WorldSystemEntity extends WorldEntity {
    private final WorldSystem mWorldSystem;
	
	public WorldSystemEntity(WorldSystem worldSystem, long id) {
	    super(worldSystem.getWorld(), id);
        mWorldSystem = worldSystem;
	}
	
	public WorldSystem getWorldSystem() {
        return mWorldSystem;
    }
}
