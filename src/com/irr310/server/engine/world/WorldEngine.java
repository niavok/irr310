package com.irr310.server.engine.world;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.engine.Engine;
import com.irr310.common.engine.Observable;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.item.Item.State;
import com.irr310.common.world.item.ShipItem;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.Duration;
import com.irr310.server.GameServer;
import com.irr310.server.ProductionManager;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;
import com.irr310.server.engine.system.SystemEngine;
import com.irr310.server.world.product.Product;

public class WorldEngine implements Engine {

    private World world;
    private Time mNextRoundTime;
    private Time mNextTurnTime;
    private Duration mRoundDuration;
    private Duration mTurnDuration;
    private ProductionManager productionManager = new ProductionManager();
    private Map<WorldSystem, SystemEngine> systemEngineMap = new HashMap<WorldSystem, SystemEngine>();
    
    public WorldEngine(World world) {
        this.world = world;
        GameServer.setWorldEngine(this);
    }

    
    @Override
    public void init() {
        
        for(WorldSystem system : world.getMap().getSystems()) {
            SystemEngine systemEngine = new SystemEngine(this, system);
            systemEngineMap.put(system, systemEngine);
            systemEngine.init();
        }
        
        mRoundDuration = new Duration(1000000000l); // 1s
        mTurnDuration = new Duration(10000000000l); // 10s
        mNextRoundTime = Time.now(true);
        mNextTurnTime = Time.now(true);
        
        Time.startGame(mNextRoundTime);
        
    }

    @Override
    public void start() {
        for(SystemEngine engine: systemEngineMap.values()) {
            engine.start();
        }
    }
    
    @Override
    public void stop() {
        for(SystemEngine engine: systemEngineMap.values()) {
            engine.stop();
        }
    }
    
    @Override
    public void destroy() 
    {
        for(SystemEngine engine: systemEngineMap.values()) {
            engine.destroy();
        }
    }
    
    
    @Override
    public void tick(Timestamp time) {
        // The game engine has 3 times cron :
        // - One at each tick for use input managerment (tick)
        // - One per second for short cooldown (round)
        // - One per 10 seconds for long cooldown (turn)
        
        
        for(SystemEngine engine: systemEngineMap.values()) {
            engine.tick(time);
        }
        
        Time frameTime = time.getGameTime();
        
        doTick();
        
        while(frameTime.after(mNextRoundTime)) {
            doRound();
            mNextRoundTime = mNextRoundTime.add(mRoundDuration);
        }
        
        while(frameTime.after(mNextTurnTime)) {
            doTurn();
            mNextTurnTime = mNextTurnTime.add(mTurnDuration);
        }
    }

    private void doTick() {
        
    }

    private void doRound() {
//        Log.trace("-------------- tick");
        for(Faction faction: world.getFactions()) {
            productionManager.doRound(faction.getProduction());
            notifyProductionChanged(faction.getProduction());
            notifyStocksChanged(faction.getStocks());
            notifyFactionChanged(faction);
        }
    }

    private void doTurn() {
//        Log.trace("=============== turn");
        // Revenue
        for(Faction faction: world.getFactions()) {
            faction.giveStaters(1000);
            faction.giveOres(400);
        }
        
        for(Faction faction: world.getFactions()) {
            productionManager.doTurn(faction.getProduction());
        }
    }
    
    public ProductionManager getProductionManager() {
		return productionManager;
	}
    
    public void connectPlayerAction(String playerLogin, boolean isLocal) {

        Player newPlayer = world.getPlayerByLogin(playerLogin);


        if(newPlayer == null) {
            newPlayer = new Player(world, GameServer.pickNewId(), playerLogin);
            newPlayer.setHuman(true);
            newPlayer.setLocal(isLocal);
            //Find faction
            Faction faction = world.getFactions().get(0);

            faction.assignPlayer(newPlayer);

            world.addPlayer(newPlayer);
        }
        notifyPlayerConnected(newPlayer);
    }
    
    
    
    public SystemEngine getSystemEngine(WorldSystem system) {
        return systemEngineMap.get(system);
    }

    // Actions
    
    public void buyFactoryCapacityAction(Faction faction, int amount) {
         productionManager.buyFactoryCapacity(faction.getProduction(), amount);
         notifyProductionChanged(faction.getProduction());
         notifyFactionChanged(faction);
    }

	public void sellFactoryCapacityAction(Faction faction, int amount) {
		productionManager.sellFactoryCapacity(faction.getProduction(), amount);
		notifyProductionChanged(faction.getProduction());
        notifyFactionChanged(faction);
	}
	
	public void buyProductAction(Faction faction, Product product, long count) {
	    faction.getProduction().addTask(GameServer.pickNewId(), product, count);
        notifyProductionChanged(faction.getProduction());
    }

	public void deployShipAction(ShipItem ship, Nexus nexus) {
	    ship.setState(State.DEPLOYED);

	    SystemEngine systemEngine = systemEngineMap.get(nexus.getWorldSystem());
	    systemEngine.deployShip(ship, nexus);
	    
	    notifyStocksChanged(ship.getOwner().getStocks());
	}
	
	public World getWorld() {
        return world;
    }
	
	// Observers
    private Observable<WorldEngineObserver> mWorldEngineObservable = new Observable<WorldEngineObserver>();
    
    public Observable<WorldEngineObserver> getWorldEnginObservable() {
        return mWorldEngineObservable;
    }
    
    private void notifyPlayerConnected(Player player) {
        for(WorldEngineObserver observer : mWorldEngineObservable.getObservers()) {
            observer.onPlayerConnected(player);
        }
    }
    
    private void notifyFactionChanged(Faction faction) {
        for(WorldEngineObserver observer : mWorldEngineObservable.getObservers()) {
            observer.onFactionChanged(faction);
        }        
    }


    private void notifyStocksChanged(FactionStocks stocks) {
        for(WorldEngineObserver observer : mWorldEngineObservable.getObservers()) {
            observer.onStocksChanged(stocks);
        }
    }


    private void notifyProductionChanged(FactionProduction production) {
        for(WorldEngineObserver observer : mWorldEngineObservable.getObservers()) {
            observer.onProductionChanged(production);
        }
    }
    

}
