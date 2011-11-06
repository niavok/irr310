package com.irr310.client.script.js.objects;


public class LinearEngineCapacity extends Capacity {

    private final com.irr310.common.world.capacity.LinearEngineCapacity capacity;
	
	public LinearEngineCapacity(com.irr310.common.world.capacity.LinearEngineCapacity capacity) {
	    this.capacity = capacity;
	}
	
	public void setTargetThrust(double thrust) {
	    System.out.println("trust to "+thrust);
	    capacity.setTargetThrust(thrust);
	}
	
	public double getMaxThrust() {
	    return capacity.getMaxThrust();
	}
	
}
