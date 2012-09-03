package com.irr310.common.world.capacity;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.view.CapacityView;
import com.irr310.common.world.zone.DamageDescriptor;

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

    public RocketWeaponCapacity(long id) {
        super(id);
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

    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.GUN.ordinal();

        // TODO
        // view.pushBoolean(fire);
        // view.pushDouble(damage);
        // view.pushDouble(range);
        // view.pushDouble(accuracy);
        // view.pushDouble(coolingSpeed);
        // view.pushDouble(heatingSpeed);
        // view.pushDouble(firerate);
        // view.pushDouble(armorPenetration);
        // view.pushInteger(barrels.size());
        for (Vec3 barrel : barrels) {
            view.pushVect3(barrel);
        }

        view.pushInteger(damageType.ordinal());
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        setName(view.name);
        fire = view.popBoolean();
        // damage = view.popDouble();
        // range = view.popDouble();
        // accuracy = view.popDouble();
        // coolingSpeed = view.popDouble();
        // heatingSpeed = view.popDouble();
        firerate = view.popDouble();
        armorPenetration = view.popDouble();

        // TODO

        int barrelCount = view.popInteger();
        for (int i = 0; i < barrelCount; i++) {
            barrels.add(view.popVect3());
        }

        damageType = DamageDescriptor.DamageType.values()[view.popInteger()];
    }

}
