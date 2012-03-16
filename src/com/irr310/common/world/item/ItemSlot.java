package com.irr310.common.world.item;

import com.irr310.common.Game;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Ship;
import com.irr310.server.game.ComponentFactory;

public class ItemSlot {

    private final Vec3 position;
    private Item content;
    private final Ship ship;
    private final Component slotComponent;
    private final Vec3 connectionPosition;
    private Component component;

    public ItemSlot(Ship ship, Component slotComponent, Vec3 position, Vec3 connectionPosition) {
        this.ship = ship;
        this.slotComponent = slotComponent;
        this.position = position;
        this.connectionPosition = connectionPosition;
        this.content = null;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setContent(Item content) {
        if (this.content == content) {
            return;
        }

        if (this.content != null) {
            component = this.content.getComponent();
            ship.remove(component);
            Game.getInstance().getWorld().removeComponent(component);
            this.content.setUsed(false);
        }

        this.content = content;
        if (content != null) {
            content.setUsed(true);
            Component component = ComponentFactory.createByItem(content);
            component.setShipPosition(position);
            Game.getInstance().getWorld().addComponent(component);
            ship.assign(component);
            ship.link(component, slotComponent, connectionPosition);
            content.setComponent(component);
        }
    }

    public Item getContent() {
        return content;
    }

    public boolean isEmpty() {
        return content == null;
    }

}
