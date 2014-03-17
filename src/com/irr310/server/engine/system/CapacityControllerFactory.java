package com.irr310.server.engine.system;

import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.ContactDetectorCapacity;
import com.irr310.common.world.capacity.ExplosiveCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.RocketCapacity;
import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.common.world.capacity.controller.ContactDetectorController;
import com.irr310.common.world.capacity.controller.ExplosiveCapacityController;
import com.irr310.common.world.capacity.controller.GunController;
import com.irr310.common.world.capacity.controller.LinearEngineController;
import com.irr310.common.world.capacity.controller.RocketController;
import com.irr310.common.world.capacity.controller.RocketPodController;
import com.irr310.common.world.capacity.controller.ShotgunController;
import com.irr310.common.world.system.Component;

public class CapacityControllerFactory {

    public CapacityController createController(Component component, Capacity capacity) {
        if (capacity instanceof LinearEngineCapacity) {
            return new LinearEngineController(component, (LinearEngineCapacity) capacity);
        }
        if (capacity instanceof BalisticWeaponCapacity) {
            if (capacity.getName().equals("gun")) {
                return new GunController(component, (BalisticWeaponCapacity) capacity);
            } else if (capacity.getName().equals("shotgun")) {
                return new ShotgunController(component, (BalisticWeaponCapacity) capacity);
            }
        } else if (capacity instanceof RocketWeaponCapacity) {
            if (capacity.getName().equals("rocketpod")) {
                return new RocketPodController(component, (RocketWeaponCapacity) capacity);
            }
        }
        throw new RuntimeException("Not implemented");
//        } else if (capacity instanceof ExplosiveCapacity) {
//            return new ExplosiveCapacityController(component, (ExplosiveCapacity) capacity));
//        } else if (capacity instanceof ContactDetectorCapacity) {
//            ContactDetectorController contactDetectorController = new ContactDetectorController(component, (ContactDetectorCapacity) capacity);
//            contactDetectorMap.put(component, contactDetectorController);
//            addCapacityController(contactDetectorController);
//        } else if (capacity instanceof RocketCapacity) {
//            addCapacityController(new RocketController(component, (RocketCapacity) capacity));
//        }
    }

}
