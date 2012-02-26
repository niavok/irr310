package com.irr310.server;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.Game;
import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.engine.RayResultDescriptor;
import com.irr310.common.event.AddShipEvent;
import com.irr310.common.event.AddWorldObjectEvent;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent.Reason;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.DamageEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.GameOverEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.world.CelestialObject;
import com.irr310.common.world.Component;
import com.irr310.common.world.DamageType;
import com.irr310.common.world.Monolith;
import com.irr310.common.world.Part;
import com.irr310.common.world.Ship;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.GunCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.common.world.capacity.controller.GunController;
import com.irr310.common.world.capacity.controller.LinearEngineController;
import com.irr310.server.game.ShipFactory;

public class ServerGameEngine extends FramerateEngine {

    private static final double WORLD_SIZE = 1000;
    private List<CapacityController> capacityControllers;

    public ServerGameEngine() {
        capacityControllers = new ArrayList<CapacityController>();
        framerate = new Duration(15000000);
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new GameEngineEventVisitor());
    }

    @Override
    protected void frame() {
        for (CapacityController controller : capacityControllers) {
            controller.update(framerate.getSeconds());
        }
        
        
        // Check world leave
        for(Part part : Game.getInstance().getWorld().getParts()) {
            if(part.getTransform().getTranslation().length() > WORLD_SIZE) {
                
                
                
                if(part.getParentObject() instanceof CelestialObject) {
                    CelestialObject object = (CelestialObject) part.getParentObject();
                    Game.getInstance().getWorld().removeCelestialObject(object, Reason.LEAVE_OUT_WORLD);
                }
            }
        }
        
        
        
        
        
    }

    private final class GameEngineEventVisitor extends DefaultEngineEventVisitor {

        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping game engine");
            setRunning(false);
        }

        @Override
        public void visit(AddWorldObjectEvent event) {
            Component o = null;

            switch (event.getType()) {
                case CAMERA:
                    o = new Component(GameServer.pickNewId(),"camera");
                    break;
                case LINEAR_ENGINE:
                    o = new Component(GameServer.pickNewId(), "camera");
                    break;
            }

            if (event.getPosition() != null) {
                o.changeTranslation(event.getPosition());
            }

            /*
             * if(event.getRotation() != null) {
             * o.getRotation().set(event.getRotation()); }
             */

            if (event.getLinearSpeed() != null) {
                o.changeLinearSpeed(event.getLinearSpeed());
            }

            if (event.getRotationSpeed() != null) {
                o.changeRotationSpeed(event.getRotationSpeed());
            }

            o.setName(event.getName());

            //Game.getInstance().getWorld().addObject(o);
        }

        @Override
        public void visit(AddShipEvent event) {
            Ship ship = null;

            switch (event.getType()) {
                case SIMPLE:
                    ship = ShipFactory.createSimpleShip();
                    ship.setOwner(event.getOwner());
                    break;
                case SIMPLE_FIGHTER:
                    ship = ShipFactory.createSimpleFighter();
                    ship.setOwner(event.getOwner());
                    break;
            }

            Game.getInstance().getWorld().addShip(ship, event.getPosition());
        }

        @Override
        public void visit(WorldShipAddedEvent event) {
            Ship ship = event.getShip();

            for (Component component : ship.getComponents()) {
                for (Capacity capacity : component.getCapacities()) {
                    if (capacity instanceof LinearEngineCapacity) {
                        addCapacityController(new LinearEngineController(component, (LinearEngineCapacity) capacity));
                    }
                    if (capacity instanceof GunCapacity) {
                        addCapacityController(new GunController(component, (GunCapacity) capacity));
                    }
                }
            }

        }
        
        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
        }

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);
        }
        
        @Override
        public void visit(CollisionEvent event) {
            CollisionDescriptor collisionDescriptor = event.getCollisionDescriptor();
            processCollision(collisionDescriptor.getPartA(), collisionDescriptor.getImpulse());
            processCollision(collisionDescriptor.getPartB(), collisionDescriptor.getImpulse());
        }
        
        @Override
        public void visit(BulletFiredEvent event) {

            RayResultDescriptor rayTest = Game.getInstance().getPhysicEngine().rayTest(event.getFrom(), event.getTo());
            if(rayTest != null) {
                // damage = (1-rangePercent^3)
                double damage = event.getDamage()*(1- Math.pow(rayTest.getHitFraction(), 3));
                applyDamage(rayTest.getPart(), damage, event.getDamageType());
            }
        }

        
        @Override
        public void visit(CelestialObjectRemovedEvent event) {
            if(event.getObject() instanceof Monolith) {
                Game.getInstance().sendToAll(new GameOverEvent("The monolith is destroyed"));
            }
        }
    }
    
    private void processCollision(Part part, float impulse) {
        applyDamage(part, impulse, DamageType.PHYSICAL);
    }
    
    private void applyDamage(Part target, double damage, DamageType damageType) {
        WorldObject parentObject = target.getParentObject();
        
        double effectiveDamage = damage * (1.0 - parentObject.getPhysicalResistance());
        
        if(effectiveDamage == 0) {
            return;
        }
        
        double newDurablility = parentObject.getDurability();
        newDurablility -= effectiveDamage;
        if(newDurablility < 0) {
            newDurablility = 0;
        }
        
        parentObject.setDurability(newDurablility);
        
        //TODO: extras damage transmission 
        
        Game.getInstance().sendToAll(new DamageEvent(target, effectiveDamage, damageType));

        if(newDurablility == 0) {
            if(parentObject instanceof CelestialObject) {
                Game.getInstance().getWorld().removeCelestialObject((CelestialObject) parentObject, Reason.DESTROYED);
            }
        }
        
    }
    
    private void addCapacityController(CapacityController controller) {
        capacityControllers.add(controller);
    }

    @Override
    protected void init() {
        // Create the world
    }

    @Override
    protected void end() {
    }

}
