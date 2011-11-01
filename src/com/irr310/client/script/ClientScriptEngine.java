package com.irr310.client.script;


import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.server.Duration;

public class ClientScriptEngine extends FramerateEngine {

    ScriptContext scriptContext;
    
    public ClientScriptEngine() {
        framerate = new Duration(15000000);
        
    }

    @Override
    protected void frame() {
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new ClientScriptEngineEventVisitor());
    }

    private final class ClientScriptEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping game engine");
            setRunning(false);
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
        public void visit(KeyPressedEvent event) {
            transmitKeyPressed(event.getKeyCode());
        }

    }

    private void transmitKeyPressed(int keyCode) {
        
        Object[] args = new Object[1];
        args[0] = keyCode;
        scriptContext.callFunction("onKeyPressed", args);
        
        
    }

    @Override
    protected void init() {
        scriptContext = new ScriptContext();
    }

    @Override
    protected void end() {
    }

}
