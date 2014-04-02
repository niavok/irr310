package com.irr310.i3d.view.drawable;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Duplicable;
import com.irr310.i3d.Graphics;

public abstract class Drawable implements Duplicable<Drawable> {

    protected Graphics g;
    protected float bottom;
    protected float left;
    protected float top;
    protected float right;

    public void setGraphics(Graphics g) {
        this.g = g;
    }

    public void setBounds(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void begin(int glType) {
        GL11.glBegin(glType);
    }

    public void end() {
        GL11.glEnd();
    }

    public void vertex(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public  abstract void close();

    public void draw() {
        begin(GL11.GL_QUADS);
        vertex(left, top);
        vertex(right, top);
        vertex(right, bottom);
        vertex(left, bottom);
        end();
    }

    public abstract int getIntrinsicWidth();

    public abstract int getIntrinsicHeight();
    
    @Override
    public Drawable duplicate() {
        return this;
    }
}
