package com.irr310.common.world.capacity;

import com.irr310.common.event.DamageEvent.DamageType;
import com.irr310.common.world.view.CapacityView;

public class GunCapacity extends Capacity {

    public boolean fire;
    public double damage;
    public DamageType damageType;

    public GunCapacity(long id) {
        super(id, "gun");
        fire = false;
        damage = 10;
        damageType = DamageType.PHYSICAL;
    }

    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.GUN.ordinal();

        view.pushBoolean(fire);
        view.pushDouble(damage);
        view.pushInteger(damageType.ordinal());
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        fire = view.popBoolean();
        damage = view.popDouble();
        damageType =DamageType.values()[view.popInteger()];
    }

    

}
