package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Part;


//
//public class ExplosionFiredEvent extends EngineEvent {
//
//    private final Vec3 location;
//    private Part source;
//    private final double armorPenetration;
//    private final double explosionBlast;
//    private final double explosionRadius;
//    private final double explosionDamage;
//
//    public ExplosionFiredEvent(Part source, Vec3 location, double armorPenetration, double explosionBlast, double explosionRadius, double explosionDamage) {
//        this.source = source;
//        this.location = location;
//        this.armorPenetration = armorPenetration;
//        this.explosionBlast = explosionBlast;
//        this.explosionRadius = explosionRadius;
//        this.explosionDamage = explosionDamage;
//    }
//
//    @Override
//    public void accept(EngineEventVisitor visitor) {
//        visitor.visit(this);
//    }
//    
//    public Vec3 getLocation() {
//        return location;
//    }
//    
//    public Part getSource() {
//        return source;
//    }
//    
//    public double getArmorPenetration() {
//        return armorPenetration;
//    }
//    
//    public double getExplosionBlast() {
//        return explosionBlast;
//    }
//    
//    public double getExplosionRadius() {
//        return explosionRadius;
//    }
//    
//    public double getExplosionDamage() {
//        return explosionDamage;
//    }
//}
