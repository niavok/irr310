package fr.def.iss.vd2.lib_v3d;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;


public class V3DMouseEvent extends V3DInputEvent {

    
    public enum Action {
        MOUSE_DRAGGED, MOUSE_MOVED, MOUSE_CLICKED, MOUSE_PRESSED, MOUSE_RELEASED,
    }

    private Action action;
    
    private final int button;
    private final int y;
    private final int x;
    
    public V3DMouseEvent(Action action, int x, int y, int button)  {
        this.action = action;
        this.x = x;
        this.y = y;
        this.button = button;
        
    }

    public Action getAction() {
        return action;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getButton() {
        return button;
    }

//    public MouseEvent toAwtEvent(Component component) {
//        return new MouseEvent(component, MouseEvent.MOUSE_PRESSED , System.currentTimeMillis(), 0, x, y, x, y, 0, false, button);
//    }

    

}
