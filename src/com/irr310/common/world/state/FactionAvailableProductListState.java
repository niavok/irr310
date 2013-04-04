package com.irr310.common.world.state;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class FactionAvailableProductListState {

    @NetworkField
    public long factionId;
    
    @NetworkListField(ProductState.class)
    public List<ProductState> products = new ArrayList<ProductState>();
}