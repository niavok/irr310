package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.DamageDescriptor;
import com.irr310.common.world.Part;



public class BulletFiredEvent extends EngineEvent {

    private final DamageDescriptor damage;
    private final Part source;
    private final Vec3 from;
    private final Vec3 to;
    private final double range;
    

    public BulletFiredEvent(Part source, DamageDescriptor damage, double range, Vec3 from, Vec3 to) {
        this.source = source;
        this.range = range;
        this.damage = damage;
        this.from = from;
        this.to = to;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }
    
    public DamageDescriptor getDamage() {
        return damage;
    }
    
    public Vec3 getTo() {
        return to;
    }
    
    public double getRange() {
        return range;
    }
    
    public Vec3 getFrom() {
        return from;
    }
    
    public Part getSource() {
        return source;
    }
}
