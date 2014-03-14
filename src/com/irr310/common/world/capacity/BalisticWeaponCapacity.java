package com.irr310.common.world.capacity;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.system.DamageDescriptor;

public class BalisticWeaponCapacity extends Capacity {

    public boolean fire;
    public double damage;
    public double range;
    // Size of the dispertion cube at 1000m
    public double accuracy;

    // Bullet count to overheat
    public double heatingSpeed;

    // Time to cool to zero
    public double coolingSpeed;

    // Bullet par second
    public double firerate;

    // Armor penetration
    public double armorPenetration;

    // Offset of barrels
    public List<Vec3> barrels;

    public DamageDescriptor.DamageType damageType;

    public BalisticWeaponCapacity(World world, long id) {
        super(world, id);
        fire = false;
        damage = 50;
        range = 1000;
        accuracy = 10;
        coolingSpeed = 10;
        heatingSpeed = 5;
        firerate = 1;
        armorPenetration = 0;
        barrels = new ArrayList<Vec3>();

        damageType = DamageDescriptor.DamageType.PHYSICAL;
    }
}
