package com.irr310.client.script;

import org.lwjgl.input.Keyboard;

import com.irr310.client.GameClient;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.KeyReleasedEvent;
import com.irr310.common.event.MouseEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.PlayerAddedEvent;
import com.irr310.common.event.PlayerLoggedEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.ReloadUiEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldReadyEvent;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class ClientScriptEngine extends FramerateEngine {

    //ScriptContext scriptContext;
    ClientGuiDriver driver;
    
    public ClientScriptEngine() {
        framerate = new Duration(15000000);
        driver = new NoDriver();
    }

    @Override
    protected void frame() {
        Object[] args = new Object[1];
        args[0] = Time.now(true).getSeconds();
        //scriptContext.callFunction("onFrame", args);
        driver.frame();
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new ClientScriptEngineEventVisitor());
    }

    private final class ClientScriptEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping script engine");
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
            if (event.getKeyCode() == Keyboard.KEY_F12 && Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                System.out.println("Reload javascript");
                init();
                return;
            }
            transmitKeyPressed(event.getKeyCode(), event.getCharacter());
        }

        @Override
        public void visit(KeyReleasedEvent event) {
            transmitKeyReleased(event.getKeyCode(), event.getCharacter());
        }

        @Override
        public void visit(MouseEvent event) {
            transmitMouseEvent(event.getMouseEvent());
        }

        @Override
        public void visit(WorldReadyEvent event) {
            init();
        }
        @Override
        public void visit(ReloadUiEvent event) {
            init();
        }
        
        @Override
        public void visit(PlayerAddedEvent event) {
            driver = new FighterDriver();
            driver.init();
            GameClient.getInstance().getGraphicEngine().getRenderer().resetGui();
        }

    }

    private void transmitMouseEvent(V3DMouseEvent mouseEvent) {
        /*Object[] args = new Object[4];
        args[0] = mouseEvent.getAction().ordinal();
        args[1] = mouseEvent.getButton();
        args[2] = mouseEvent.getX();
        args[3] = mouseEvent.getY();
        scriptContext.callFunction("onMouseEvent", args);*/
        
        driver.mouseEvent(mouseEvent);
    }

    private void transmitKeyPressed(int keyCode, String character) {
        /*Object[] args = new Object[2];
        args[0] = keyCode;
        args[1] = character;
        scriptContext.callFunction("onKeyPressed", args);*/
        driver.keyPressed(keyCode, character);
    }

    private void transmitKeyReleased(int keyCode, String character) {
        /*Object[] args = new Object[2];
        args[0] = keyCode;
        args[1] = character;
        scriptContext.callFunction("onKeyReleased", args);*/
        driver.keyRelease(keyCode, character);
    }

    @Override
    public void init() {
        
        
        /*if (scriptContext != null) {
            scriptContext.close();
            
        }
        scriptContext = new ScriptContext();*/
        
    }

    @Override
    protected void end() {
    }

}
