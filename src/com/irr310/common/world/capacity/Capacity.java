package com.irr310.common.world.capacity;

import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldEntity;

public abstract class Capacity extends WorldEntity{

    private String name;

    public Capacity(World world, long id) {
        super(world, id);
        this.name = "undefined";
    }

    public enum CapacityType {
        LINEAR_ENGINE,
        WING,
        GUN,
        ROCKET,
    }
    
    public static Capacity createFromType(World world, long id, CapacityType type) {
        switch (type) {
            case LINEAR_ENGINE:
                return new LinearEngineCapacity(world, id);
            case WING:
                return new WingCapacity(world, id);
            case GUN:
                return new BalisticWeaponCapacity(world, id);
            default:
                System.err.println("Not implemented capacity type: "+type.toString());
        }
        return null;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void trigger(String triggerCode) {
    }
    
}
