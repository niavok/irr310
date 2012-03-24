package com.irr310.common.world.capacity.controller;

import java.util.Random;

import com.irr310.common.Game;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.DamageDescriptor;
import com.irr310.common.world.Part;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.capacity.ContactDetectorCapacity;

public class ContactDetectorController extends CapacityController {

    private final ContactDetectorCapacity capacity;
    private final Component component;

    public ContactDetectorController(Component component, ContactDetectorCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
    }

    @Override
    public void update(double duration) {
    }
    
    public  void contact(double impulse) {
        if(impulse > capacity.minImpulse) {
            capacity.triggerTarget.trigger(capacity.triggerCode);
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }

}
