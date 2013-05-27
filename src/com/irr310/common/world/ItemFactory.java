package com.irr310.common.world;

import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.world.item.ComponentItem;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.ShipItem;
import com.irr310.server.GameServer;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ShipProduct;

public class ItemFactory {

    private final World world;

    public ItemFactory(World world) {
        this.world = world;
    }

    public void createItem(Product product, Faction faction, Map<String,Item> subItems) {
        if(product instanceof ComponentProduct) {
            ComponentProduct componentProduct = (ComponentProduct) product;
            ComponentItem componentItem = new ComponentItem(componentProduct, GameServer.pickNewId(), faction, subItems);
            faction.getStocks().addItem(componentItem);
        } else if(product instanceof ShipProduct) {
            ShipProduct shipProduct = (ShipProduct) product;
            ShipItem componentItem = new ShipItem(shipProduct, GameServer.pickNewId(), faction, subItems);
            faction.getStocks().addItem(componentItem);
        } else {
            Log.error("Not implement createItem for"+product.getClass().getSimpleName());
        }
     
        //Remove sub items from stock
        for(Item item:subItems.values()) {
            faction.getStocks().removeItem(item);
        }
        
    }

}
