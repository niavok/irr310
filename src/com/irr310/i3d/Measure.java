package com.irr310.i3d;


public class Measure {

    private float value;
    private final boolean relative;
    private final Axis axis;
    
    public enum Axis {
        HORIZONTAL,
        VERTICAL
    }

    public Measure(float value, boolean relative, Axis axis) {
        this.value = value;
        this.relative = relative;
        this.axis = axis;
    }

    public Measure(Measure measure) {
        this.value = measure.value;
        this.relative = measure.relative;
        this.axis = measure.axis;
    }

    public void setValue(float value) {
		this.value = value;
	}

    public float getValue() {
        return value;
    }

    public boolean isRelative() {
        return relative;
    }
    
    public Axis getAxis() {
        return axis;
    }
}
