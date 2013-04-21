package com.irr310.common.world.state;

import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class FactionStocksState {

    @NetworkField
    public long factionId;
    
    @NetworkListField(ItemState.class)
    public List<ItemState> stocks;
}