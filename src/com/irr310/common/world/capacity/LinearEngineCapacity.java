package com.irr310.common.world.capacity;

import com.irr310.common.world.World;
import com.irr310.common.world.state.CapacityState;

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

    public LinearEngineCapacity(World world, long id) {
        super(world, id);
        currentThrust = 0;
        targetThrust = 0;
        targetThrustInput =0 ;
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
    public CapacityState toState() {
        CapacityState view = new CapacityState();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.LINEAR_ENGINE.ordinal();

        view.pushDouble(currentThrust);
        view.pushDouble(targetThrust);
        view.pushDouble(theoricalMaxThrust);
        view.pushDouble(theoricalMinThrust);
        view.pushDouble(theoricalVariationSpeed);
        view.pushDouble(maxThrust);
        view.pushDouble(minThrust);
        view.pushDouble(variationSpeed);
        view.pushDouble(targetThrustInput);
        return view;
    }

    @Override
    public void fromState(CapacityState view) {
        setName(view.name);
        currentThrust = view.popDouble();
        targetThrust = view.popDouble();
        theoricalMaxThrust = view.popDouble();
        theoricalMinThrust = view.popDouble();
        theoricalVariationSpeed = view.popDouble();
        maxThrust = view.popDouble();
        minThrust = view.popDouble();
        variationSpeed = view.popDouble();
        targetThrustInput = view.popDouble();
    }

    

}
