package com.irr310.i3d.view;

import com.irr310.common.tools.Vec2;



public class Point {
    public float x;
    public float y;
    
    public Point() {
        this.x = 0;
        this.y = 0;
    }
    
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Point divide(float d) {
        return new Point(this.x / d, this.y / d);
    }
    
    public Point multiply(float d) {
        return new Point(x * d, y * d);
    }
    
    public Point add(Point p) {
        return new Point(p.x + x, p.y + y);
    }
    
    public Point minus(Point p) {
        return new Point(x - p.x, y - p.y);
    }
    
    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }
    
    public double distanceTo(Point p) {
        return this.diff(p).length();
    }
    
    public Point diff(Point p) {
        return new Point(p.x - x, p.y - y);
    }
    
    public double length() {
        return Math.sqrt(x * x + y * y);
    }
    
    public Point rotate(double angle) {
        return new Point((float)(x* Math.cos(angle) - y * Math.sin(angle)),(float) (x* Math.sin(angle) + y * Math.cos(angle)));
    }
}
