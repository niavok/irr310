package com.irr310.common.world.capacity.controller;

import java.util.Random;

import com.irr310.common.Game;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.capacity.GunCapacity;

public class GunController extends CapacityController {

    private final GunCapacity capacity;
    private final Component component;
    private double[] barrelHeat;
    private double[] barrelCooldown;
    double globalCooldown;

    public GunController(Component component, GunCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
        
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
        
        Part part = component.getFirstPart();
            
        double xoffset = capacity.barrels.get(barrel).getX();
        double yoffset = capacity.barrels.get(barrel).getY();
            
        Vec3 from = new Vec3(xoffset, 0, yoffset).transform(part.getTransform());
            
        Random random = new Random();
        double accuracy = capacity.accuracy * (10 - 9* component.getEfficiency());
            
        Vec3 to = new Vec3(xoffset + accuracy *(0.5 - random.nextDouble()), capacity.range + accuracy *(0.5 - random.nextDouble()), yoffset + accuracy *(0.5 - random.nextDouble())).transform(part.getTransform());
            
            
            
        Game.getInstance().sendToAll(new BulletFiredEvent(part, component.getEfficiency() * capacity.damage, capacity.range, capacity.damageType, from, to));

        
        
        //Add overhit and cooldown
        barrelCooldown[barrel] = 1.0 / capacity.firerate;
        barrelHeat[barrel] += 1.0 /capacity.heatingSpeed;
        
        globalCooldown = 1.0 / (capacity.firerate * capacity.barrels.size());
        
    }

}
