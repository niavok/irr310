package com.irr310.common.world.capacity;

import com.irr310.common.world.GameEntity;
import com.irr310.common.world.view.CapacityView;

public abstract class Capacity extends GameEntity{

    public Capacity(long id) {
        super(id);
    }

    public enum CapacityType {
        LINEAR_ENGINE
    }
    
    
    public abstract CapacityView toView();

    public abstract void fromView(CapacityView view);

    public static Capacity createFromType(long id, CapacityType type) {
        switch (type) {
            case LINEAR_ENGINE:
                return new LinearEngineCapacity(id);
        }
        return null;
    }
    
}
