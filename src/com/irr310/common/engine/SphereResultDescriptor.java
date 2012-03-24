package com.irr310.common.engine;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Part;

public class SphereResultDescriptor{

    private Part part;
    private Vec3 localPosition;
    private Vec3 globalPosition;
    private Vec3 distance;
    
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

    public void setDistance(Vec3 distance) {
        this.distance = distance;
        
    }
    
    public Vec3 getDistance() {
        return distance;
    }

  

}
