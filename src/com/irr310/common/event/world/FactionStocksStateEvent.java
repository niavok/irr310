package com.irr310.common.event.world;

import com.irr310.common.world.state.FactionStocksState;

public class FactionStocksStateEvent extends WorldEvent {

    final private FactionStocksState factionStocks;

    public FactionStocksStateEvent(FactionStocksState factionStocks) {
        this.factionStocks = factionStocks;
    }

    public FactionStocksState getFactionStocks() {
        return factionStocks;
    }

    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
