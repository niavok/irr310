package com.irr310.common.world.item;

import java.util.Map;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.item.Item.ItemType;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.world.product.Product;

public abstract class DeployableItem extends Item {
    
//    private double deployedRadius;
//    protected WorldSystem currentSystem;

    public DeployableItem(Product product, ItemType type, World world, long id, Faction owner, Map<String,Item> subItems) {
        super(product, type, world, id, owner, subItems);
    }
    
//    protected void setDeployedRadius(double radius) {
//        this.deployedRadius = radius;
//        
//    }
//    
//    public double getDeployedRadius() {
//        return deployedRadius;
//    }
//    
//    public WorldSystem getCurrentSystem() {
//        return currentSystem;
//    }
    
    @Override
    public boolean isDeployable() {
        return true;
    }

    protected abstract void doDeploy(Vec3 position);
}
