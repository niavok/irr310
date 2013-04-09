package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.ProductState;

public class ActionBuyProductEvent extends WorldEvent {

    
    final private FactionState faction;
    private final long count;
    private final ProductState product;

    public ActionBuyProductEvent(FactionState faction, ProductState product, long count) {
        this.faction = faction;
        this.product = product;
        this.count = count;
    }
    
    public FactionState getFaction() {
        return faction;
    }
    
    public long getCount() {
        return count;
    }
    
    public ProductState getProduct() {
        return product;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
