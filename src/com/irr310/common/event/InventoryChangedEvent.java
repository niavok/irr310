package com.irr310.common.event;

import com.irr310.common.world.Player;
import com.irr310.common.world.item.Item;

public class InventoryChangedEvent extends EngineEvent {

    private final Item item;
    private final Player player;
    private final boolean added;

    public InventoryChangedEvent(Player player, Item item, boolean added) {
        this.item = item;
        this.player = player;
        this.added = added;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public Player getPlayer() {
        return player;
    }

    public Item getItem() {
        return item;
    }

    public boolean isAdded() {
        return added;
    }

}
