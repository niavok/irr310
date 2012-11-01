package com.irr310.common.world.item;

import com.irr310.common.world.Faction;
import com.irr310.common.world.system.GameEntity;

public class Item extends GameEntity {
    
    enum ItemType {
        BUILDING
    }
    
    private final Faction owner;
    private Faction usufruct;

    private boolean deployable;
    private ItemType type;

    
    public Item(long id, Faction owner) {
        super(id);
        this.owner = owner;
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

}
