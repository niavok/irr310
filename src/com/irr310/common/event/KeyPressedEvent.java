package com.irr310.common.event;

import java.io.File;

public class KeyPressedEvent extends EngineEvent {

    private final int keyCode;

    public KeyPressedEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public int getKeyCode() {
        return keyCode;
    }
}
