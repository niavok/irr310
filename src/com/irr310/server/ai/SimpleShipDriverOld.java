package com.irr310.server.ai;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class SimpleShipDriverOld {

    private final Ship ship;
    private double targetSpeed;
    private Vec3 targetPosition;
    private LinearEngineCapacity leftEngineCapacity;
    private LinearEngineCapacity rightEngineCapacity;
    private LinearEngineCapacity topEngineCapacity;
    private LinearEngineCapacity bottomEngineCapacity;
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
    private Component leftEngine;
    private Component rightEngine;
    private Component topEngine;
    private Component bottomEngine;
    private ControlMode controlMode;

    public enum ControlMode {
        MANUAL,
        AUTOMATIC,
    }
    
    public SimpleShipDriverOld(Ship ship) {
        this.ship = ship;
        
        controlMode = ControlMode.AUTOMATIC;
        
        leftEngine = ship.getComponentByKey("leftReactor");
        rightEngine = ship.getComponentByKey("rightReactor");
        topEngine = ship.getComponentByKey("topReactor");
        bottomEngine = ship.getComponentByKey("bottomReactor");
        
        leftEngineCapacity = leftEngine.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        rightEngineCapacity = rightEngine.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        topEngineCapacity = topEngine.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        bottomEngineCapacity = bottomEngine.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        kernel = ship.getComponentByKey("kernel");

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

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
    }
    
    public double getMaxSpeed() {
        return ship.getMaxSpeed(false);
    }

    /**
     * Only for manual control mode
     * @param rotationSpeedTarget
     */
    public void setRotationSpeedTarget(Vec3 rotationSpeedTarget) {
        this.rotationSpeedTarget = rotationSpeedTarget;
    }
    
    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    /**
     * Only for automatic control mode
     * @param targetPosition
     */
    public void setPositionTarget(Vec3 targetPosition) {
        this.targetPosition = targetPosition;

    }

    public void processOrders() {

        if(controlMode == ControlMode.AUTOMATIC) {
        
        TransformMatrix shipTransform = kernel.getFirstPart().getTransform();

        Vec3 shipPosition = shipTransform.getTranslation();
       
        Vec3 localTargetPosition = targetPosition.transform(shipTransform.inverse());
        
        Vec3 localTargetDirection  = localTargetPosition.normalize();
        
        Vec3 left = new Vec3(1, 0, 0);
        Vec3 top = new Vec3(0, 0, 1);
        Vec3 front = new Vec3(0, 1, 0);
        
        
        double diffX = left.dot(localTargetDirection);
        double diffY = front.dot(localTargetDirection);
        double diffZ = top.dot(localTargetDirection);
        double dir = front.dot(localTargetDirection);
        
       

        this.rotationSpeedTarget.x = 0;
        this.rotationSpeedTarget.z = 0;
        
        this.rotationSpeedTarget.z = -diffX * maxRotationSpeed;
        this.rotationSpeedTarget.x = diffZ * maxRotationSpeed;
       
        }
        

        processEngines();
    }
    
    private void processEngines() {

        Time time = Time.getGameTime();

        double theoricalMaxSpeed = ship.getMaxSpeed(true);
        double currentSpeed = kernel.getFirstPart().getLinearSpeed().dot(new Vec3(0, 1, 0).rotate(kernel.getFirstPart().getTransform()));
        Duration deltaTime = lastTime.durationTo(time);
        double deltaSpeed = (currentSpeed - this.lastSpeed);
        double deltaAcc = (deltaSpeed - this.lastDeltaSpeed);

        double maxThrustLeft = (leftEngine.isAttached() ?   leftEngineCapacity.getMaxThrust() : 0);
        double maxThrustRight = (rightEngine.isAttached() ?   rightEngineCapacity.getMaxThrust() : 0);
        double maxThrustTop = (topEngine.isAttached() ?   topEngineCapacity.getMaxThrust() : 0);
        double maxThrustBottom = (bottomEngine.isAttached() ?   bottomEngineCapacity.getMaxThrust() : 0);
        double minThrustLeft = (leftEngine.isAttached() ?   leftEngineCapacity.getMinThrust() : 0);
        double minThrustRight = (rightEngine.isAttached() ?   rightEngineCapacity.getMinThrust() : 0);
        double minThrustTop = (topEngine.isAttached() ?   topEngineCapacity.getMinThrust() : 0);
        double minThrustBottom = (bottomEngine.isAttached() ?   bottomEngineCapacity.getMinThrust() : 0);

        if (maxThrustLeft != lastMaxThrustLeft || maxThrustRight != lastMaxThrustRight || maxThrustTop != lastMaxThrustTop
                || maxThrustBottom != lastMaxThrustBottom) {
            Log.trace("return to phase 0 lastMaxThrustLeft diff ");
            phase = 0;
        }

        //Log.trace("leftEngine.getMaxThrust()" + maxThrustLeft);
        double maxThrust = Math.min(maxThrustLeft, maxThrustRight);
        double minThrust = Math.min(-minThrustLeft, -minThrustRight);
        double maxRotationThrust = Math.min(minThrust, maxThrust);
        double maxHorizontalThrust = Math.min(maxThrustLeft, maxThrustRight);
        double minHorizontalThrust = Math.min(-minThrustLeft, -minThrustRight);
        double maxVerticalThrust = Math.min(maxThrustTop, maxThrustBottom);
        double minVerticalThrust = Math.min(-minThrustTop, -minThrustBottom);
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
                    Log.trace("return to phase 0 because too big difference ");
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

        minHorizontalThrust = Math.max(-leftEngineCapacity.getMinThrust(), -rightEngineCapacity.getMinThrust());
        minVerticalThrust = Math.max(-minThrustTop, -minThrustBottom);

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

        //Log.trace("this.leftThrustTarget =" + this.leftThrustTarget);

        leftEngineCapacity.setTargetThrust(this.leftThrustTarget + leftRotThrust);
        rightEngineCapacity.setTargetThrust(this.rightThrustTarget + rightRotThrust);
        topEngineCapacity.setTargetThrust(this.topThrustTarget + topRotThrust);
        bottomEngineCapacity.setTargetThrust(this.bottomThrustTarget + bottomRotThrust);

        this.lastSpeed = currentSpeed;
        this.lastSpeedTarget = this.targetSpeed;
        this.lastTime = time;
        this.lastDeltaSpeed = deltaSpeed;
        this.lastMaxThrustLeft = maxThrustLeft;
        this.lastMaxThrustRight = maxThrustRight;
        this.lastMaxThrustTop = maxThrustTop;
        this.lastMaxThrustBottom = maxThrustBottom;
    }

}
