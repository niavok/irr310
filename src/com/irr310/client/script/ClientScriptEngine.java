package com.irr310.client.script;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.QuitGameEvent;

public class ClientScriptEngine extends EventEngine {

    public ClientScriptEngine() {

    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new ScriptEventVisitor());
    }

    private final class ScriptEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping network engine");
            setRunning(false);

        }
        
        @Override
        public void visit(KeyPressedEvent event) {
            transmitKeyPressed(event.getKeyCode());
        }

        

    }

    private void transmitKeyPressed(int keyCode) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    protected void init() {
    }

    @Override
    protected void end() {
    }
}
