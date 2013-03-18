package com.irr310.common.world.item;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldSystem;

public abstract class DeployableItem extends Item {
    
    private double deployedRadius;
    protected WorldSystem currentSystem;

    public DeployableItem(World world, long id, Faction owner) {
        super(world, id, owner);
        setDeployable(true);
    }
    
    protected void setDeployedRadius(double radius) {
        this.deployedRadius = radius;
        
    }
    
    public double getDeployedRadius() {
        return deployedRadius;
    }
    
    public void forceDeploy(WorldSystem system, Vec3 position) {
        
        this.currentSystem = system;
        doDeploy(position);
    }

    protected abstract void doDeploy(Vec3 position);
}
