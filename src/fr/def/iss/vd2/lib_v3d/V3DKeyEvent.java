package fr.def.iss.vd2.lib_v3d;

import org.lwjgl.input.Keyboard;

public class V3DKeyEvent extends V3DInputEvent {

    public enum KeyAction {
        KEY_PRESSED, KEY_RELEASED
    }
    
    static public int KEY_RETURN =  Keyboard.KEY_RETURN;
    static public int KEY_ESCAPE =  Keyboard.KEY_ESCAPE;

    private KeyAction action;
    private final String character;
    private final int keyCode;
    private final int mouseX;
    private final int mouseY;

    public V3DKeyEvent(KeyAction action, int keyCode, String character, int mouseX, int mouseY) {
        this.action = action;
        this.keyCode = keyCode;
        this.character = character;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
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

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

}
