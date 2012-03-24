package com.irr310.common.world.capacity;

import com.irr310.common.world.view.CapacityView;

public class RocketCapacity extends Capacity {

    public double currentThrust;
    public double targetThrust;
    public double theoricalMaxThrust;
    public double theoricalMinThrust;
    public double theoricalVariationSpeed;
    public double maxThrust;
    public double minThrust;
    public double variationSpeed;

    public double targetThrustInput;
    public ExplosiveCapacity explosive;
    public double securityTimeout;
    public double thrustDuration;
    public double stability;

    public RocketCapacity(long id) {
        super(id);
        currentThrust = 1;
        targetThrust = 0;
        targetThrustInput =0 ;
        stability = 0.5;
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

    public void setTargetThrust(double targetThrustInput) {
        this.targetThrustInput = targetThrustInput;
    }
    
    public double getTargetThrustInput() {
        return targetThrustInput;
    }

    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.LINEAR_ENGINE.ordinal();

        //TODO
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        setName(view.name);
        //TODO
    }

    

}
