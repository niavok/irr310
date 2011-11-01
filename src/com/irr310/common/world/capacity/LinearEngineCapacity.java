package com.irr310.common.world.capacity;

public class LinearEngineCapacity extends Capacity {

    public double currentThrust;
    public double targetThrust;
    public final double theoricalMaxThrust;
    public final double theoricalVariationSpeed;
    public double maxThrust;
    public double variationSpeed;

    public double targetThrustInput;
    
    public LinearEngineCapacity(double theoricalMaxThrust, double theoricalVariationSpeed) {
        this.theoricalMaxThrust = theoricalMaxThrust;
        this.theoricalVariationSpeed = theoricalVariationSpeed;
    }

    public double getCurrentThrust() {
        
        return currentThrust;
    }

    public double getTargetThrust() {
        return targetThrust;
    }

    public double getTheoricalMaxThrust() {
        return theoricalMaxThrust;
    }

    public double getMaxThrust() {
        return maxThrust;
    }
    
    public double getVariationSpeed() {
        return variationSpeed;
    }

    public void setTargetThrust(double targetThrustInput) {
        this.targetThrustInput = targetThrustInput; 
    }

}
