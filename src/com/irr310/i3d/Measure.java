package com.irr310.i3d;

public class Measure {

    private final int value;
    private final boolean relative;

    public Measure(int value, boolean relative) {
        this.value = value;
        this.relative = relative;
    }

    public int getValue() {
        return value;
    }

    public boolean isRelative() {
        return relative;
    }
}
