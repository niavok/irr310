package com.irr310.common.world.item;

import java.util.Map;

import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.world.product.Product;

public class Item extends WorldEntity {
    
    enum ItemType {
        BUILDING
    }
    
    public enum State {
        STOCKED,
        RESERVED,
        DEPLOYED,
        DESTROYED,
    }
    
    private final Faction owner;
    private Faction usufruct;
    private State state;
    private boolean deployable = false;
    private ItemType type;
    private Product product;
    private Map<String,Item> subItems;

    
    public Item(Product product, World world, long id, Faction owner, Map<String,Item> subItems) {
        super(world, id);
        this.product = product;
        this.owner = owner;
        this.subItems = subItems;
        this.state = State.STOCKED;
        setUsufruct(owner);
        world.addItem(this);
    }

    private void setUsufruct(Faction usufruct) {
        this.usufruct = usufruct;
    }
    
    protected void setItemType(ItemType type) {
        this.type = type;
        
    }

    protected void setDeployable(boolean deployable) {
        this.deployable = deployable;
    }

       
    public boolean isDeployable() {
        return deployable;
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
    
    public Map<String,Item> getSubItems() {
        return subItems;
    }
}
