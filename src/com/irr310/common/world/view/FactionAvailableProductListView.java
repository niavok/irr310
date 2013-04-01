package com.irr310.common.world.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class FactionAvailableProductListView {

    @NetworkField
    public long factionId;
    
    @NetworkListField(ProductView.class)
    public List<ProductView> products = new ArrayList<ProductView>();
}
