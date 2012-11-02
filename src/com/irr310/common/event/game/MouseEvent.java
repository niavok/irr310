package com.irr310.common.event.game;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;


public class MouseEvent extends GameEvent {


    private final V3DMouseEvent mouseEvent;

    public MouseEvent(V3DMouseEvent event) {
        this.mouseEvent = event;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }

    
    public V3DMouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
