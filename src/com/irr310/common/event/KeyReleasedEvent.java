package com.irr310.common.event;


public class KeyReleasedEvent extends EngineEvent {

    private final int keyCode;
    private final String character;

    public KeyReleasedEvent(int keyCode, String character) {
        this.keyCode = keyCode;
        this.character = character;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public int getKeyCode() {
        return keyCode;
    }
    
    public String getCharacter() {
        return character;
    }
}
