package fr.def.iss.vd2.lib_v3d;


public class V3DInputEvent {
    private boolean isConsumed;
    
    public V3DInputEvent() {
        isConsumed = false;
    }
    
    public boolean isConsumed() {
        return isConsumed;
    }

    public void consume() {
        isConsumed = true;
    }
}
