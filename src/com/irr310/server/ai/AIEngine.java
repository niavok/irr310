package com.irr310.server.ai;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.BindAIEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.world.Ship;

public class AIEngine extends FramerateEngine {

    List<AIProcessor> processors = new CopyOnWriteArrayList<AIProcessor>();

    public AIEngine() {
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new AIEngineEventVisitor());
    }

    @Override
    protected void frame() {
        for(AIProcessor processor: processors) {
            processor.process();
        }
    }

    @Override
    protected void init() {
        
    }

    @Override
    protected void end() {
        
    }

    private final class AIEngineEventVisitor extends DefaultEngineEventVisitor {

        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping ai engine");
            setRunning(false);
        }
        
        @Override
        public void visit(BindAIEvent event) {
            createAI(event.getShip());
        }
        
        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
        }

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);
        }
    }

    private void createAI(Ship ship) {
        processors.add(new AISucideProcessor(ship));
    }

}
