package com.irr310.server.ai;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Part;
import com.irr310.common.world.system.Ship;
import com.irr310.server.Time.Timestamp;

public class SimpleShipDriver implements ShipDriver {

    private static final double MASS_FACTOR = 10;
    private static final double ENERGY_EPSILON = 100;
    private Ship mShip;
    List<LinearEngineCapacity> mEngines = new ArrayList<LinearEngineCapacity>();
    private Vec3 mLinearVelocityCommand = Vec3.origin();
    private Vec3 mAngularVelocityCommand = Vec3.origin();
    private Component mKernel;
    private double mTotalMass;

    @Override
    public void init(Ship ship) {
        mShip = ship;

        mKernel = ship.getComponentByKey("kernel");
        mTotalMass = 0;
        
        for (Component component : ship.getComponents()) {
            for (Part part : component.getParts()) {
                mTotalMass += part.getMass() / MASS_FACTOR;
            }
            
            
            List<LinearEngineCapacity> engines = component.getCapacitiesByClass(LinearEngineCapacity.class);
            for (LinearEngineCapacity engine : engines) {
                mEngines.add(engine);
            }
        }
    }

    @Override
    public void setLinearVelocityCommand(Vec3 linearVelocity) {
        mLinearVelocityCommand = linearVelocity;
    }

    @Override
    public void setAngularVelocityCommand(Vec3 angularVelocity) {
        mAngularVelocityCommand = angularVelocity;
        Log.log("setAngularVelocityCommand " + angularVelocity);
    }

    @Override
    public void update(float seconds) {

        Vec3 linearSpeed = mKernel.getFirstPart().getLinearSpeed();
        double currentSpeedY = linearSpeed.dot(new Vec3(0, 1, 0).rotate(mKernel.getFirstPart().getTransform()));

        double currentEnergy = 0.5 * mTotalMass * currentSpeedY * currentSpeedY * (currentSpeedY > 0 ? 1 : -1); 
        
        double targetEnergy = 0.5 * mTotalMass * mLinearVelocityCommand.getY() * mLinearVelocityCommand.getY() * (mLinearVelocityCommand.getY() > 0 ? 1 : -1);
        
        double energyDelta = targetEnergy - currentEnergy; 
        
        double energyToZero = 0;
        
        for (LinearEngineCapacity engine : mEngines) {
            double thrustSign = (engine.getCurrentThrust() > 0 ? 1 : -1);
            energyToZero += thrustSign * 0.5 * engine.getCurrentThrust() * engine.getCurrentThrust() / engine.getVariationSpeed();
        }
        
        //double e = Math.abs(targetEnergy / 1000) + 0.1;
        double e = 10;
//        Log.log("targetEnergy="+targetEnergy);
//        Log.log("currentEnergy="+currentEnergy);
//        Log.log("energyToZero="+energyToZero);
        
        if (currentEnergy + energyToZero < targetEnergy - e) {
            for (LinearEngineCapacity engine : mEngines) {
                engine.setTargetThrust(engine.theoricalMaxThrust);
            }
        } else if (currentEnergy + energyToZero > targetEnergy + e) {
            for (LinearEngineCapacity engine : mEngines) {
                engine.setTargetThrust(engine.theoricalMinThrust);
            }
        } else if (currentEnergy + energyToZero < targetEnergy ) {
            for (LinearEngineCapacity engine : mEngines) {
                engine.setTargetThrust(engine.theoricalMaxThrust * (energyDelta - energyToZero) / e );
            }
        } else if (currentEnergy + energyToZero > targetEnergy) {
            for (LinearEngineCapacity engine : mEngines) {
                engine.setTargetThrust(engine.theoricalMinThrust * (energyToZero - energyDelta) / e  );
            }
        } else {
            for (LinearEngineCapacity engine : mEngines) {
                engine.setTargetThrust(0);
            }
        }
        
        Vec3 aim = new Vec3(0,0,0);
        
        Vec3 rotationSpeed = mKernel.getFirstPart().getRotationSpeed().rotate(mKernel.getFirstPart().getTransform().inverse());
        
        
        
        double mMaxAngularSpeed = 2;
        double mSoftModeAngularLimit = 1.0;
        
//        Log.log("mAngularVelocityCommand=" + mAngularVelocityCommand);
//        Log.log("rotationSpeed=" + rotationSpeed);
        
        
        double maxDiff = Double.MAX_VALUE;
        
        for (LinearEngineCapacity engine : mEngines) {
            maxDiff = Math.min(maxDiff, Math.min(-engine.getMinThrust(), engine.getMaxThrust()));
        }
        
//        Log.log("maxDiff=" + maxDiff);
        
        
        
        
        aim.x = computeSoftAngularCommand(rotationSpeed.x, mMaxAngularSpeed, mSoftModeAngularLimit, mAngularVelocityCommand.x);
        aim.y = computeSoftAngularCommand(rotationSpeed.y, mMaxAngularSpeed, mSoftModeAngularLimit, mAngularVelocityCommand.y);
        aim.z = computeSoftAngularCommand(rotationSpeed.z, mMaxAngularSpeed, mSoftModeAngularLimit, mAngularVelocityCommand.z);
          
//        Log.log("aim=" + aim);
//          aim = aim.multiply(maxDiff);
        
        
        for (LinearEngineCapacity engine : mEngines) {
//            Vector3 direction = mNode->getOrientation() * Vector3(0, 0, -1);
//            Vector3 rotAxis = mRelPosition.crossProduct(direction);
//            Vector3 target = mShip->getDirectionCommand();
//            Vector3 aim = mShip->getRotationCommand();
            
            
            
            Vec3 shipRotation = engine.getComponent().getShipRotation();
            TransformMatrix tmp = TransformMatrix.identity();
            
            tmp.translate(new Vec3(0,-1,0));
            
            tmp.rotateX(Math.toRadians(shipRotation.x));
            tmp.rotateY(Math.toRadians(shipRotation.y));
            tmp.rotateZ(Math.toRadians(shipRotation.z));

            Vec3 direction = tmp.getTranslation();
            
            Vec3 locationInShip = engine.getComponent().getLocationInShip();
            
            Vec3 rotAxis = locationInShip.cross(direction);
            
            float alpha = 0;
            
            if (Math.abs(rotAxis.x) > 0.001 && Math.abs(aim.x) > 0.001)
            {
                alpha +=  (engine.getMaxThrust() - engine.getMinThrust()) * aim.x  * (rotAxis.x > 0 ? 1: -1);
            }
            
            if (Math.abs(rotAxis.y) > 0.001  && Math.abs(aim.y) > 0.001)
            {
                alpha += (engine.getMaxThrust() - engine.getMinThrust()) * aim.y * (rotAxis.y > 0 ? 1: -1);
            }
            
            if (Math.abs(rotAxis.z) > 0.001  && Math.abs(aim.z) > 0.001)
            {
                alpha += (engine.getMaxThrust() - engine.getMinThrust()) * aim.z * (rotAxis.z > 0 ? 1: -1);
            }
            
            engine.setTargetThrust(engine.getTargetThrustInput() + alpha);
        }
        
        
        
//        for (LinearEngineCapacity engine : mEngines) {
//            engine.setTargetThrust(0);
//        }

    }

    private double computeSoftAngularCommand(double measure, double mMaxAngularSpeed, double mSoftModeAngularLimit, double command) {
        double result;
        if (measure - (command * mMaxAngularSpeed) < -mSoftModeAngularLimit)
        {
        result = -1;
        }
        else if (measure - (command * mMaxAngularSpeed) > mSoftModeAngularLimit)
        {
        result = 1;
        }
        else
        {
        result = (1.0f / mSoftModeAngularLimit) * (measure - (command * mMaxAngularSpeed));
        }
        return result;
    }
}
