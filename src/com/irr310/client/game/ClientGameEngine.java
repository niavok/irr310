package com.irr310.client.game;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.QuitGameEvent;

public class ClientGameEngine extends EventEngine {

    public ClientGameEngine() {
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new GameEngineEventVisitor());
    }

    private final class GameEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping game engine");
            setRunning(false);
        }
    }

    @Override
    protected void init() {
    }

    @Override
    protected void end() {
    }

}
