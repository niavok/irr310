package com.irr310.common.world.item;

import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldEntity;

public class Item extends WorldEntity {
    
    enum ItemType {
        BUILDING
    }
    
    private final Faction owner;
    private Faction usufruct;

    private boolean deployable;
    private ItemType type;
    private boolean reserved;

    
    public Item(World world, long id, Faction owner) {
        super(world, id);
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

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
        
    }

}
