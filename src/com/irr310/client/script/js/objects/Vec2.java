package com.irr310.client.script.js.objects;

public class Vec2 {

    public final double y;
    public final double x;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2 minus(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }
}
