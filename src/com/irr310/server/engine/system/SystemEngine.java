package com.irr310.server.engine.system;

import java.util.Random;

import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.engine.Engine;
import com.irr310.common.engine.Observable;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.item.ShipItem;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.Time.Timestamp;
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.game.ShipFactory;

public class SystemEngine implements Engine {

    private WorldSystem mSystem;
//    private WorldEngine mWorldEngine;
    private ShipFactory mShipFactory;
    private PhysicEngine mPhysicEngine;

    public SystemEngine(WorldEngine worldEngine, WorldSystem system) {
//        this.mWorldEngine = worldEngine;
        this.mSystem = system;
        mShipFactory = new ShipFactory(system);
        
        mPhysicEngine = new PhysicEngine(this);
    }
    
    @Override
    public void init() {
        mPhysicEngine.init();
    }
    
    @Override
    public void start() {
        mPhysicEngine.start();
    }
    
    @Override
    public void stop() {
        mPhysicEngine.stop();
    }

    @Override
    public void destroy() {
        mPhysicEngine.destroy();
    }
    
    @Override
    public void tick(Timestamp time) {
        mPhysicEngine.tick(time);
    }
    
    public PhysicEngine getPhysicEngine() {
        return mPhysicEngine;
    }
    
    public WorldSystem getSystem() {
        return mSystem;
    }

    public void deployShip(ShipItem shipItem, Nexus nexus) {

        Ship ship = mShipFactory.createShip(shipItem);
        
        // TODO make deterministe random
//        Random random = new Random();
        
        TransformMatrix shipTransform = TransformMatrix.identity();
//        shipTransform.rotateX(random.nextDouble() * 360);        
//        shipTransform.rotateZ(random.nextDouble() * 360);
//        shipTransform.rotateY(random.nextDouble() * 360);
        shipTransform.translate(nexus.getLocation());

        mSystem.addShip(ship);
        
        notifyShipDeployed(ship, shipTransform);
    }

    public void notifyCollision(CollisionDescriptor collisionDescriptor) {
        
    }

    // Observers
    private Observable<SystemEngineObserver> mSystemEngineObservable = new Observable<SystemEngineObserver>();
    
    public Observable<SystemEngineObserver> getSystemEnginObservable() {
        return mSystemEngineObservable;
    }
    
    private void notifyShipDeployed(Ship ship, TransformMatrix shipTransform) {
        for(SystemEngineObserver observer : mSystemEngineObservable.getObservers()) {
            observer.onDeployShip(ship, shipTransform);
        }
    }

    
}
