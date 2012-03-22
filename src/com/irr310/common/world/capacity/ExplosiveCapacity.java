package com.irr310.common.world.capacity;

import com.irr310.common.world.DamageDescriptor.DamageType;
import com.irr310.common.world.view.CapacityView;

public class ExplosiveCapacity extends Capacity {

    public boolean fire;
    public boolean disarm;
    public double explosionRadius;
    public double explosionBlast;
    public double armorPenetration;
    public double explosiontDamage;
    public DamageType damageType;


    public ExplosiveCapacity(long id) {
        super(id);
        boolean fire = false;
    }

    
    @Override
    public CapacityView toView() {
        CapacityView view = new CapacityView();
        view.id = getId();
        view.name = getName();
        view.type = CapacityType.LINEAR_ENGINE.ordinal();

        //TODO
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        setName(view.name);
        //TODO
    }

    

}
