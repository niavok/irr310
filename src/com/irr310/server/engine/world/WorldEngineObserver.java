package com.irr310.server.engine.world;

import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;


public interface WorldEngineObserver {
    void onPlayerConnected(Player player);

    void onFactionChanged(Faction faction);

    void onStocksChanged(FactionStocks stocks);

    void onProductionChanged(FactionProduction production);
}
