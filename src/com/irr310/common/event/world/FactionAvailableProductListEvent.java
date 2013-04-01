package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionAvailableProductListState;

public class FactionAvailableProductListEvent extends WorldEvent {

    final private FactionAvailableProductListState factionAvailableProductList;

    public FactionAvailableProductListEvent(FactionAvailableProductListState factionAvailableProductList) {
        this.factionAvailableProductList = factionAvailableProductList;
    }

    public FactionAvailableProductListState getFactionAvailableProductList() {
        return factionAvailableProductList;
    }

    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
