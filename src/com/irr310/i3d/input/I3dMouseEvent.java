package com.irr310.i3d.input;

import fr.def.iss.vd2.lib_v3d.V3DInputEvent;

public class I3dMouseEvent extends V3DInputEvent {

    public enum Action {
        MOUSE_DRAGGED, MOUSE_MOVED, MOUSE_CLICKED, MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_WHEEL_DOWN, MOUSE_WHEEL_UP,
    }

    private Action action;

    private final int button;
    private final int y;
    private final int x;

    private final int clickCount;

    private I3dMouseEvent mParentEvent;
    
    public I3dMouseEvent(Action action, int x, int y, int button, int clickCount) {
        this.action = action;
        this.x = x;
        this.y = y;
        this.button = button;
        this.clickCount = clickCount;
        mParentEvent = null;
    }
    
    public void setParentEvent(I3dMouseEvent parentEvent) {
        mParentEvent = parentEvent;
    }
    
    public I3dMouseEvent getParentEvent() {
        return mParentEvent;
    }
    
    public I3dMouseEvent getRootEvent() {
        if(mParentEvent == null) {
            return this;
        } else {
            return mParentEvent.getRootEvent();
        }
    }

    public Action getAction() {
        return action;
    }
    
    public int getClickCount() {
        return clickCount;
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
    
    public I3dMouseEvent relativeTo(int xOffset, int yOffset) {
        I3dMouseEvent mouseEvent = new I3dMouseEvent(action, x - xOffset, y - yOffset, button, clickCount);
        mouseEvent.setParentEvent(this);
        return mouseEvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new  StringBuilder();
        
        sb.append("V3DMouseEvent[Action=");
        sb.append(action.toString());
        sb.append(";X=");
        sb.append(x);
        sb.append(";Y=");
        sb.append(y);
        sb.append(";Button=");
        sb.append(button);
        sb.append("(");
        sb.append("(");
        sb.append(")]");
        
        
        return sb.toString();
    }
    
    public static String getButtonName(int button) {
        String buttonName = "Unknown";
        switch(button) {
            case 1:
                buttonName = "Left";
                break;
            case 2:
                buttonName = "Right";
                break;
            case 3:
                buttonName = "Middle";
                break;
        }
        
        return buttonName;
    }
    // public MouseEvent toAwtEvent(Component component) {
    // return new MouseEvent(component, MouseEvent.MOUSE_PRESSED ,
    // System.currentTimeMillis(), 0, x, y, x, y, 0, false, button);
    // }

}
