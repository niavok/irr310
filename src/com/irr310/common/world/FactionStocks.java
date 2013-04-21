package com.irr310.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.Item.State;
import com.irr310.common.world.state.FactionProductionState;
import com.irr310.common.world.state.FactionStocksState;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.server.world.product.Product;

public class FactionStocks {

    Map<Product, List<Item>> itemsByProduct = new HashMap<Product, List<Item>>();
    
    private final Faction faction;

    public FactionStocks(Faction faction) {
        this.faction = faction;
    }

    public Item getAvailableItem(Product product) {
        
        List<Item> items = itemsByProduct.get(product);
        if(items == null) {
            // No if of this product
            return null;
        }
        
        for(Item item: items) {
            if(item.getState() == State.STOCKED) {
                return item;
            }
        }
        
        return null;
    }

    public void addItem(Item item) {
        List<Item> items = itemsByProduct.get(item.getProduct());
        if(items == null) {
            items = new ArrayList<Item>();
            itemsByProduct.put(item.getProduct(), items);
        }
        
        items.add(item);
    }

    public void removeItem(Item item) {
        List<Item> items = itemsByProduct.get(item.getProduct());
        if(items != null) {
            items.remove(item);
        }
    }

    public FactionStocksState toState() {
        FactionStocksState state = new FactionStocksState();
        state.factionId = faction.getId();

        state.stocks = new ArrayList<ItemState>();
        
        for(List<Item> itemsList: itemsByProduct.values()) {
            for(Item item: itemsList) {
                state.stocks.add(item.toState());
            }
        }
        
        
        return state;
    }
    
    

    
}
