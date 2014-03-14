package com.irr310.client.game;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.engine.Engine;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.server.Duration;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

public class ClientSystemEngine implements Engine {

    private List<CapacityController> capacityControllers;
    private Time mLastTime;
    
    public ClientSystemEngine() {
        capacityControllers = new ArrayList<CapacityController>();
    }

    @Override
    public void init() {
    }

    @Override
    public void stop() {
    }
    
    @Override
    public void start() {
        mLastTime = Time.getGameTime();
    }
    
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void tick(Timestamp time) {
        Duration duration = mLastTime.durationTo(time.getGameTime());
        for (CapacityController controller : capacityControllers) {
            controller.update(duration.getSeconds());
        }
        mLastTime = time.getGameTime();
    }

//    private final class ClientSystemEngineEventVisitor extends DefaultSystemEventVisitor {
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

        
        
//    }

//    private void addCapacityController(CapacityController controller) {
//        capacityControllers.add(controller);
//    }

}
