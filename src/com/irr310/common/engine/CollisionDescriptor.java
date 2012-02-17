package com.irr310.common.engine;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Part;

public class CollisionDescriptor {

    private Part partB;
    private Part partA;
    private Vect3 localPositionOnA;
    private Vect3 localPositionOnB;
    private Vect3 globalPosition;
    private float impulse;

    public void setPartA(Part partA) {
        this.partA = partA;
    }

    public void setPartB(Part partB) {
        this.partB = partB;
        
    }

    public void setLocalPositionOnA(Vect3 position) {
        this.localPositionOnA = position;
    }
    
    public void setLocalPositionOnB(Vect3 position) {
        this.localPositionOnB = position;
    }

    
    public void setGlobalPosition(Vect3 position) {
        this.globalPosition = position;
    }

    public void setImpulse(float impulse) {
        this.impulse = impulse;
        
    }
    
    public Vect3 getGlobalPosition() {
        return globalPosition;
    }
    
    public float getImpulse() {
        return impulse;
    }
    
    public Vect3 getLocalPositionOnA() {
        return localPositionOnA;
    }
    
    public Vect3 getLocalPositionOnB() {
        return localPositionOnB;
    }

}
