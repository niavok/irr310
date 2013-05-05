package com.irr310.common.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.irr310.common.world.item.Item;
import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.PlayerState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.server.world.product.ProductManager;

public class World {

    private final List<Item> items;
    private final List<Player> players;
    private final List<Faction> factions;
    
    private final List<Upgrade> availableUpgrades;
    private final Map<Long, Player> playerIdMap;
    private final Map<Long, Faction> factionIdMap;
    private final Map<Long, Item> itemIdMap;

    
    private WorldMap map;
    private ProductManager productManager;
//    private BinderServer binderServer;
    private ItemFactory itemFactory;

    public World() {
//        binderServer = new BinderServer();
        
        players = new CopyOnWriteArrayList<Player>();
        factions= new CopyOnWriteArrayList<Faction>();
        items = new CopyOnWriteArrayList<Item>();
        
        
        playerIdMap = new HashMap<Long, Player>();
        factionIdMap = new HashMap<Long, Faction>();
        
        itemIdMap = new HashMap<Long, Item>();
        availableUpgrades = new CopyOnWriteArrayList<Upgrade>();
       
        map = new com.irr310.common.world.WorldMap();
        
        itemFactory = new ItemFactory(this);
    }

    

    public void addPlayer(Player player) {
        players.add(player);
        playerIdMap.put(player.getId(), player);
//        Game.getInstance().sendToAll(new PlayerAddedEvent(player));
    }

    public void addFaction(Faction faction) {
        factions.add(faction);
        factionIdMap.put(faction.getId(), faction);
//        Game.getInstance().sendToAll(new FactionAddedEvent(faction));
    }
    
    public List<Faction> getFactions() {
        return factions;
    }
    
    public Player loadPlayer(PlayerState playerView) {
        if (playerIdMap.containsKey(playerView.id)) {
            return playerIdMap.get(playerView.id);
        }

        Player player = new Player(this, playerView.id, playerView.login);
        player.fromState(playerView);
        addPlayer(player);
        return player;
    }

    
    public Player getPlayerById(long playerId) {
        return playerIdMap.get(playerId);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addUpgrade(Upgrade upgrade) {
        availableUpgrades.add(upgrade);
    }

    public List<Upgrade> getAvailableUpgrades() {
        return availableUpgrades;
    }

    public com.irr310.common.world.WorldMap getMap() {
        return map;
    }

    public void addItem(Item item) {
        items.add(item);
        itemIdMap.put(item.getId(), item);
    }

//    public Player getLocalPlayer() {
//        for(Player player: players) {
//            if(player.isLocal()) {
//                return player;
//            }
//        }
//        return null;
//    }



    public Faction getFaction(FactionState factionState) {
        for(Faction faction: factions) {
            if(faction.isState(factionState)) {
                return faction;
            }
        }
        return null;
    }

    public Item getItem(ItemState itemState) {
        for(Item item: items) {
            if(item.isState(itemState)) {
                return item;
            }
        }
        return null;
    }
    

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
    
    public ProductManager getProductManager() {
        return productManager;
    }



    public ItemFactory getItemFactory() {
        return itemFactory;
    }



    public WorldSystem getSystem(WorldSystemState systemState) {
        for(WorldSystem system: map.getSystems()) {
            if(system.isState(systemState)) {
                return system;
            }
        }
        return null;
    }

//    public BinderServer getBinderServer() {
//        return binderServer;
//    }
//
//    public void flush() {
//        binderServer.flush();
//    }

}
