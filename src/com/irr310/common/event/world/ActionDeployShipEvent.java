package com.irr310.common.event.world;

import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.NexusState;

public class ActionDeployShipEvent extends WorldEvent {

    
    private final NexusState nexus;
    private final ItemState item;


    public ActionDeployShipEvent(ItemState item, NexusState nexus) {
        this.item = item;
        this.nexus = nexus;
        
    }
    
    public ItemState getItem() {
        return item;
    }
    
    public NexusState getNexus() {
        return nexus;
    }
 
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
