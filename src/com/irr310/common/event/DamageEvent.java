package com.irr310.common.event;

import com.irr310.common.world.DamageType;
import com.irr310.common.world.Part;



public class DamageEvent extends EngineEvent {

    private final double damage;
    private final Part target;
    private final DamageType damageType;

    public DamageEvent(Part target, double damage, DamageType damageType) {
        this.target = target;
        this.damage = damage;
        this.damageType = damageType;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }
    
    public double getDamage() {
        return damage;
    }
    
    public DamageType getDamageType() {
        return damageType;
    }
    
    public Part getTarget() {
        return target;
    }
}
