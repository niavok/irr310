package com.irr310.server.ai;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class SimpleShipDriver {

    private final Ship ship;
    private double targetSpeed;
    private Vec3 targetPosition;
    private LinearEngineCapacity leftEngine;
    private LinearEngineCapacity rightEngine;
    private LinearEngineCapacity topEngine;
    private LinearEngineCapacity bottomEngine;
    private Component kernel;
    private Time lastTime;
    private double lastSpeed;
    private double lastDeltaSpeed;
    private double optimalPower;
    private double lastSpeedTarget;
    private double allowPower;
    private int phase;
    private double initiaSpeed;
    private double maxOptimalPower;
    private double minOptimalPower;
    private double leftThrustTarget;
    private double rightThrustTarget;
    private double topThrustTarget;
    private double bottomThrustTarget;
    private boolean lastTooSlow;
    private boolean lastTooFast;
    private Vec3 rotationSpeedTarget;
    private double maxRotationSpeed;
    private double lastMaxThrustLeft;
    private double lastMaxThrustRight;
    private double lastMaxThrustTop;
    private double lastMaxThrustBottom;

    public SimpleShipDriver(Ship ship) {
        this.ship = ship;

        leftEngine = ship.getComponentByName("leftReactor").getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        rightEngine = ship.getComponentByName("rightReactor").getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        topEngine = ship.getComponentByName("topReactor").getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        bottomEngine = ship.getComponentByName("bottomReactor").getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        kernel = ship.getComponentByName("kernel");

        lastTime = Time.getGameTime();
        lastSpeed = 0;
        lastDeltaSpeed = 0;
        optimalPower = 0;
        lastSpeedTarget = 0;
        allowPower = 1;
        phase = 0;
        this.initiaSpeed = 0;
        this.maxOptimalPower = 0;
        this.minOptimalPower = 0;
        this.leftThrustTarget = 0;
        this.rightThrustTarget = 0;
        this.topThrustTarget = 0;
        this.bottomThrustTarget = 0;
        this.lastTooSlow = false;
        this.lastTooFast = false;
        this.rotationSpeedTarget = new Vec3();
        this.maxRotationSpeed = 2;
    }

    public double getMaxSpeed() {
        return ship.getTheoricalMaxSpeed();
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public void setPositionTarget(Vec3 targetPosition) {
        this.targetPosition = targetPosition;

    }

    public void processOrders() {
        // TODO rotation

        processEngines();
    }

    private void processEngines() {

        Time time = Time.getGameTime();

        double theoricalMaxSpeed = ship.getTheoricalMaxSpeed();
        double currentSpeed = kernel.getFirstPart().getLinearSpeed().dot(new Vec3(0, 1, 0).rotate(kernel.getFirstPart().getTransform()));
        Duration deltaTime = lastTime.durationTo(time);
        double deltaSpeed = (currentSpeed - this.lastSpeed);
        double deltaAcc = (deltaSpeed - this.lastDeltaSpeed);

        double maxThrustLeft = leftEngine.getMaxThrust();
        double maxThrustRight = rightEngine.getMaxThrust();
        double maxThrustTop = topEngine.getMaxThrust();
        double maxThrustBottom = bottomEngine.getMaxThrust();

        if (maxThrustLeft != lastMaxThrustLeft || maxThrustRight != lastMaxThrustRight || maxThrustTop != lastMaxThrustTop
                || maxThrustBottom != lastMaxThrustBottom) {
            phase = 0;
        }

        Log.trace("leftEngine.getMaxThrust()" + maxThrustLeft);
        double maxThrust = Math.min(maxThrustLeft, maxThrustRight);
        double minThrust = Math.min(-leftEngine.getMinThrust(), -rightEngine.getMinThrust());
        double maxRotationThrust = Math.min(minThrust, maxThrust);
        double maxHorizontalThrust = Math.min(maxThrustLeft, maxThrustRight);
        double minHorizontalThrust = Math.min(-leftEngine.getMinThrust(), -rightEngine.getMinThrust());
        double maxVerticalThrust = Math.min(maxThrustTop, maxThrustBottom);
        double minVerticalThrust = Math.min(-topEngine.getMinThrust(), -bottomEngine.getMinThrust());
        this.optimalPower = this.targetSpeed / theoricalMaxSpeed;

        if (Math.abs(this.targetSpeed - currentSpeed) > 0.1) {

            if (this.lastSpeedTarget != this.targetSpeed) {
                // Thrust target changed, full power allowed
                this.allowPower = 1;
                this.phase = 0;
                Log.trace("engine go to phase 0");

                this.maxOptimalPower = 1;
                this.minOptimalPower = -1;
                this.initiaSpeed = currentSpeed;
            }

            if (this.phase == 0) {
                Log.trace("phase 0");
                if (this.targetSpeed > currentSpeed) {
                    Log.trace("maxHorizontalThrust=" + maxHorizontalThrust);
                    this.leftThrustTarget = maxHorizontalThrust;
                    this.rightThrustTarget = maxHorizontalThrust;
                    this.topThrustTarget = maxVerticalThrust;
                    this.bottomThrustTarget = maxVerticalThrust;
                    this.lastTooSlow = true;
                    this.lastTooFast = false;
                } else if (this.targetSpeed < currentSpeed) {
                    this.leftThrustTarget = -minHorizontalThrust;
                    this.rightThrustTarget = -minHorizontalThrust;
                    this.topThrustTarget = -minVerticalThrust;
                    this.bottomThrustTarget = -minVerticalThrust;
                    this.lastTooSlow = false;
                    this.lastTooFast = true;
                } else {
                    this.leftThrustTarget = 0;
                    this.rightThrustTarget = 0;
                    this.topThrustTarget = 0;
                    this.bottomThrustTarget = 0;
                }
                this.phase = 1;
                Log.trace("engine go to phase 1");
            } else if (this.phase == 1) {
                if (this.targetSpeed > currentSpeed && this.lastTooFast && deltaSpeed < -0.001) {
                    // Too slow
                    this.leftThrustTarget = maxHorizontalThrust * this.optimalPower;
                    this.rightThrustTarget = maxHorizontalThrust * this.optimalPower;
                    this.topThrustTarget = maxVerticalThrust * this.optimalPower;
                    this.bottomThrustTarget = maxVerticalThrust * this.optimalPower;
                    this.phase = 2;
                    Log.trace("engine go to phase 2 too slow");
                } else if (this.targetSpeed < currentSpeed && this.lastTooSlow && deltaSpeed > 0.001) {
                    // Too fast
                    this.leftThrustTarget = maxHorizontalThrust * this.optimalPower;
                    this.rightThrustTarget = maxHorizontalThrust * this.optimalPower;
                    this.topThrustTarget = maxVerticalThrust * this.optimalPower;
                    this.bottomThrustTarget = maxVerticalThrust * this.optimalPower;
                    this.phase = 2;
                    Log.trace("engine g o to phase 2 too fast");
                }
            } else if (this.phase == 2) {
                if (Math.abs(this.targetSpeed - currentSpeed) > theoricalMaxSpeed / 6) {
                    this.phase = 0;
                }
            }
        }

        Vec3 worldRotationSpeed = kernel.getFirstPart().getRotationSpeed();
        Vec3 localRotationSpeed = worldRotationSpeed.rotate(kernel.getFirstPart().getTransform().inverse());

        double topRotThrust = 0;
        double bottomRotThrust = 0;
        double leftRotThrust = 0;
        double rightRotThrust = 0;

        minHorizontalThrust = Math.max(-leftEngine.getMinThrust(), -rightEngine.getMinThrust());
        minVerticalThrust = Math.max(-topEngine.getMinThrust(), -bottomEngine.getMinThrust());

        if (Math.abs(localRotationSpeed.getX() - rotationSpeedTarget.getX()) > 0.001) {
            double deltaThrust = this.topThrustTarget - this.bottomThrustTarget;
            double deltaRot = localRotationSpeed.getX() - rotationSpeedTarget.getX();
            // var thrustRatio = Math.min(Math.pow((Math.abs(deltaRot) /
            // maxRotationSpeed)+0.3,3),1);
            double deltaRotRatio = Math.abs(deltaRot) / this.maxRotationSpeed;
            double thrustRatio = 0;
            if (deltaRotRatio > 0.1) {
                thrustRatio = 1;
            } else {
                thrustRatio = 10 * deltaRotRatio; // Math.pow(5*deltaRotRatio,2);
            }
            double thrustMax = minHorizontalThrust;
            if ((localRotationSpeed.getX() - rotationSpeedTarget.getX() < 0)) {
                // Too slow
                topRotThrust -= thrustMax * thrustRatio;
                bottomRotThrust += thrustMax * thrustRatio;
            }

            if ((localRotationSpeed.getX() - rotationSpeedTarget.getX() > 0)) {
                // Too fast
                topRotThrust += thrustMax * thrustRatio;
                bottomRotThrust -= thrustMax * thrustRatio;

            }

        }

        if (Math.abs(localRotationSpeed.getZ() - rotationSpeedTarget.getZ()) > 0.001) {
            double deltaThrust = this.topThrustTarget - this.bottomThrustTarget;
            double deltaRot = localRotationSpeed.getZ() - rotationSpeedTarget.getZ();
            // var thrustRatio = Math.min(Math.pow((Math.abs(deltaRot) /
            // maxRotationSpeed)+0.3,3),1);
            double deltaRotRatio = Math.abs(deltaRot) / this.maxRotationSpeed;
            double thrustRatio = 0;
            if (deltaRotRatio > 0.1) {
                thrustRatio = 1;
            } else {
                thrustRatio = 10 * deltaRotRatio; // Math.pow(5*deltaRotRatio,2);
            }
            double thrustMax = minVerticalThrust;
            if ((localRotationSpeed.getZ() - rotationSpeedTarget.getZ() < 0)) {
                // Too slow
                leftRotThrust -= thrustMax * thrustRatio;
                rightRotThrust += thrustMax * thrustRatio;
            }

            if ((localRotationSpeed.getZ() - rotationSpeedTarget.getZ() > 0)) {
                // Too fast
                leftRotThrust += thrustMax * thrustRatio;
                rightRotThrust -= thrustMax * thrustRatio;

            }

        }

        Log.trace("this.leftThrustTarget =" + this.leftThrustTarget);

        leftEngine.setTargetThrust(this.leftThrustTarget + leftRotThrust);
        rightEngine.setTargetThrust(this.rightThrustTarget + rightRotThrust);
        topEngine.setTargetThrust(this.topThrustTarget + topRotThrust);
        bottomEngine.setTargetThrust(this.bottomThrustTarget + bottomRotThrust);

        this.lastSpeed = currentSpeed;
        this.lastSpeedTarget = this.targetSpeed;
        this.lastTime = time;
        this.lastDeltaSpeed = deltaSpeed;
        this.lastMaxThrustLeft = maxThrustLeft;
        this.lastMaxThrustRight = maxThrustRight;
        this.lastMaxThrustTop = maxThrustTop;
        this.lastMaxThrustBottom = maxThrust;
    }

}
