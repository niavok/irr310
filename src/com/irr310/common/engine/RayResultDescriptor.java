package com.irr310.common.engine;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Part;

public class RayResultDescriptor implements Comparable<RayResultDescriptor>{

    private Part part;
    private Vec3 localPosition;
    private Vec3 globalPosition;
    private double hitFraction;
    
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
    public double getHitFraction() {
        return hitFraction;
    }
    public void setHitFraction(double hitFraction) {
        this.hitFraction = hitFraction;
    }
    @Override
    public int compareTo(RayResultDescriptor o) {
        if(hitFraction > o.getHitFraction()) {
            return 1;
        } else if(hitFraction < o.getHitFraction()) {
            return -1;
        } else {
            return 0;
        }
    }

  

}
