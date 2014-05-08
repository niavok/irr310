package com.irr310.common.world.capacity;

import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.WorldSystem;

public class LinearEngineCapacity extends Capacity {

    public double currentThrust;
    public double targetThrust;
    public double theoricalMaxThrust;
    public double theoricalMinThrust;
    public double theoricalVariationSpeed;
    public double maxThrust;
    public double minThrust;
    public double variationSpeed;

    public double targetThrustInput;

    public LinearEngineCapacity(WorldSystem worldSystem, long id) {
        super(worldSystem, id);
        currentThrust = 0;
        targetThrust = 0;
        targetThrustInput =0 ;
    }

    public double getCurrentThrust() {

        return currentThrust;
    }

    public double getTheoricalVariationSpeed() {
        return theoricalVariationSpeed;
    }

    public double getTargetThrust() {
        return targetThrust;
    }

    public double getTheoricalMaxThrust() {
        return theoricalMaxThrust;
    }
    
    public double getTheoricalMinThrust() {
        return theoricalMinThrust;
    }

    public double getMaxThrust() {
        return maxThrust;
    }
    
    public double getMinThrust() {
        return minThrust;
    }

    public double getVariationSpeed() {
        return variationSpeed;
    }


    public void setTargetThrust(double targetThrust) {
        this.targetThrust = targetThrust;
    }
    
    public double getTargetThrustInput() {
        return targetThrustInput;
    }

    public void setCurrentThrust(double currentThrust) {
        this.currentThrust = currentThrust;
    }

    public void setTheoricalMaxThrust(double theoricalMaxThrust) {
        this.theoricalMaxThrust = theoricalMaxThrust;
    }

    public void setTheoricalMinThrust(double theoricalMinThrust) {
        this.theoricalMinThrust = theoricalMinThrust;
    }

    public void setTheoricalVariationSpeed(double theoricalVariationSpeed) {
        this.theoricalVariationSpeed = theoricalVariationSpeed;
    }

    public void setMaxThrust(double maxThrust) {
        this.maxThrust = maxThrust;
    }

    public void setMinThrust(double minThrust) {
        this.minThrust = minThrust;
    }

    public void setVariationSpeed(double variationSpeed) {
        this.variationSpeed = variationSpeed;
    }

    public void setTargetThrustInput(double targetThrustInput) {
        this.targetThrustInput = targetThrustInput;
    }
}
