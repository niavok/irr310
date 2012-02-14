package com.irr310.client.script.js.objects;

public class Color {

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }

}
