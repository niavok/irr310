package com.irr310.server.ai;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.engine.Engine;
import com.irr310.common.world.system.Ship;
import com.irr310.server.Time.Timestamp;

public class AIEngine implements Engine {

    List<AIProcessor> activeProcessors = new CopyOnWriteArrayList<AIProcessor>();
    List<Ship> activeShips = new CopyOnWriteArrayList<Ship>();
    List<AIProcessor> pendingProcessors = new CopyOnWriteArrayList<AIProcessor>();

    public AIEngine() {
    }

    
    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        
    }

    @Override
    public void stop() {
        
    }
    
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void tick(Timestamp time) {
        //TODO remove procesoir for destroyed ships
        for(AIProcessor processor: activeProcessors) {
            processor.process();
        }
    }

//    private final class AIEngineEventVisitor extends DefaultSystemEventVisitor {

//        @Override
//        public void visit(QuitGameEvent event) {
//            System.out.println("stopping ai engine");
//            setRunning(false);
//        }
//        
//        @Override
//        public void visit(BindAIEvent event) {
//            createAI(event.getShip());
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
//        public void visit(WorldShipAddedEvent event) {
//            activeShips.add(event.getShip());
//            for(AIProcessor processor: pendingProcessors) {
//                if(processor.getShip() == event.getShip()) {
//                    pendingProcessors.remove(processor);
//                    activeProcessors.add(processor);
//                }
//            }
//        }
//        
//        @Override
//        public void visit(WorldShipRemovedEvent event) {
//            activeShips.remove(event.getShip());
//            for(AIProcessor processor: activeProcessors) {
//                if(processor.getShip() == event.getShip()) {
//                    activeProcessors.remove(processor);
//                }
//            }
//        }
//    }

    private void createAI(Ship ship) {
        AIProcessor processor = new AISucideProcessor(ship);
        if(activeShips.contains(ship)) {
            activeProcessors.add(processor);
        } else {
            pendingProcessors.add(processor);
        }
    }

}
