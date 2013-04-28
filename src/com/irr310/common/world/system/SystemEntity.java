package com.irr310.common.world.system;


public class SystemEntity extends WorldEntity {
    private WorldSystem system;
	
	public SystemEntity(WorldSystem system, long id) {
	    super(system.getWorld(), id);
        this.system = system;
	}

	public WorldSystem getSystem() {
        return system;
    }
}
