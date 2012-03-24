package com.irr310.common.world.capacity.controller;

import com.irr310.common.Game;
import com.irr310.common.event.ExplosionFiredEvent;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.ExplosiveCapacity;

public class ExplosiveCapacityController extends CapacityController {

    private final ExplosiveCapacity capacity;
    private final Component component;

    public ExplosiveCapacityController(Component component, ExplosiveCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
    }

    @Override
    public void update(double duration) {
        if(capacity.fire && !capacity.consumed) {
            capacity.consumed = true;
            Game.getInstance().sendToAll(new ExplosionFiredEvent(component.getFirstPart(), component.getFirstPart().getTransform().getTranslation(), capacity.armorPenetration, capacity.explosionBlast, capacity.explosionRadius, capacity.explosionDamage));
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }
}
