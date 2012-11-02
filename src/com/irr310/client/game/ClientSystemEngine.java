package com.irr310.client.game;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.system.DefaultSystemEventVisitor;
import com.irr310.common.event.system.SystemEvent;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.server.Duration;

public class ClientSystemEngine extends FramerateEngine<SystemEvent> {

    private List<CapacityController> capacityControllers;
    
    public ClientSystemEngine() {
        capacityControllers = new ArrayList<CapacityController>();
        framerate = new Duration(15000000);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void frame() {
        for (CapacityController controller : capacityControllers) {
            controller.update(framerate.getSeconds());
        }
    }

    @Override
    protected void processEvent(SystemEvent e) {
        e.accept(new ClientSystemEngineEventVisitor());
    }

    private final class ClientSystemEngineEventVisitor extends DefaultSystemEventVisitor {
//        @Override
//        public void visit(QuitGameEvent event) {
//            System.out.println("stopping game engine");
//            setRunning(false);
//        }
//
//        @Override
//        public void visit(WorldShipAddedEvent event) {
//            Ship ship = event.getShip();
//
//            for (Component component : ship.getComponents()) {
//                for (Capacity capacity : component.getCapacities()) {
//                    if (capacity instanceof LinearEngineCapacity) {
//                        addCapacityController(new LinearEngineController(component, (LinearEngineCapacity) capacity));
//                    }
//                    if (capacity instanceof BalisticWeaponCapacity) {
//                        if(capacity.getName().equals("gun")) {
//                            addCapacityController(new GunController(component, (BalisticWeaponCapacity) capacity));
//                        } else if(capacity.getName().equals("shotgun")) { 
//                            addCapacityController(new ShotgunController(component, (BalisticWeaponCapacity) capacity));
//                        }
//                    }
//                }
//            }
//
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
//        
//        @Override
//        public void visit(GameOverEvent event) {
////            Game.getInstance().gameOver();
//        }

        
        
    }

//    private void addCapacityController(CapacityController controller) {
//        capacityControllers.add(controller);
//    }

    @Override
    protected void onInit() {
    }

    @Override
    protected void onEnd() {
    }

}
