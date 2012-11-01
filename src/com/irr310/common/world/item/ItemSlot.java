package com.irr310.common.world.item;

import com.irr310.common.Game;
import com.irr310.common.event.ComponentRemovedEvent.Reason;
import com.irr310.common.event.InventoryChangedEvent;
import com.irr310.common.event.InventoryChangedEvent.ChangeType;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
import com.irr310.server.game.ComponentFactory;

public class ItemSlot {

    private final Vec3 position;
    private ItemOld content;
    private final Ship ship;
    private final Component slotComponent;
    private final Vec3 connectionPosition;
    private Component component;

    public ItemSlot(Ship ship, Component slotComponent, Vec3 position, Vec3 connectionPosition) {
        assert(slotComponent != null);
        this.ship = ship;
        this.slotComponent = slotComponent;
        this.position = position;
        this.connectionPosition = connectionPosition;
        this.content = null;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setContent(ItemOld content) {
//        if (this.content == content) {
//            return;
//        }
//
//        if (this.content != null) {
//            component = this.content.getComponent();
//            ship.remove(component);
//            Game.getInstance().getWorld().removeComponent(component, Reason.INVENTORY);
//            this.content.setUsed(false);
//            Game.getInstance().sendToAll(new InventoryChangedEvent(ship.getOwner(), content, ChangeType.DESACTIVATED));
//        }
//
//        this.content = content;
//        if (content != null) {
//            content.setUsed(true);
//            Component component = ComponentFactory.createByItem(content);
//            component.setShipPosition(position);
//            ship.assign(component);
//            ship.link(component, slotComponent, connectionPosition);
//            Game.getInstance().getWorld().addComponent(component);
//            content.setComponent(component);
//            Game.getInstance().sendToAll(new InventoryChangedEvent(ship.getOwner(), content, ChangeType.ACTIVATED));
//        }
    }

    public ItemOld getContent() {
        return content;
    }

    public boolean isEmpty() {
        return content == null;
    }

}
