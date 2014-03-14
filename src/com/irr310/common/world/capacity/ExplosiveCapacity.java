package com.irr310.common.world.capacity;

import com.irr310.common.world.World;
import com.irr310.common.world.system.DamageDescriptor.DamageType;

public class ExplosiveCapacity extends Capacity {

    public boolean fire;
    public boolean consumed;
    public double disarmTimeout;
    public double explosionRadius;
    public double explosionBlast;
    public double armorPenetration;
    public double explosionDamage;
    public DamageType damageType;


    public ExplosiveCapacity(World world, long id) {
        super(world, id);
        fire = false;
        consumed = false;
    }
    
    @Override
    public void trigger(String triggerCode) {
        if(triggerCode.equals("fire")) {
            fire = true;
        }
    }

    

}
