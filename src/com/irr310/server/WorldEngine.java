package com.irr310.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.irr310.common.engine.EngineManager;
import com.irr310.common.engine.EventDispatcher;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.game.DefaultGameEventVisitor;
import com.irr310.common.event.game.GameEvent;
import com.irr310.common.event.game.QuitGameEvent;
import com.irr310.common.event.world.ConnectPlayerEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.PlayerConnectedEvent;
import com.irr310.common.event.world.WorldEvent;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Map;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.item.BuildingItemFactory;
import com.irr310.common.world.item.NexusItem;
import com.irr310.common.world.system.WorldSystem;

public class WorldEngine extends FramerateEngine<GameEvent> implements EventDispatcher<WorldEventVisitor, WorldEvent>  {

    private World world;
    private EngineManager<WorldEventVisitor, WorldEvent> engineManager;

    public WorldEngine(Object object) {
        engineManager = new EngineManager<WorldEventVisitor, WorldEvent>();
        engineManager.registerEventVisitor(new WorldEngineWorldEventVisitor());
    }

    @Override
    protected void processEvent(GameEvent e) {
        e.accept(new WorldEngineGameEventVisitor());
    }

    @Override
    protected void frame() {
    }

    @Override
    protected void onInit() {
        initWorld();
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
            Player newPlayer = new Player(GameServer.pickNewId(), event.getPlayerLogin());
            newPlayer.setHuman(true);
            //Find faction
            Faction faction = world.getFactions().get(0);
            
            faction.assignPlayer(newPlayer);
            
            world.addPlayer(newPlayer);
            engineManager.sendToAll(new PlayerConnectedEvent(newPlayer));
        }
    }
    
    private void initWorld() {
        
        world = new World();
        Random random = new Random();
        
        Map map = getWorld().getMap();
        
        //Init map
        int factionCount = 5;
        int systemCount = 100;
        double mapSize = 1000;
        double mapMinDistance = mapSize/50;
        
        // Init zone positions
        
        int validSystem = 0;
        while(validSystem < systemCount) {
            
            //double distance = (1 - Math.sqrt(random.nextDouble())) * mapSize;
            double distance = (0.5 * (1 - Math.sqrt(random.nextDouble())) + 0.5 * random.nextDouble()) * mapSize;
            double azimut = random.nextDouble() * 2 * Math.PI;
            
            Vec2 location = new Vec2(0, distance).rotate(azimut);
            

            if(map.getZones().size() > 0) {
                
                WorldSystem nearestSystem = map.nearestSystemTo(location);
                
                if(nearestSystem.getLocation().distanceTo(location) < mapMinDistance) {
                    // Too near to a existing system, retry
                    Log.trace("Too near to a existing system, retry :"+ nearestSystem.getLocation().distanceTo(location));
                    mapMinDistance--;
                    continue;
                } else {
                    Log.trace("Distance before :"+ nearestSystem.getLocation().distanceTo(location));
                    location = location.add(location.diff(nearestSystem.getLocation()).normalize().multiply(mapMinDistance/2) );
                    Log.trace("Distance after :"+ nearestSystem.getLocation().distanceTo(location));
                }
            }
            
            WorldSystem system = new WorldSystem(GameServer.pickNewId(), location);
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
        
        
        // Init faction
        for(int i = 0; i < factionCount; i++) {
            // Pick home system
            int homeIndex = random.nextInt(factionCount - i);
            WorldSystem system = availableHome.get(homeIndex);
            availableHome.remove(homeIndex);
            
            Faction faction = new Faction(GameServer.pickNewId());
            faction.setHomeSystem(system);
            
            getWorld().addFaction(faction);
            
            NexusItem nexus = BuildingItemFactory.createNexus(faction);
            
            
            getWorld().addItem(nexus);
            
            nexus.forceDeploy(system, system.getRandomEmptySpace(nexus.getDeployedRadius()));
        }
        

        
        
        map.dump();
    }
    
    public World getWorld() {
        return world;
    }

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
