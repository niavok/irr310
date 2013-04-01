package com.irr310.common.event.world;

import com.irr310.common.world.view.FactionAvailableProductListView;

public class FactionAvailableProductListEvent extends WorldEvent {

    final private FactionAvailableProductListView factionAvailableProductList;

    public FactionAvailableProductListEvent(FactionAvailableProductListView factionAvailableProductList) {
        this.factionAvailableProductList = factionAvailableProductList;
    }

    public FactionAvailableProductListView getFactionAvailableProductList() {
        return factionAvailableProductList;
    }

    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
