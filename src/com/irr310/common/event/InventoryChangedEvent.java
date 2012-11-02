package com.irr310.common.event;

import com.irr310.common.world.Player;
//
//public class InventoryChangedEvent extends EngineEvent {
//
//    private final ItemOld item;
//    private final Player player;
//    
//    public enum ChangeType {
//        ADDED,
//        REMOVE,
//        ACTIVATED,
//        DESACTIVATED
//    }
//    private final ChangeType change;
//
//    
//    public InventoryChangedEvent(Player player, ItemOld item, ChangeType change) {
//        this.item = item;
//        this.player = player;
//        this.change = change;
//    }
//
//    @Override
//    public void accept(EngineEventVisitor visitor) {
//        visitor.visit(this);
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
//    public ItemOld getItem() {
//        return item;
//    }
//
//    public ChangeType getChange() {
//        return change;
//    }
//
//}
