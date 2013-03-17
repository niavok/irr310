package fr.def.iss.vd2.lib_v3d;

public class V3DMouseEvent extends V3DInputEvent {

    public enum Action {
        MOUSE_DRAGGED, MOUSE_MOVED, MOUSE_CLICKED, MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_WHEEL_DOWN, MOUSE_WHEEL_UP,
    }

    private Action action;

    private final int button;
    private final int y;
    private final int x;

    public V3DMouseEvent(Action action, int x, int y, int button) {
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
    
    public V3DMouseEvent relativeTo(int xOffset, int yOffset) {
        return new V3DMouseEvent(action, x - xOffset, y - yOffset, button);
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
