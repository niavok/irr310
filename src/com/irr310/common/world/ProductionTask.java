package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.world.product.Product;

class ProductionTask extends WorldEntity {

    private final Product product;
    private final long requestedQuantity;
    private int doneQuantity;

    List<Item> reservedItems = new ArrayList<Item>();
    
    public ProductionTask(World world, long id, Product product, long quantity) {
        super(world, id);
        this.product = product;
        this.requestedQuantity = quantity;
        this.doneQuantity = 0;
    }
    
    public ProductionTaskState toState() {
        ProductionTaskState state = new ProductionTaskState();
        
        state.id = getId();
        state.requestedQuantity = requestedQuantity;
        state.doneQuantity = doneQuantity;
        state.product = product.toState();
        
        state.reservedItemIds = new ArrayList<Long>();
        
        for(Item item : reservedItems) {
            state.reservedItemIds.add(item.getId());
        }
        
        return state;
    }
    
    
}