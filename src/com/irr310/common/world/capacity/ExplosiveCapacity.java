package com.irr310.common.world.capacity;

import com.irr310.common.world.view.CapacityView;
import com.irr310.common.world.zone.DamageDescriptor.DamageType;

public class ExplosiveCapacity extends Capacity {

    public boolean fire;
    public boolean consumed;
    public double disarmTimeout;
    public double explosionRadius;
    public double explosionBlast;
    public double armorPenetration;
    public double explosionDamage;
    public DamageType damageType;


    public ExplosiveCapacity(long id) {
        super(id);
        fire = false;
        consumed = false;
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
    
    @Override
    public void trigger(String triggerCode) {
        if(triggerCode.equals("fire")) {
            fire = true;
        }
    }

    

}
