package com.irr310.i3d;

import com.irr310.i3d.Measure.Axis;

public class MeasurePoint {

    private Measure x;
    private Measure y;

    MeasurePoint(Measure x, Measure y) {
        this.x = x;
        this.y = y;
    }

    public MeasurePoint() {
        x = new Measure(0, false, Axis.HORIZONTAL);
        y = new Measure(0, false, Axis.VERTICAL);
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
