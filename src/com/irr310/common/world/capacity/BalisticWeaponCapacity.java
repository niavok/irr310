package com.irr310.common.world.capacity;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.state.CapacityState;
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

    @Override
    public CapacityState toState() {
        CapacityState view = new CapacityState();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.GUN.ordinal();

        view.pushBoolean(fire);
        view.pushDouble(damage);
        view.pushDouble(range);
        view.pushDouble(accuracy);
        view.pushDouble(coolingSpeed);
        view.pushDouble(heatingSpeed);
        view.pushDouble(firerate);
        view.pushDouble(armorPenetration);
        view.pushInteger(barrels.size());
        for (Vec3 barrel : barrels) {
            view.pushVect3(barrel);
        }

        view.pushInteger(damageType.ordinal());
        return view;
    }

    @Override
    public void fromState(CapacityState view) {
        setName(view.name);
        fire = view.popBoolean();
        damage = view.popDouble();
        range = view.popDouble();
        accuracy = view.popDouble();
        coolingSpeed = view.popDouble();
        heatingSpeed = view.popDouble();
        firerate = view.popDouble();
        armorPenetration = view.popDouble();

        int barrelCount = view.popInteger();
        for (int i = 0; i < barrelCount; i++) {
            barrels.add(view.popVect3());
        }

        damageType = DamageDescriptor.DamageType.values()[view.popInteger()];
    }

}
