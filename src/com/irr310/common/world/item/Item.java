package com.irr310.common.world.item;

import com.irr310.common.network.NetworkField;
import com.irr310.common.tools.Log;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.Product;

public class Item extends WorldEntity {
    
    enum ItemType {
        BUILDING
    }
    
    public enum State {
        STOCKED,
        RESERVED,
        DEPLOYED,
        DEPLOYING,
        DESTROYED,
    }
    
    private final Faction owner;
    private Faction usufruct;
    private State state;
    private boolean deployable = false;
    private ItemType type;
    private Product product;
    private Item[] subItems;

    
    public Item(Product product, World world, long id, Faction owner, Item[] subItems) {
        super(world, id);
        this.product = product;
        this.owner = owner;
        this.subItems = subItems;
        this.state = State.STOCKED;
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
   

    public ItemState toState() {
        ItemState state  = new ItemState();
        state.id = getId();
        state.product = product.toState();
        
        switch (this.state) {
            case STOCKED:
                    state.state = ItemState.STOCKED;
                break;
            case DEPLOYED:
                state.state = ItemState.DEPLOYED;
            break;
            case DEPLOYING:
                state.state = ItemState.DEPLOYING;
            break;
            case RESERVED:
                state.state = ItemState.RESERVED;
            break;
            case DESTROYED:
                state.state = ItemState.DESTROYED;
            break;
            default:
                Log.error("Unsupported state");
                break;
        }
        
        return state;
    }
}
