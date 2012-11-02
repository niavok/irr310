package com.irr310.client.input;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.GameOverEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.KeyReleasedEvent;
import com.irr310.common.event.MouseEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
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
            
//            if(Keyboard.getEventKey() == Keyboard.KEY_TAB && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
//                Game.getInstance().sendToAll(new MinimizeWindowEvent());
//                return true;
//            }
            
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
                if(cheatString.toLowerCase().equals("gold")) {
                    LoginManager.localPlayer.giveMoney(10000);
                    Log.log("Cheat - 10000 $");
                }
                if(cheatString.toLowerCase().equals("glittering prizes")) {
                    LoginManager.localPlayer.giveMoney(500000);
                    Log.log("Cheat - 500000 $");
                }
//                if(cheatString.toLowerCase().equals("armor")) {
//                    Monolith monolith = null;
//                    for (CelestialObject object : Game.getInstance().getWorld().getCelestialsObjects()) {
//                        if (object instanceof Monolith) {
//                            monolith = (Monolith) object;
//                            break;
//                        }
//                    }
//                    if (monolith != null) {
//                        if(monolith.getPhysicalResistance() < 1) {
//                            Log.log("Cheat - Monolith invicibility activated");
//                            monolith.setPhysicalResistance(100);
//                            monolith.setHeatResistance(100);
//                        } else {
//                            Log.log("Cheat - Monolith invicibility desactivated");
//                            monolith.setPhysicalResistance(0.5);
//                            monolith.setHeatResistance(0);
//                        }
//                        
//                    }
//                    
//                }
                if(cheatString.toLowerCase().equals("repair")) {
                    Ship ship = LoginManager.getLocalPlayer().getShipList().get(0);
                    for(Component component : ship.getComponents()) {
                        component.setDurability(component.getDurabilityMax()) ; 
                    }
                }
                if(cheatString.toLowerCase().equals("power+")) {
                    Ship ship = LoginManager.getLocalPlayer().getShipList().get(0);
                    for(Component component : ship.getComponents()) {
                        
                        List<LinearEngineCapacity> engines = component.getCapacitiesByClass(LinearEngineCapacity.class);
                        for (LinearEngineCapacity engineCapacity : engines) {
                            engineCapacity.theoricalMaxThrust *=2;
                            engineCapacity.theoricalMinThrust *=2;
                        }
                    }
                }
                if(cheatString.toLowerCase().equals("looser")) {
                    Game.getInstance().sendToAll(new GameOverEvent("Cheat"));
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
