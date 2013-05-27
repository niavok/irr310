package com.irr310.common.world.item;

import java.util.Map;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.ShipProduct;

public class ShipItem extends DeployableItem {

    private final ShipProduct product;

    public ShipItem(ShipProduct product,  long id, Faction owner, Map<String,Item> subItems) {
        super(product , owner.getWorld(), id, owner, subItems);
        this.product = product;
    }

    @Override
    protected void doDeploy(Vec3 position) {
        // TODO Auto-generated method stub
        
    }

}
