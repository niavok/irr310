package fr.def.iss.vd2.lib_v3d;


public class V3DControllerEvent extends V3DInputEvent {

    public enum ControllerAction {
        AXIS_MOVED, BUTTON_PRESSED, BUTTON_RELEASED 
    }
    
    private ControllerAction mAction;
    private final String mName;
    private final int mIndex;
    private final int mMouseX;
    private final int mMouseY;
    private final ControllerState mState;
    
    public V3DControllerEvent(ControllerAction action, String name, int index, ControllerState state, int mouseX, int mouseY) {
        super();
        mAction = action;
        mName = name;
        mIndex = index;
        mState = state;
        mMouseX = mouseX;
        mMouseY = mouseY;
    }
    public ControllerAction getAction() {
        return mAction;
    }
    public String getName() {
        return mName;
    }
    public int getIndex() {
        return mIndex;
    }
    
    public int getMouseX() {
        return mMouseX;
    }
    
    public int getMouseY() {
        return mMouseY;
    }
    public float getAxisValue(int i) {
        return mState.mAxisValues[i];
    }
    
    public ControllerState getState() {
        return mState;
    }
    
    public static class ControllerState {
        public ControllerState(int axisCount, int buttonCount) {
            mAxisValues = new float[axisCount];
            mDeadZoneValues = new float[axisCount];
            mButtonValues = new boolean[buttonCount];
        }
        
        public float mAxisValues[];
        public float mDeadZoneValues[];
        public boolean[] mButtonValues;

    }
    
}
