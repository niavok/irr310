package com.irr310.server.engine.world;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.irr310.common.engine.Engine;
import com.irr310.common.engine.Observable;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.WorldMap;
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
import com.irr310.server.world.product.ProductManager;

public class WorldEngine implements Engine {

    private World world;
    private Time mNextRoundTime;
    private Time mNextTurnTime;
    private Duration mRoundDuration;
    private Duration mTurnDuration;
    private ProductionManager productionManager = new ProductionManager();
    private Map<WorldSystem, SystemEngine> systemEngineMap = new HashMap<WorldSystem, SystemEngine>();
    
    public WorldEngine() {
        GameServer.setWorldEngine(this);
    }

    
    @Override
    public void init() {
        initWorld();    
        
        mRoundDuration = new Duration(1000000000l); // 1s
        mTurnDuration = new Duration(10000000000l); // 10s
        mNextRoundTime = Time.now(true);
        mNextTurnTime = Time.now(true);
    }

    @Override
    public void start() {
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
        Player newPlayer = new Player(world, GameServer.pickNewId(), playerLogin);
        newPlayer.setHuman(true);
        newPlayer.setLocal(isLocal);
        //Find faction
        Faction faction = world.getFactions().get(0);
        
        faction.assignPlayer(newPlayer);
        
        world.addPlayer(newPlayer);
        notifyPlayerConnected(newPlayer);
    }
    
    private void initWorld() {
        
        List<String> availableNames = loadSystemNameList();
        
        world = new World();
        Random random = new Random();
        
        WorldMap map = world.getMap();
        
        //Init map
        int factionCount = 5;
        int systemCount = 100;
        double mapSize = 1000;
        double mapMinDistance = 100;
        
        // Init zone positions
        
        int validSystem = 0;
        while(validSystem < systemCount) {
            
            //double distance = (1 - Math.sqrt(random.nextDouble())) * mapSize;
            double distance = (0.5 * (1 - Math.sqrt(random.nextDouble())) + 0.5 * random.nextDouble()) * mapSize;
            double azimut = random.nextDouble() * 2 * Math.PI;
            
            Vec2 location = new Vec2(0, distance).rotate(azimut);
            

            if(map.getSystems().size() > 0) {
                
                WorldSystem nearestSystem = map.nearestSystemTo(location);
                
                if(nearestSystem.getLocation().distanceTo(location) < mapMinDistance) {
                    // Too near to a existing system, retry and reduce the min distance requierement.
                    mapMinDistance--;
                    continue;
                } else {
                    // Reduce the distance to the nearest system to create small cluster of system
                    location = location.add(location.diff(nearestSystem.getLocation()).normalize().multiply(mapMinDistance/2) );
                }
            }
            
            WorldSystem system = new WorldSystem(world, GameServer.pickNewId(), location);
            
            
            system.setRadius(1000 + (1 - random.nextDouble()) * 50000 );
            
            
            SystemEngine systemEngine = new SystemEngine(this, system);
            systemEngineMap.put(system, systemEngine);
            systemEngine.init();
            
            int nameIndex = random.nextInt(availableNames.size());
            String name = availableNames.remove(nameIndex);
            
            system.setName(name);
            map.addZone(system);
            mapMinDistance++;
            
            validSystem++;
        }
        
        // Find home system
        double baseAzimut = random.nextDouble() * 2 * Math.PI;
        
        List<WorldSystem> availableHome = new ArrayList<WorldSystem>();
        
        for(int i = 0; i < factionCount; i++) {
            Vec2 location = new Vec2(0, mapSize/2).rotate(baseAzimut + i * 2 * Math.PI / factionCount);
            availableHome.add(map.nearestSystemTo(location));
        }
        
        // Init products
        ProductManager productManager = new ProductManager();
        try {
            productManager.init();    
        } catch(RessourceLoadingException e) {
            Log.warn("Fail to load products world correctly", e);
        }
        world.setProductManager(productManager);
        
        
        // Init faction
        for(int i = 0; i < factionCount; i++) {
            // Pick home system
            int homeIndex = random.nextInt(factionCount - i);
            WorldSystem system = availableHome.get(homeIndex);
            availableHome.remove(homeIndex);
            
            Faction faction = new Faction(world, GameServer.pickNewId());
            faction.getAvailableProductList().setProductManager(productManager);
            faction.setHomeSystem(system);
            system.setHomeSystem(true);
            system.setOwner(faction);
            
            faction.setStatersAmount(2000);
            faction.setOresAmount(20000);
            faction.setKoliumAmount(300);
            faction.setNeuridiumAmount(0);
            
            world.addFaction(faction);
            
            Nexus rootNexus = new Nexus(system, GameServer.pickNewId());
            rootNexus.setRadius(10);
            Vec3 nexusLocation = system.getRandomEmptySpace(rootNexus.getRadius());
            rootNexus.setLocation(nexusLocation);
            rootNexus.setOwner(faction);
            
            faction.setRootNexus(rootNexus);
            system.addNexus(rootNexus);
            
            
//            NexusItem nexus = new BuildingItemFactory(world).createNexus(faction);
            
            
//            world.addItem(nexus);
            
            
            
//            nexus.forceDeploy(system, );
        }
        
//        world.flush();
        
        
        //Wait for system engine started
        
        map.dump();
        
        
    }
    
    private List<String> loadSystemNameList() {
        
        List<String> names = new ArrayList<String>();
        
        try {
            FileInputStream fis = new FileInputStream("assets/system_names.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;
            while ((line = br.readLine()) != null) {
                names.add(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
        
        return names;
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
