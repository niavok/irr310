package com.irr310.i3d;

public class MeasurePoint {

    private Measure x;
    private Measure y;

    MeasurePoint(Measure x, Measure y) {
        this.x = x;
        this.y = y;
    }

    public MeasurePoint() {
        x = new Measure(0, false);
        y = new Measure(0, false);
    }

    public Measure getX() {
        return x;
    }

    public Measure getY() {
        return y;
    }

    public void setX(Measure x) {
        this.x = x;
    }

    public void setY(Measure y) {
        this.y = y;
    }

}
