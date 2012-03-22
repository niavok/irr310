package com.irr310.client.script.js.objects;

import java.util.ArrayList;
import java.util.List;


import com.irr310.common.Game;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;

public class Component {

    private com.irr310.common.world.Component component;

    public Component(long id) {
        component = Game.getInstance().getWorld().getComponentBy(id);
    }

    public long getId() {
        return component.getId();
    }

    @SuppressWarnings("unchecked")
    public List<Capacity> getCapacitiesByClass(String className) {
        List<Capacity> outCapacities = new ArrayList<Capacity>();
        
        Class<com.irr310.common.world.capacity.Capacity> clazz;
        try {
            clazz = (Class<com.irr310.common.world.capacity.Capacity>) Class.forName("com.irr310.common.world.capacity."
                    + className);
        } catch (ClassNotFoundException e) {
            Log.warn("Class call from js with getCapacitiesByClass not found: "+className);
            return outCapacities;
        }

        List<com.irr310.common.world.capacity.Capacity> capacities = component.getCapacitiesByClass(clazz);

        for (com.irr310.common.world.capacity.Capacity capacity : capacities) {
            if (capacity instanceof com.irr310.common.world.capacity.LinearEngineCapacity) {
                outCapacities.add(new LinearEngineCapacity((com.irr310.common.world.capacity.LinearEngineCapacity) capacity));
            } else if (capacity instanceof com.irr310.common.world.capacity.BalisticWeaponCapacity) {
                outCapacities.add(new BalisticWeaponCapacity((com.irr310.common.world.capacity.BalisticWeaponCapacity) capacity));
            } else if (capacity instanceof com.irr310.common.world.capacity.RocketWeaponCapacity) {
                outCapacities.add(new RocketWeaponCapacity((com.irr310.common.world.capacity.RocketWeaponCapacity) capacity));
            }
        }

        return outCapacities;
    }

    public Vec3 getLinearSpeed() {
        return component.getFirstPart().getLinearSpeed();
    }

    public Vec3 getPosition() {
        return component.getFirstPart().getTransform().getTranslation();
    }

    public TransformMatrix getTransform() {
        return component.getFirstPart().getTransform();
    }

    public Vec3 getRotationSpeed() {
        return component.getFirstPart().getRotationSpeed();
    }
}
