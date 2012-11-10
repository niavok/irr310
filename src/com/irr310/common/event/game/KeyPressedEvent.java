package com.irr310.common.event.game;

import com.irr310.common.event.EngineEventVisitor;


public class KeyPressedEvent extends GameEvent {

    private final int keyCode;
    private final String character;

    public KeyPressedEvent(int keyCode, String character) {
        this.keyCode = keyCode;
        this.character = character;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }

    public int getKeyCode() {
        return keyCode;
    }
    
    public String getCharacter() {
        return character;
    }
}
