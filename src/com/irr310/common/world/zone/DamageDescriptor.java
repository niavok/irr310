package com.irr310.common.world.zone;

public class DamageDescriptor {
    public enum DamageType {
        PHYSICAL,
        HEAT,
        DECAY,
    }
    
    public enum DamageCause {
        BULLET,
        EXPLOSION,
        COLLISION,
        DECAY,
    }

    private double baseDamage;
    public final double armorPenetration;
    public final DamageType type;
    private double effectiveDamage;
    private double weaponBaseDamage;
    public final DamageCause cause;
    
    public DamageDescriptor(DamageType type, double armorPenetration, DamageCause cause) {
        this.type = type;
        this.armorPenetration = armorPenetration;
        this.cause = cause;
        weaponBaseDamage = -1;
        baseDamage = -1;
        effectiveDamage = -1;
    }

    public void setEffectiveDamage(double effectiveDamage) {
        this.effectiveDamage = effectiveDamage;
        
    }
    
    public double getWeaponBaseDamage() {
        return weaponBaseDamage;
    }
    
    public void setWeaponBaseDamage(double weaponBaseDamage) {
        this.weaponBaseDamage = weaponBaseDamage;
    }
    
    public double getEffectiveDamage() {
        return effectiveDamage;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }
    
    public double getBaseDamage() {
        return baseDamage;
    }
    
    public DamageCause getCause() {
        return cause;
    }
    
    public DamageType getType() {
        return type;
    }
}