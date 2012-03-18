package com.irr310.common.engine;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Part;

public class CollisionDescriptor {

    private Part partB;
    private Part partA;
    private Vec3 localPositionOnA;
    private Vec3 localPositionOnB;
    private Vec3 globalPosition;
    private double impulse;

    public void setPartA(Part partA) {
        this.partA = partA;
    }

    public void setPartB(Part partB) {
        this.partB = partB;
        
    }

    public void setLocalPositionOnA(Vec3 position) {
        this.localPositionOnA = position;
    }
    
    public void setLocalPositionOnB(Vec3 position) {
        this.localPositionOnB = position;
    }

    
    public void setGlobalPosition(Vec3 position) {
        this.globalPosition = position;
    }

    public void setImpulse(double impulse) {
        this.impulse = impulse;
        
    }
    
    public Vec3 getGlobalPosition() {
        return globalPosition;
    }
    
    public double getImpulse() {
        return impulse;
    }
    
    public Vec3 getLocalPositionOnA() {
        return localPositionOnA;
    }
    
    public Vec3 getLocalPositionOnB() {
        return localPositionOnB;
    }
    
    public Part getPartA() {
        return partA;
    }
    
    public Part getPartB() {
        return partB;
    }

}
