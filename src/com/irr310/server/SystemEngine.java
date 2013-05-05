package com.irr310.server;

import java.util.Random;

import com.irr310.common.engine.EngineManager;
import com.irr310.common.engine.EventDispatcher;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.system.DefaultSystemEventVisitor;
import com.irr310.common.event.system.QuerySystemStateEvent;
import com.irr310.common.event.system.ShipDeployedSystemEvent;
import com.irr310.common.event.system.SystemEvent;
import com.irr310.common.event.system.SystemEventVisitor;
import com.irr310.common.event.system.SystemStateEvent;
import com.irr310.common.event.world.ActionDeployShipEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.ShipDeployedWorldEvent;
import com.irr310.common.event.world.WorldEvent;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.ShipItem;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.game.ShipFactory;

public class SystemEngine extends FramerateEngine<WorldEvent> implements EventDispatcher<SystemEventVisitor, SystemEvent> {

    private EngineManager<SystemEventVisitor, SystemEvent> engineManager;
    private SystemEngineWorldEventVisitor systemEngineVisitor;
    private WorldSystem system;
    private WorldEngine worldEngine;
    private ShipFactory shipFactory;

    public SystemEngine(WorldEngine worldEngine, WorldSystem system) {
        this.worldEngine = worldEngine;
        this.system = system;
        shipFactory = new ShipFactory(system);
        //        world = worldEngine.getWorld();
        engineManager = new EngineManager<SystemEventVisitor, SystemEvent>();
        engineManager.registerEventVisitor(new SystemEngineSystemEventVisitor());
        systemEngineVisitor = new SystemEngineWorldEventVisitor();
        
        
        
        
        
        
    }

    @Override
    protected void processEvent(WorldEvent e) {
        
        e.accept(systemEngineVisitor);
    }

    @Override
    protected void frame() {
    }

    @Override
    protected void onStart() {
        pause(false);
    }
    
    @Override
    protected void onInit() {
    }

    @Override
    protected void onEnd() {
        
    }

    private final class SystemEngineWorldEventVisitor extends DefaultWorldEventVisitor {

//        @Override
//        public void visit(QuitGameEvent event) {
//            java.lang.System.out.println("stopping system engine");
//            setRunning(false);
//        }
//        
//        @Override
//        public void visit(StartEngineEvent event) {
//            pause(false);
//        }
//
//        @Override
//        public void visit(PauseEngineEvent event) {
//            pause(true);
//        }
        
        @Override
        public void visit(ActionDeployShipEvent event) {
            Item item = system.getWorld().getItem(event.getItem());
            Nexus nexus = system.getNexus(event.getNexus());
            
            if(nexus != null && item instanceof ShipItem) {
                ShipItem shipItem = (ShipItem) item;
                
                Ship ship  = deployShip(shipItem, nexus);
                engineManager.sendToAll(new ShipDeployedSystemEvent(ship));
                
                worldEngine.sendToAll(new ShipDeployedWorldEvent(shipItem.toState()));
            }
        }
    }

    private final class SystemEngineSystemEventVisitor extends DefaultSystemEventVisitor {
        @Override
        public void visit(QuerySystemStateEvent event) {
            sendToAll(new SystemStateEvent(system.toState()));
        }
    }
    
    public PhysicEngine getPhysicEngine() {
        return null;
    }

    @Override
    public void sendToAll(SystemEvent event) {
        engineManager.sendToAll(event);
    }
    
    public void registerEventVisitor(SystemEventVisitor visitor) {
        engineManager.registerEventVisitor(visitor);
    }

    public void unregisterEventVisitor(SystemEventVisitor visitor) {
        engineManager.unregisterEventVisitor(visitor);
    }

    
    private Ship deployShip(ShipItem shipItem, Nexus nexus) {
        system.lock();
        Ship ship = shipFactory.createShip(shipItem);
        
        // TODO make deterministe random
        Random random = new Random();
        
        TransformMatrix shipTransform = TransformMatrix.identity();
        shipTransform.rotateX(random.nextDouble() * 360);        
        shipTransform.rotateZ(random.nextDouble() * 360);
        shipTransform.rotateY(random.nextDouble() * 360);
        shipTransform.translate(nexus.getLocation());

        system.addShip(ship, shipTransform);
        
        system.unlock();
        return ship;
    }

    public WorldSystem getSystem() {
        return system;
    }
   
}
