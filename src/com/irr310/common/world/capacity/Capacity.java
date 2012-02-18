package com.irr310.common.world.capacity;

import com.irr310.common.world.GameEntity;
import com.irr310.common.world.view.CapacityView;

public abstract class Capacity extends GameEntity{

    private final String name;

    public Capacity(long id, String name) {
        super(id);
        this.name = name;
    }

    public enum CapacityType {
        LINEAR_ENGINE,
        WING,
        GUN,
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
                return new GunCapacity(id);
            default:
                System.err.println("Not implemented capacity type: "+type.toString());
        }
        return null;
    }

    public String getName() {
        return name;
    }
    
}
