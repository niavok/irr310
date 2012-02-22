package com.irr310.common.world.capacity.controller;

import com.irr310.common.Game;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.capacity.GunCapacity;

public class GunController extends CapacityController {

    private final GunCapacity capacity;
    private final Component component;
    private int nextGun = 0;

    public GunController(Component component, GunCapacity capacity) {
        this.component = component;
        this.capacity = capacity;
    }

    @Override
    public void update(double duration) {
        if(capacity.fire) {
            capacity.fire = false;
            System.err.println("Gun want fire !");          
            
            Part part = component.getFirstPart();
            
            double xoffset = 0;
            double yoffset = 0;
            
            switch (nextGun) {
                case 0:
                    xoffset = -0.14;
                    yoffset = 0.14;
                    break;
                case 1:
                    xoffset = 0.14;
                    yoffset = 0.14;
                    break;
                case 2:
                    xoffset = 0.14;
                    yoffset = -0.14;
                    break;
                case 3:
                    xoffset = -0.14;
                    yoffset = -0.14;
                    break;

                default:
                    break;
            }
            
            Vect3 from = new Vect3(xoffset, 0, yoffset).transform(part.getTransform());
            Vect3 to = new Vect3(xoffset, capacity.range, yoffset).transform(part.getTransform());
            
            
            
            Game.getInstance().sendToAll(new BulletFiredEvent(part, component.getEfficiency() * capacity.damage, capacity.range, capacity.damageType, from, to));
            
            nextGun++;
            if(nextGun > 3) {
                nextGun = 0;
            }
            
        }
    }

}
