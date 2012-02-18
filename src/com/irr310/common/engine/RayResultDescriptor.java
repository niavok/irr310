package com.irr310.common.engine;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Part;

public class RayResultDescriptor {

    private Part part;
    private Vect3 localPosition;
    private Vect3 globalPosition;
    private float hitFraction;
    
    public Part getPart() {
        return part;
    }
    public void setPart(Part part) {
        this.part = part;
    }
    public Vect3 getLocalPosition() {
        return localPosition;
    }
    public void setLocalPosition(Vect3 localPosition) {
        this.localPosition = localPosition;
    }
    public Vect3 getGlobalPosition() {
        return globalPosition;
    }
    public void setGlobalPosition(Vect3 globalPosition) {
        this.globalPosition = globalPosition;
    }
    public float getHitFraction() {
        return hitFraction;
    }
    public void setHitFraction(float hitFraction) {
        this.hitFraction = hitFraction;
    }

  

}
