package com.irr310.common.world.item;

import java.util.Map;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.server.world.product.ComponentProduct;

public class ComponentItem extends DeployableItem {

    private final ComponentProduct product;

    public ComponentItem(ComponentProduct product,  World world, long id, Map<String,Item> subItems) {
        super(product , ItemType.COMPONENT ,  world, id, subItems);
        this.product = product;
    }

    @Override
    protected void doDeploy(Vec3 position) {
        // TODO Auto-generated method stub
        
    }

}
