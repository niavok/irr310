package com.irr310.common.world.item;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.system.System;

public abstract class DeployableItem extends Item {
    
    private double deployedRadius;
    protected System currentSystem;

    public DeployableItem(long id, Faction owner) {
        super(id, owner);
        setDeployable(true);
    }
    
    protected void setDeployedRadius(double radius) {
        this.deployedRadius = radius;
        
    }
    
    public double getDeployedRadius() {
        return deployedRadius;
    }
    
    public void forceDeploy(System system, Vec3 position) {
        
        this.currentSystem = system;
        doDeploy(position);
    }

    protected abstract void doDeploy(Vec3 position);
}
