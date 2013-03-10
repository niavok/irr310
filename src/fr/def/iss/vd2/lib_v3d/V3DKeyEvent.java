package fr.def.iss.vd2.lib_v3d;

public class V3DKeyEvent extends V3DInputEvent {

    public enum KeyAction {
        KEY_PRESSED, KEY_RELEASED
    }

    private KeyAction action;
    private final String character;
    private final int keyCode;
    
    public V3DKeyEvent(KeyAction action, int keyCode, String character) {
        this.action = action;
        this.keyCode = keyCode;
        this.character = character;
        
    }

    public KeyAction getAction() {
        return action;
    }
    
    public String getCharacter() {
        return character;
    }
    
    public int getKeyCode() {
        return keyCode;
    }

}
