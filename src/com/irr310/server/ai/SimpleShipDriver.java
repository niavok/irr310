package com.irr310.server.ai;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Log;
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
    }

    @Override
    public void update(float seconds) {

        double currentSpeedY = mKernel.getFirstPart().getLinearSpeed().dot(new Vec3(0, 1, 0).rotate(mKernel.getFirstPart().getTransform()));

        double currentEnergy = 0.5 * mTotalMass * currentSpeedY * currentSpeedY * (currentSpeedY > 0 ? 1 : -1); 
        
        double targetEnergy = 0.5 * mTotalMass * mLinearVelocityCommand.getY() * mLinearVelocityCommand.getY() * (mLinearVelocityCommand.getY() > 0 ? 1 : -1);
        
        double energyDelta = targetEnergy - currentEnergy; 
        
        double energyToZero = 0;
        
        for (LinearEngineCapacity engine : mEngines) {
            double thrustSign = (engine.getCurrentThrust() > 0 ? 1 : -1);
            energyToZero += thrustSign * 0.5 * engine.getCurrentThrust() * engine.getCurrentThrust() / engine.getVariationSpeed();
        }
        
        double e = Math.abs(targetEnergy / ENERGY_EPSILON) + 0.1;
        
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
        
//        for (LinearEngineCapacity engine : mEngines) {
//            engine.setTargetThrust(0);
//        }

    }
}
