package com.irr310.common.world.capacity;

import com.irr310.common.world.World;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.common.world.system.WorldSystemEntity;

public abstract class Capacity extends WorldSystemEntity{

    private String name;
    private Component mComponent;

    public Capacity(WorldSystem worldSystem, long id) {
        super(worldSystem, id);
        this.name = "undefined";
    }

    //TODO check usage
    public enum CapacityType {
        LINEAR_ENGINE,
        WING,
        GUN,
        ROCKET,
    }

    public void setComponent(Component component) {
        mComponent = component;
    }

    public Component getComponent() {
        return mComponent;
    }
    
//    public static Capacity createFromType(World world, long id, CapacityType type) {
//        switch (type) {
//            case LINEAR_ENGINE:
//                return new LinearEngineCapacity(world, id);
//            case WING:
//                return new WingCapacity(world, id);
//            case GUN:
//                return new BalisticWeaponCapacity(world, id);
//            default:
//                System.err.println("Not implemented capacity type: "+type.toString());
//        }
//        return null;
//    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void trigger(String triggerCode) {
    }
    
}
