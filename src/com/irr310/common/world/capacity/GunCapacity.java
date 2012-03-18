package com.irr310.common.world.capacity;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.DamageType;
import com.irr310.common.world.view.CapacityView;

public class GunCapacity extends Capacity {

    public boolean fire;
    public double damage;
    public double range;
    //Size of the dispertion cube at 1000m
    public double accuracy;
    
    // Bullet count to overheat
    public double heatingSpeed;

    // Time to cool to zero
    public double coolingSpeed;
    
    //Bullet par second
    public double firerate;
    
    
    //Offset of barrels
    public List<Vec3> barrels;
    
    public DamageType damageType;
    

    public GunCapacity(long id) {
        super(id, "gun");
        fire = false;
        damage = 50;
        range = 1000;
        accuracy = 10;
        coolingSpeed = 10;
        heatingSpeed = 5;
        firerate = 1;
        barrels = new ArrayList<Vec3>();
        barrels.add(new Vec3(0.14,0,0.14));
        barrels.add(new Vec3(-0.14,0,0.14));
        barrels.add(new Vec3(-0.14,0,0.14));
        barrels.add(new Vec3(0.14,0,-0.14));

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
        view.pushDouble(range);
        view.pushDouble(accuracy);
        view.pushDouble(coolingSpeed);
        view.pushDouble(heatingSpeed);
        view.pushDouble(firerate);
        view.pushInteger(barrels.size());
        for(Vec3 barrel: barrels) {
            view.pushVect3(barrel);
        }
        
        view.pushInteger(damageType.ordinal());
        return view;
    }

    @Override
    public void fromView(CapacityView view) {
        fire = view.popBoolean();
        damage = view.popDouble();
        range = view.popDouble();
        accuracy = view.popDouble();
        coolingSpeed = view.popDouble();
        heatingSpeed = view.popDouble();
        firerate = view.popDouble();
        
        int barrelCount = view.popInteger();
        for(int i = 0; i < barrelCount; i++) {
            barrels.add(view.popVect3());
        }
        
        damageType =DamageType.values()[view.popInteger()];
    }

    

}
