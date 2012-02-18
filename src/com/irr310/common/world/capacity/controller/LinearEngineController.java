package com.irr310.common.world.capacity.controller;

import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.LinearEngineCapacity;

public class LinearEngineController extends CapacityController {

    private final LinearEngineCapacity capacity;
    private final Component component;

    public LinearEngineController(Component component, LinearEngineCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
    }

    @Override
    public void update(double duration) {

        capacity.targetThrust = capacity.targetThrustInput;

        capacity.maxThrust = component.getEfficiency() * capacity.theoricalMaxThrust;
        capacity.minThrust = component.getEfficiency() * capacity.theoricalMinThrust;
        capacity.variationSpeed = component.getEfficiency() * capacity.theoricalVariationSpeed;

        double realTargetTrust = Math.min(capacity.targetThrust, capacity.maxThrust);
        realTargetTrust = Math.max(realTargetTrust, capacity.minThrust);

        if (capacity.currentThrust > capacity.maxThrust) {
            // If the current thrust is too hight, slow down the engine
            capacity.currentThrust -= duration * capacity.theoricalVariationSpeed;
            if (capacity.currentThrust < realTargetTrust) {
                capacity.currentThrust = realTargetTrust;
            }
        } else if (capacity.currentThrust < capacity.minThrust) {
            // If the current thrust is too hight, slow down the engine
            capacity.currentThrust += duration * capacity.theoricalVariationSpeed;
            if (capacity.currentThrust > realTargetTrust) {
                capacity.currentThrust = realTargetTrust;
            }
        } else if (capacity.currentThrust != capacity.targetThrust) {

            double deltaThrust = realTargetTrust - capacity.currentThrust;
            double realDeltaThrust;
            if (deltaThrust > 0) {
                realDeltaThrust = Math.min(deltaThrust, capacity.variationSpeed * duration);
            } else {
                realDeltaThrust = Math.max(deltaThrust, -capacity.variationSpeed * duration);
            }

            capacity.currentThrust += realDeltaThrust;
        }

    }

}
