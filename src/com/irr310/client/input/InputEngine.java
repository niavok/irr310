package com.irr310.client.input;

import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.KeyReleasedEvent;
import com.irr310.common.event.MinimizeWindowEvent;
import com.irr310.common.event.MouseEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class InputEngine extends FramerateEngine {

    private boolean dragging;
    private long[] pressTime;
    private String cheatString = "";
    
    public InputEngine() {
        framerate = new Duration(15000000);
        dragging = false;
        pressTime = new long[10];
        
    }

    @Override
    protected void frame() {
        
        try { 
        
        while (Keyboard.next()) {
            if(interceptSpecialKeys()) {
                continue;
            }
            
            
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventCharacter() == 0) {
                    Game.getInstance().sendToAll(new KeyPressedEvent(Keyboard.getEventKey(), ""));
                } else {
                    Game.getInstance().sendToAll(new KeyPressedEvent(Keyboard.getEventKey(), Character.toString(Keyboard.getEventCharacter())));
                }

            } else {
                if (Keyboard.getEventCharacter() == 0) {
                    Game.getInstance().sendToAll(new KeyReleasedEvent(Keyboard.getEventKey(), ""));
                } else {
                    Game.getInstance().sendToAll(new KeyReleasedEvent(Keyboard.getEventKey(), Character.toString(Keyboard.getEventCharacter())));
                }
            }
        }
        

        while (Mouse.next()) {
          

            if (Mouse.getEventButton() == -1) {
                if (dragging) {
                    // Drag
                    V3DMouseEvent mouseEvent = new V3DMouseEvent(Action.MOUSE_DRAGGED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1);
                    Game.getInstance().sendToAll(new MouseEvent(mouseEvent));
                } else {
                    // Move
                    V3DMouseEvent mouseEvent = new V3DMouseEvent(Action.MOUSE_MOVED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1);
                    Game.getInstance().sendToAll(new MouseEvent(mouseEvent));
                }

            } else {
                if (Mouse.getEventButtonState()) {
                    // Pressed
                    dragging = true;
                    pressTime[Mouse.getEventButton()] = Mouse.getEventNanoseconds();
                    V3DMouseEvent mouseEvent = new V3DMouseEvent(Action.MOUSE_PRESSED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1);
                    Game.getInstance().sendToAll(new MouseEvent(mouseEvent));
                } else {
                    // Released
                    dragging = false;
                    V3DMouseEvent mouseEvent = new V3DMouseEvent(Action.MOUSE_RELEASED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1);
                    Game.getInstance().sendToAll(new MouseEvent(mouseEvent));
                    if( Mouse.getEventNanoseconds()  - pressTime[Mouse.getEventButton()] < 500000000 ) {
                        mouseEvent = new V3DMouseEvent(Action.MOUSE_CLICKED, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton()+1);
                        Game.getInstance().sendToAll(new MouseEvent(mouseEvent));
                    }
                }

            }

        }
        
        } catch(IllegalStateException e) {
            System.err.println("IllegalStateException");
        }

    }

    private boolean interceptSpecialKeys() {
        if (Keyboard.getEventKeyState()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_F4 && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                Game.getInstance().sendToAll(new QuitGameEvent());
                return true;
            }
            
            if(Keyboard.getEventKey() == Keyboard.KEY_TAB && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                Game.getInstance().sendToAll(new MinimizeWindowEvent());
                return true;
            }
            
        }
        return false;
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new InputEngineEventVisitor());
    }

    private final class InputEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping input engine");
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
            // Cheats
            if(event.getKeyCode() == Keyboard.KEY_RETURN) {
                if(cheatString.toLowerCase().equals("glittering prizes") || cheatString.toLowerCase().equals("gold")) {
                    LoginManager.localPlayer.giveMoney(10000);
                }
                cheatString = "";
            } else if(event.getCharacter() != null) {
                cheatString += event.getCharacter();
            }
            
        }

    }

    @Override
    protected void init() {
        Mouse.setGrabbed(false);
    }

    @Override
    protected void end() {
    }

}
