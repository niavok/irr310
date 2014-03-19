package com.irr310.common.world.capacity;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.DamageDescriptor;
import com.irr310.common.world.system.WorldSystem;

public class RocketWeaponCapacity extends Capacity {

    // Weapon management
    public boolean fire;
    // Delay before refill
    public double cooldown;
    // Used to avoid chained explosions
    public double firerate;
    // Offset of barrels
    public List<Vec3> barrels;

    // Navigation
    public double securityTimeout;
    public double thrust;
    public double damping;
    public double thrustDuration;
    public double stability;
    public double hitPoint;

    // Damage
    public double explosionDamage;
    public double explosionRadius;
    public double explosionBlast;
    public double armorPenetration;
    public DamageDescriptor.DamageType damageType;

    public RocketWeaponCapacity(WorldSystem worldSystem, long id, Component component) {
        super(worldSystem, id, component);
        fire = false;
        barrels = new ArrayList<Vec3>();
        cooldown = 5;
        firerate = 5;

        securityTimeout = 15;
        thrust = 3;
        damping = 0.1;
        thrustDuration = 2;
        stability = 0.005;
        hitPoint = 10;

        explosionDamage = 50;
        explosionRadius = 10;
        explosionBlast = 10;
        armorPenetration = 5;
        damageType = DamageDescriptor.DamageType.HEAT;
    }
}
