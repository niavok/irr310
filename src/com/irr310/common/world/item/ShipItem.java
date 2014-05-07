package com.irr310.common.world.item;

import java.util.Map;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.server.world.product.ShipProduct;

public class ShipItem extends DeployableItem {

    // TODO delete that and use a gat getter ?
    private final ShipProduct mProduct;

    public ShipItem(ShipProduct product,  World world, long id, Map<String,Item> subItems) {
        super(product , ItemType.SHIP , world, id, subItems);
        this.mProduct = product;
    }

    @Override
    protected void doDeploy(Vec3 position) {
        // TODO Auto-generated method stub
        
    }

}
