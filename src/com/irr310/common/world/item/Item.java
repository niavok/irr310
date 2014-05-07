package com.irr310.common.world.item;

import java.util.Map;

import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.world.product.Product;

/**
 * An Item is a physical object present in a faction inventory. Only components can be deplayed.
 * 
 */
public class Item extends WorldEntity {
    
    public enum ItemType {
        INGREDIENT,
        COMPONENT,
        SHIP,
    }
    
    public enum State {
        STOCKED,
        RESERVED,
        DEPLOYED,
        DESTROYED,
    }
    
    private Faction owner;
    private Faction usufruct;
    private State state;
    private final ItemType type;
    private Product product;
    private Map<String,Item> subItems;

    
    public Item(Product product, ItemType type, World world, long id, Map<String,Item> subItems) {
        super(world, id);
        this.product = product;
        this.type = type;
        this.subItems = subItems;
        this.state = State.STOCKED;
        world.addItem(this);
    }

    public void setOwner(Faction owner) {
        this.owner = owner;
        if(usufruct == null) {
            setUsufruct(owner);
        }
    }

    public void setUsufruct(Faction usufruct) {
        this.usufruct = usufruct;
    }
       
    public boolean isDeployable() {
        return false;
    }
   
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public Faction getOwner() {
        return owner;
    }
    
    public Faction getUsufruct() {
        return usufruct;
    }
    
    public ItemType getType() {
        return type;
    }
    
    public Map<String,Item> getSubItems() {
        return subItems;
    }
}
