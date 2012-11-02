package com.irr310.server.ai;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.system.DefaultSystemEventVisitor;
import com.irr310.common.event.system.SystemEvent;
import com.irr310.common.world.system.Ship;

public class AIEngine extends FramerateEngine<SystemEvent> {

    List<AIProcessor> activeProcessors = new CopyOnWriteArrayList<AIProcessor>();
    List<Ship> activeShips = new CopyOnWriteArrayList<Ship>();
    List<AIProcessor> pendingProcessors = new CopyOnWriteArrayList<AIProcessor>();

    public AIEngine() {
    }

    @Override
    protected void processEvent(SystemEvent e) {
        e.accept(new AIEngineEventVisitor());
    }

    @Override
    protected void frame() {
        //TODO remove procesoir for destroyed ships
        for(AIProcessor processor: activeProcessors) {
            processor.process();
        }
    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onInit() {
        
    }

    @Override
    protected void onEnd() {
        
    }

    private final class AIEngineEventVisitor extends DefaultSystemEventVisitor {

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
    }

    private void createAI(Ship ship) {
        AIProcessor processor = new AISucideProcessor(ship);
        if(activeShips.contains(ship)) {
            activeProcessors.add(processor);
        } else {
            pendingProcessors.add(processor);
        }
    }

}
