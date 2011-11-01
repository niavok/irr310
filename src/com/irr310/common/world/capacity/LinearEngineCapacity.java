package com.irr310.common.world.capacity;

import com.irr310.common.world.view.CapacityView;

public class LinearEngineCapacity extends Capacity {

    public double currentThrust;
    public double targetThrust;
    public double theoricalMaxThrust;
    public double theoricalVariationSpeed;
    public double maxThrust;
    public double variationSpeed;

    public double targetThrustInput;

    public LinearEngineCapacity(long id) {
        super(id);
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

    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.type = CapacityType.LINEAR_ENGINE.ordinal();

        view.pushDouble(currentThrust);
        view.pushDouble(targetThrust);
        view.pushDouble(theoricalMaxThrust);
        view.pushDouble(theoricalVariationSpeed);
        view.pushDouble(maxThrust);
        view.pushDouble(variationSpeed);
        view.pushDouble(targetThrustInput);
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        currentThrust = view.popDouble();
        targetThrust = view.popDouble();
        theoricalMaxThrust = view.popDouble();
        theoricalVariationSpeed = view.popDouble();
        maxThrust = view.popDouble();
        variationSpeed = view.popDouble();
        targetThrustInput = view.popDouble();
    }

}
