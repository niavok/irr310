package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.DamageDescriptor;
import com.irr310.common.world.system.Part;



public class DamageEvent extends EngineEvent {

    private final Part target;
    private final DamageDescriptor damage;
    private final Vec3 impact;

    public DamageEvent(Part target,DamageDescriptor damage, Vec3 impact) {
        this.target = target;
        this.damage = damage;
        this.impact = impact;
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
    
    public Vec3 getImpact() {
        return impact;
    }
}
