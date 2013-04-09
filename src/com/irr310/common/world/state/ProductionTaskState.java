package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.server.world.product.Product;

@NetworkClass
public class ProductionTaskState {

    @NetworkField
    public long id;
    
    @NetworkField
    public long requestedQuantity;
    
    @NetworkField
    public long doneQuantity;
    
    @NetworkField
    public ProductState product;
    
    @NetworkListField(Long.class)
    public List<Long> reservedItemIds;
    
}
