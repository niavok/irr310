package com.irr310.common.event.game;

import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;


public class KeyEvent extends GameEvent {

    private final V3DKeyEvent keyEvent;

    public KeyEvent(V3DKeyEvent event) {
        this.keyEvent = event;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }
    
    public V3DKeyEvent getKeyEvent() {
        return keyEvent;
    }
}
