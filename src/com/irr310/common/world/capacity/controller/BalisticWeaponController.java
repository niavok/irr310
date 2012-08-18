package com.irr310.common.world.capacity.controller;

import java.util.Random;

import com.irr310.common.Game;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.DamageDescriptor;
import com.irr310.common.world.Part;
import com.irr310.common.world.DamageDescriptor.DamageCause;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;

public abstract class BalisticWeaponController extends CapacityController {

    private final BalisticWeaponCapacity capacity;
    private final Component component;
    private double[] barrelHeat;
    private double[] barrelCooldown;
    double globalCooldown;

    public BalisticWeaponController(Component component, BalisticWeaponCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
        globalCooldown = 0;
        barrelHeat = new double[capacity.barrels.size()];
        barrelCooldown = new double[capacity.barrels.size()];
        
    }

    @Override
    public void update(double duration) {
     // Cool barrel and decrease cooldown
        for(int i = 0; i< capacity.barrels.size(); i++) {
            barrelHeat[i] -= duration / capacity.coolingSpeed;
            if( barrelHeat[i] < 0) {
                barrelHeat[i] = 0;
            }
            
            barrelCooldown[i] -= duration;
            if( barrelCooldown[i] < 0) {
                barrelCooldown[i] = 0;
            }
        }
        
        globalCooldown -= duration;
        
        if(!capacity.fire) {
            // The player don't want to fire
            return;
        }

        if(globalCooldown > 0) {
            //Wait to avoid to empty all barrel
            return;
        }
        
        
        int barrel = -1;

        //Find an available barrel
        for(int i = 0; i< capacity.barrels.size(); i++) {
            if(barrelCooldown[i] == 0 && barrelHeat[i] < 1) {
                barrel = i;
                break;
            }
        }
        
        if(barrel == -1) {
            // All barel on cooldown or overheated
            return;
        }
        
        shot(barrel);
        
        
      //Add overhit and cooldown
        barrelCooldown[barrel] = 1.0 / capacity.firerate;
        barrelHeat[barrel] += 1.0 /capacity.heatingSpeed;
        
        globalCooldown = 1.0 / (capacity.firerate * capacity.barrels.size());
    }
        
    
    protected abstract void shot(int barrel);

    public void genericShot(int barrel) {
        
        Part part = component.getFirstPart();
            
        double xoffset = capacity.barrels.get(barrel).getX();
        double yoffset = capacity.barrels.get(barrel).getZ();
            
        Vec3 from = new Vec3(xoffset, 1, yoffset).transform(part.getTransform());
            
        Random random = new Random();
        double accuracy = capacity.range * capacity.accuracy * (10 - 9* component.getEfficiency()) / 1000.0;
        
        Vec3 imprecision = new Vec3( accuracy* random.nextDouble(),  0, 0).rotate(new Vec3(random.nextDouble()*360, random.nextDouble()*360, random.nextDouble()*360));
        
        Vec3 to = new Vec3(xoffset, capacity.range , yoffset ).plus(imprecision).transform(part.getTransform());
            
        DamageDescriptor damage = new DamageDescriptor(capacity.damageType, capacity.armorPenetration, DamageCause.BULLET);
        damage.setWeaponBaseDamage(component.getEfficiency() * capacity.damage);
        Game.getInstance().sendToAll(new BulletFiredEvent(part, damage , capacity.range, from, to));

    }

    @Override
    public Component getComponent() {
        return component;
    }

}
