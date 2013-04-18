package com.irr310.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.irr310.common.engine.EngineManager;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.game.DefaultGameEventVisitor;
import com.irr310.common.event.game.GameEvent;
import com.irr310.common.event.game.QuitGameEvent;
import com.irr310.common.event.world.ActionBuyFactionFactoryCapacityEvent;
import com.irr310.common.event.world.ActionBuyProductEvent;
import com.irr310.common.event.world.ActionSellFactionFactoryCapacityEvent;
import com.irr310.common.event.world.ConnectPlayerEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionAvailableProductListEvent;
import com.irr310.common.event.world.FactionProductionStateEvent;
import com.irr310.common.event.world.FactionStateEvent;
import com.irr310.common.event.world.PlayerConnectedEvent;
import com.irr310.common.event.world.QueryFactionAvailableProductListEvent;
import com.irr310.common.event.world.QueryFactionProductionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.QueryWorldMapStateEvent;
import com.irr310.common.event.world.WorldEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.event.world.WorldMapStateEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.WorldMap;
import com.irr310.common.world.item.BuildingItemFactory;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ProductManager;

public class WorldEngine extends FramerateEngine<GameEvent> implements WorldEventDispatcher  {

    private World world;
    private EngineManager<WorldEventVisitor, WorldEvent> engineManager;
    private Time nextTickTime;
    private Time nextTurnTime;
    private Duration tickDuration;
    private Duration turnDuration;
    private ProductionManager productionManager = new ProductionManager();
    
    public WorldEngine() {
        setFramerate(new Duration(40000000l)); // 40ms 25fps 
        engineManager = new EngineManager<WorldEventVisitor, WorldEvent>();
        engineManager.registerEventVisitor(new WorldEngineWorldEventVisitor());
    }

    @Override
    protected void processEvent(GameEvent e) {
        e.accept(new WorldEngineGameEventVisitor());
    }

    @Override
    protected void frame() {
        // The game engine has 3 times cron :
        // - One at each frame for use input managerment (frame)
        // - One per second for short cooldown (tick)
        // - One per 10 seconds for long cooldown (turn)
        
        Time frameTime = Time.now(true);
        
        doFrame();
        
        while(frameTime.after(nextTickTime)) {
            doTick();
            nextTickTime = nextTickTime.add(tickDuration);
        }
        
        while(frameTime.after(nextTurnTime)) {
            doTurn();
            nextTurnTime = nextTurnTime.add(turnDuration);
        }
        
        
    }

    private void doFrame() {
        
    }

    private void doTick() {
        Log.trace("-------------- tick");
        for(Faction faction: world.getFactions()) {
            productionManager.doTick(faction.getProduction());
            engineManager.sendToAll(new FactionProductionStateEvent(faction.getProduction().toState()));
        }
        for(Faction faction: world.getFactions()) {
            engineManager.sendToAll(new FactionStateEvent(faction.toState()));
            
        }
        
    }

    private void doTurn() {
        Log.trace("=============== turn");
        // Revenue
        for(Faction faction: world.getFactions()) {
            faction.giveStaters(1000);
            faction.giveOres(400);
            
        }
        
        for(Faction faction: world.getFactions()) {
            productionManager.doTurn(faction.getProduction());
        }
        
        
    }
    
    @Override
    protected void onInit() {
        initWorld();    
        
        tickDuration = new Duration(1000000000l); // 1s
        turnDuration = new Duration(10000000000l); // 10s
        nextTickTime = Time.now(true);
        nextTurnTime = Time.now(true);
    }

    @Override
    protected void onStart() {
        pause(false);
    }
    
    @Override
    protected void onEnd() {
        
    }

    private final class WorldEngineGameEventVisitor extends DefaultGameEventVisitor {

        @Override
        public void visit(QuitGameEvent event) {
            java.lang.System.out.println("stopping world engine");
            setRunning(false);
        }
//        
//        @Override
//        public void visit(StartEngineEvent event) {
//            pause(false);
//        }
//
//        @Override
//        public void visit(PauseEngineEvent event) {
//            pause(true);
//        }
       
    }
    
    private final class WorldEngineWorldEventVisitor extends DefaultWorldEventVisitor {

        @Override
        public void visit(ConnectPlayerEvent event) {
            Player newPlayer = new Player(world, GameServer.pickNewId(), event.getPlayerLogin());
            newPlayer.setHuman(true);
            newPlayer.setLocal(event.isLocal());
            //Find faction
            Faction faction = world.getFactions().get(0);
            
            faction.assignPlayer(newPlayer);
            
            world.addPlayer(newPlayer);
            engineManager.sendToAll(new PlayerConnectedEvent(newPlayer));
        }
        
        @Override
        public void visit(QueryFactionStateEvent event) {
            Faction faction = world.getFaction(event.getFaction());
            engineManager.sendToAll(new FactionStateEvent(faction.toState()));
        }
        
        @Override
        public void visit(QueryFactionProductionStateEvent event) {
            Faction faction = world.getFaction(event.getFaction());
            engineManager.sendToAll(new FactionProductionStateEvent(faction.getProduction().toState()));
        }
        
        @Override
        public void visit(QueryFactionAvailableProductListEvent event) {
            Faction faction = world.getFaction(event.getFaction());
            engineManager.sendToAll(new FactionAvailableProductListEvent(faction.getAvailableProductList().toState()));
        }
        
        @Override
        public void visit(QueryWorldMapStateEvent event) {
            engineManager.sendToAll(new WorldMapStateEvent(world.getMap().toState()));
        }
        
        @Override
        public void visit(ActionBuyFactionFactoryCapacityEvent event) {
            Faction faction = world.getFaction(event.getFaction());
            productionManager.buyFactoryCapacity(faction.getProduction(), event.getCount());
            engineManager.sendToAll(new FactionProductionStateEvent(faction.getProduction().toState()));
            engineManager.sendToAll(new FactionStateEvent(faction.toState()));
        }
        
        @Override
        public void visit(ActionSellFactionFactoryCapacityEvent event) {
            Faction faction = world.getFaction(event.getFaction());
            productionManager.sellFactoryCapacity(faction.getProduction(), event.getCount());
            engineManager.sendToAll(new FactionProductionStateEvent(faction.getProduction().toState()));
            engineManager.sendToAll(new FactionStateEvent(faction.toState()));
        }
        
        @Override
        public void visit(ActionBuyProductEvent event) {
            Faction faction = world.getFaction(event.getFaction());
            Product product = faction.getProduct(event.getProduct());
            faction.getProduction().addTask(GameServer.pickNewId(), product, event.getCount());
            engineManager.sendToAll(new FactionProductionStateEvent(faction.getProduction().toState()));
        }
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
            
            faction.setStatersAmount(2000);
            faction.setOresAmount(20000);
            faction.setKoliumAmount(300);
            faction.setNeuridiumAmount(0);
            
            world.addFaction(faction);
            
//            NexusItem nexus = new BuildingItemFactory(world).createNexus(faction);
            
            
//            world.addItem(nexus);
            
            system.setOwner(faction);
            
//            nexus.forceDeploy(system, system.getRandomEmptySpace(nexus.getDeployedRadius()));
        }
        
//        world.flush();
        
        
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

//    public World getWorld() {
//        return world;
//    }

    @Override
    public void sendToAll(WorldEvent event) {
        engineManager.sendToAll(event);
    }
    
    public void registerEventVisitor(WorldEventVisitor visitor) {
        engineManager.registerEventVisitor(visitor);
    }

    public void unregisterEventVisitor(WorldEventVisitor visitor) {
        engineManager.unregisterEventVisitor(visitor);
    }

}
