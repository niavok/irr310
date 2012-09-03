package com.irr310.i3d;

public class I3dMesure {

    private final int valueX;
    private final int valueY;
    private final boolean relativeX;
    private final boolean relativeY;

    public I3dMesure(int valueX, int valueY, boolean relativeX, boolean relativeY) {
        this.valueX = valueX;
        this.valueY = valueY;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    public int getValueX() {
        return valueX;
    }

    public int getValueY() {
        return valueY;
    }

    public boolean isRelativeX() {
        return relativeX;
    }

    public boolean isRelativeY() {
        return relativeY;
    }
}
