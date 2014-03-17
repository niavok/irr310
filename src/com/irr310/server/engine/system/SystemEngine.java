package com.irr310.server.engine.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.engine.Engine;
import com.irr310.common.engine.Observable;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.common.world.item.ShipItem;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.Duration;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.game.ShipFactory;

public class SystemEngine implements Engine {

    private static final CapacityControllerFactory sCapacityControler = new CapacityControllerFactory();
    private WorldSystem mSystem;
//    private WorldEngine mWorldEngine;
    private ShipFactory mShipFactory;
    private PhysicEngine mPhysicEngine;
    private List<CapacityController> mCapacityControllers;
    private Time mLastTime;
    
    public SystemEngine(WorldEngine worldEngine, WorldSystem system) {
//        this.mWorldEngine = worldEngine;
        this.mSystem = system;
        mCapacityControllers = new ArrayList<CapacityController>();
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
        mLastTime = Time.now(true);
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
        
        Duration duration = mLastTime.durationTo(time.getGameTime());

        
        for (CapacityController controller : mCapacityControllers) {
            controller.update(duration.getSeconds());
        }
        
        mLastTime = time.getGameTime();
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
        
        // Create capacity listeners
        for(Component component : ship.getComponents()) {
            for(Capacity capacity : component.getCapacities()) {
                mCapacityControllers.add(sCapacityControler.createController(component, capacity));
            }
        }
        
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
