package com.irr310.client.navigation;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.QuitGameEvent;

public class NavigationEngine extends EventEngine {

    private enum NavigationMode {
        MAIN_MENU
        
    }

    public NavigationEngine() {
    }
   
    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new NavigationEngineEventVisitor());
    }

    private final class NavigationEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping network engine");
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
