package com.irr310.common.world.zone;

public class RocketDescriptor {

    // Navigation
    public double disarmTimeout;
    public double thrust;
    public double damping;
    public double thrustDuration;
    public double stability;
    public double hitPoint;

    // Damage
    public double explosionRadius;
    public double explosionBlast;
    public double explosionDamage;
    public double armorPenetration;
    public DamageDescriptor.DamageType damageType;
    
    public RocketDescriptor() {
    }
}