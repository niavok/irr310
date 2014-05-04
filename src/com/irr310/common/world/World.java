package com.irr310.common.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.world.item.Item;
import com.irr310.server.world.product.ProductManager;

public class World {

    private final List<Item> items;
    private final List<Player> players;
    private final List<Faction> factions;
    
//    private final List<Upgrade> availableUpgrades;
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
//        availableUpgrades = new CopyOnWriteArrayList<Upgrade>();
       
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
    
    public Player getPlayerById(long playerId) {
        return playerIdMap.get(playerId);
    }

    public List<Player> getPlayers() {
        return players;
    }

//    public void addUpgrade(Upgrade upgrade) {
//        availableUpgrades.add(upgrade);
//    }
//
//    public List<Upgrade> getAvailableUpgrades() {
//        return availableUpgrades;
//    }

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

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
    
    public ProductManager getProductManager() {
        return productManager;
    }



    public ItemFactory getItemFactory() {
        return itemFactory;
    }
    
    public List<Item> getItems() {
        return items;
    }

    public Player getPlayerByLogin(String playerLogin) {
        for (Player player : players) {
            if(player.getLogin().equals(playerLogin)) {
                return player;
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
