package com.irr310.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.irr310.common.binder.BindVariable;
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
        setFramerate(new Duration(1000000000l)); // 1s 
        engineManager = new EngineManager<WorldEventVisitor, WorldEvent>();
        engineManager.registerEventVisitor(new WorldEngineWorldEventVisitor());
    }

    @Override
    protected void processEvent(GameEvent e) {
        e.accept(new WorldEngineGameEventVisitor());
    }

    @Override
    protected void frame() {
        BindVariable<Integer> statersAmount = world.getFactions().get(0).getStatersAmount();
        statersAmount.set(statersAmount.getCurrent()+1);
        world.flush();
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
            Player newPlayer = new Player(world, GameServer.pickNewId(), event.getPlayerLogin());
            newPlayer.setHuman(true);
            newPlayer.setLocal(event.isLocal());
            //Find faction
            Faction faction = world.getFactions().get(0);
            
            faction.assignPlayer(newPlayer);
            
            world.addPlayer(newPlayer);
            engineManager.sendToAll(new PlayerConnectedEvent(newPlayer));
        }
    }
    
    private void initWorld() {
        
        List<String> availableNames = loadSystemNameList();
        
        world = new World();
        Random random = new Random();
        
        Map map = getWorld().getMap();
        
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
        
        
        // Init faction
        for(int i = 0; i < factionCount; i++) {
            // Pick home system
            int homeIndex = random.nextInt(factionCount - i);
            WorldSystem system = availableHome.get(homeIndex);
            availableHome.remove(homeIndex);
            
            Faction faction = new Faction(world, GameServer.pickNewId());
            faction.setHomeSystem(system);
            system.setHomeSystem(true);
            
            faction.getStatersAmount().set(2000);
            faction.getOresAmount().set(200);
            faction.getKoliumAmount().set(300);
            faction.getNeuridiumAmount().set(0);
            
            getWorld().addFaction(faction);
            
            NexusItem nexus = new BuildingItemFactory(world).createNexus(faction);
            
            
            getWorld().addItem(nexus);
            
            system.setOwner(faction);
            
            nexus.forceDeploy(system, system.getRandomEmptySpace(nexus.getDeployedRadius()));
        }
        
        getWorld().flush();
        
        
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
