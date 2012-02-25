package com.irr310.common.engine;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Part;

public class RayResultDescriptor {

    private Part part;
    private Vec3 localPosition;
    private Vec3 globalPosition;
    private float hitFraction;
    
    public Part getPart() {
        return part;
    }
    public void setPart(Part part) {
        this.part = part;
    }
    public Vec3 getLocalPosition() {
        return localPosition;
    }
    public void setLocalPosition(Vec3 localPosition) {
        this.localPosition = localPosition;
    }
    public Vec3 getGlobalPosition() {
        return globalPosition;
    }
    public void setGlobalPosition(Vec3 globalPosition) {
        this.globalPosition = globalPosition;
    }
    public float getHitFraction() {
        return hitFraction;
    }
    public void setHitFraction(float hitFraction) {
        this.hitFraction = hitFraction;
    }

  

}
