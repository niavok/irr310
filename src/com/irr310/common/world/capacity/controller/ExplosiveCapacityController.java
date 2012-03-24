package com.irr310.common.world.capacity.controller;

import com.irr310.common.Game;
import com.irr310.common.event.ExplosionFiredEvent;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.ExplosiveCapacity;

public class ExplosiveCapacityController extends CapacityController {

    private final ExplosiveCapacity capacity;
    private final Component component;
    private double time;

    public ExplosiveCapacityController(Component component, ExplosiveCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
        this.time = 0;
    }

    @Override
    public void update(double duration) {
        
        
        
        if(capacity.fire && !capacity.consumed ) {
            capacity.consumed = true;
            Game.getInstance().sendToAll(new ExplosionFiredEvent(component.getFirstPart(), component.getFirstPart().getTransform().getTranslation(), capacity.armorPenetration, capacity.explosionBlast, capacity.explosionRadius, capacity.explosionDamage));
        }
        
        if(time > capacity.disarmTimeout) {
            Game.getInstance().sendToAll(new ExplosionFiredEvent(component.getFirstPart(), component.getFirstPart().getTransform().getTranslation(), 0, capacity.explosionBlast/10, capacity.explosionRadius/10, capacity.explosionDamage));
        }
        
        time += duration;
    }

    @Override
    public Component getComponent() {
        return component;
    }
}
