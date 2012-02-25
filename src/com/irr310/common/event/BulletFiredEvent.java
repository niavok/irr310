package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.DamageType;
import com.irr310.common.world.Part;



public class BulletFiredEvent extends EngineEvent {

    private final double damage;
    private final DamageType damageType;
    private final Part source;
    private final Vec3 from;
    private final Vec3 to;
    private final double range;
    

    public BulletFiredEvent(Part source, double damage, double range, DamageType damageType, Vec3 from, Vec3 to) {
        this.source = source;
        this.range = range;
        this.damage = damage;
        this.damageType = damageType;
        this.from = from;
        this.to = to;
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
