package com.irr310.common.tools;


public class Vect2 {

    public Double x;
    public Double y;

    public Vect2() {
        this(0, 0);
    }

    public Vect2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vect2(Vect2 v) {
        this.x = v.x;
        this.y = v.y;
    }
}
