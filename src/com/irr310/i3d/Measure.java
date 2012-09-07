package com.irr310.i3d;

public class Measure {

    private float value;
    private final boolean relative;

    public Measure(float value, boolean relative) {
        this.value = value;
        this.relative = relative;
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
}
