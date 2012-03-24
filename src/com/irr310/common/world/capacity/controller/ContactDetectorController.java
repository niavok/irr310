package com.irr310.common.world.capacity.controller;

import com.irr310.common.tools.Log;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.capacity.ContactDetectorCapacity;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class ContactDetectorController extends CapacityController {

    private final ContactDetectorCapacity capacity;
    private final Component component;
    private double time;

    public ContactDetectorController(Component component, ContactDetectorCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
        time = 0;
    }

    @Override
    public void update(double duration) {
        time += duration;
    }
    
    public  void contact(double impulse, Part target) {
        if(impulse > capacity.minImpulse && time > capacity.minTime) {
            
            //Check auto kill
            if(target.getParentObject() instanceof Component) {
                Log.trace("contact on component");
                Component component = (Component) target.getParentObject();
                if(component.getShip().equals(capacity.sourceShip)) {
                    Log.trace("save autokill");
                    return;
                }
            }
            
            capacity.triggerTarget.trigger(capacity.triggerCode);
        }
    }

    @Override
    public Component getComponent() {
        return component;
    }

}
