package com.irr310.common.event;

import com.irr310.common.world.DamageDescriptor;
import com.irr310.common.world.Part;



public class DamageEvent extends EngineEvent {

    private final Part target;
    private final DamageDescriptor damage;

    public DamageEvent(Part target,DamageDescriptor damage) {
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }
    
    public DamageDescriptor getDamage() {
        return damage;
    }
    
    public Part getTarget() {
        return target;
    }
}
