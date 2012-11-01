package com.irr310.common.world.capacity;

import com.irr310.common.world.system.GameEntity;
import com.irr310.common.world.view.CapacityView;

public abstract class Capacity extends GameEntity{

    private String name;

    public Capacity(long id) {
        super(id);
        this.name = "undefined";
    }

    public enum CapacityType {
        LINEAR_ENGINE,
        WING,
        GUN,
        ROCKET,
    }
    
    public abstract CapacityView toView();

    public abstract void fromView(CapacityView view);

    public static Capacity createFromType(long id, CapacityType type) {
        switch (type) {
            case LINEAR_ENGINE:
                return new LinearEngineCapacity(id);
            case WING:
                return new WingCapacity(id);
            case GUN:
                return new BalisticWeaponCapacity(id);
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
