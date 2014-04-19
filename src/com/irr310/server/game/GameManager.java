package com.irr310.server.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.WorldMap;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.GameServer;
import com.irr310.server.world.product.ProductManager;

public class GameManager {

    private Game mActiveGame;
    private Game mPreviousGame;

    public Game createGame() {
        Game game = new Game();
        
        initWorld(game.getWorld());
        
        return game;
    }

    private void initWorld(World world) {
        
        List<String> availableNames = loadSystemNameList();
        
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
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
        
        return names;
    }

    public void setActiveGame(Game game) {
        mActiveGame = game;
        mPreviousGame = game;
    }
    
    public Game getActiveGame() {
        return mActiveGame;
    }

    public Game getPreviousGame() {
        return mPreviousGame;
    }

    public void save() {
        Log.log("Begin save");
        GameSerializer saver = new GameSerializer();
        saver.save(mActiveGame);
        
        
        Log.log("End save");
    }
}
