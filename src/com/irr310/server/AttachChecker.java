package com.irr310.server;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.Component;
import com.irr310.common.world.Link;
import com.irr310.common.world.Ship;

public class AttachChecker {

    
    private Component kernel;
    private List<Component> attachedComponents = new ArrayList<Component>();
    private final Ship ship;

    public AttachChecker(Ship ship) {
        this.ship = ship;
        kernel = ship.getComponentByName("kernel");
    }

    private void propagateAttached(Component component) {
        if(!attachedComponents.contains(component)) {
            attachedComponents.add(component);
            
            for (Link link : ship.getLinks()) {
                if(link.getSlot1().getComponent() == component) {
                    propagateAttached(link.getSlot2().getComponent());
                } else if(link.getSlot2().getComponent() == component) {
                    propagateAttached(link.getSlot1().getComponent());
                } 
            }
        }
    }

    public void check() {
        attachedComponents.clear();
        propagateAttached(kernel);
        
        for (Component component : ship.getComponents()) {
            if(attachedComponents.contains(component)) {
                component.setAttached(true);
            } else {
                component.setAttached(false);
            }
        }
    }

}
