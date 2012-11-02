package com.irr310.common.world.capacity.controller;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Part;
import com.irr310.common.world.system.RocketDescriptor;

public abstract class RocketWeaponController extends CapacityController {

    private final RocketWeaponCapacity capacity;
    private final Component component;
    private double[] barrelCooldown;
    double globalCooldown;

    public RocketWeaponController(Component component, RocketWeaponCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
        globalCooldown = 0;
        barrelCooldown = new double[capacity.barrels.size()];
    }

    @Override
    public void update(double duration) {
     // Cool barrel and decrease cooldown
        for(int i = 0; i< capacity.barrels.size(); i++) {
            barrelCooldown[i] -= duration;
            if( barrelCooldown[i] < 0) {
                barrelCooldown[i] = 0;
            }
        }
        
        globalCooldown -= duration;
        
        if(!component.isAttached()) {
            // The component is detached, is cannot take order
            return;
        }
        
        if(!capacity.fire) {
            // The player don't want to fire
            return;
        }
        
        if(globalCooldown > 0) {
            //Wait to avoid to fire too quickly
            return;
        }

        int barrel = -1;

        //Find an available barrel
        for(int i = 0; i< capacity.barrels.size(); i++) {
            if(barrelCooldown[i] == 0) {
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
        barrelCooldown[barrel] = capacity.cooldown;
        
        globalCooldown = 1.0 / capacity.firerate;
    }
        
    
    protected abstract void shot(int barrel);

    public void genericShot(int barrel) {
        
        Part part = component.getFirstPart();
            
        double xoffset = capacity.barrels.get(barrel).getX();
        double yoffset = capacity.barrels.get(barrel).getZ();
           
        RocketDescriptor rocket = new RocketDescriptor();
        rocket.damageType = capacity.damageType;
        rocket.armorPenetration = capacity.armorPenetration;
        rocket.explosionDamage = capacity.explosionDamage;
        rocket.damping = capacity.damping;
        rocket.explosionBlast = capacity.explosionBlast;
        rocket.explosionRadius = capacity.explosionRadius;
        rocket.hitPoint = capacity.hitPoint;
        rocket.disarmTimeout = capacity.securityTimeout;
        rocket.stability = capacity.stability;
        rocket.thrust = capacity.thrust;
        rocket.thrustDuration = capacity.thrustDuration;
        
        TransformMatrix transformMatrix = TransformMatrix.identity();
        transformMatrix.translate(xoffset, 0, yoffset);
        transformMatrix.preMultiply(part.getTransform());
        Log.trace("fire barrel "+barrel+" at "+xoffset+"/"+yoffset);
        
//        Game.getInstance().sendToAll(new RocketFiredEvent(part, rocket , transformMatrix, part.getLinearSpeed()));
    }

    @Override
    public Component getComponent() {
        return component;
    }

}
