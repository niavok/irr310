package com.irr310.common.world.item;

import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.Product;

public class Item extends WorldEntity {
    
    enum ItemType {
        BUILDING
    }
    
    private final Faction owner;
    private Faction usufruct;

    private boolean deployable = false;
    private ItemType type;
    private boolean reserved = false;
    private boolean deployed = false;
    private Product product;
    private Item[] subItems;

    
    public Item(Product product, World world, long id, Faction owner, Item[] subItems) {
        super(world, id);
        this.product = product;
        this.owner = owner;
        this.subItems = subItems;
        setUsufruct(owner);
        
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

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
        
    }
    
    public boolean isReserved() {
        return reserved;
    }
    
    public boolean isDeployable() {
        return deployable;
    }
    
    public boolean isDeployed() {
        return deployed;
    }

    public Product getProduct() {
        return product;
    }
}
