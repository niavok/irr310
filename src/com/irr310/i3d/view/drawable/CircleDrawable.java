package com.irr310.i3d.view.drawable;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;

public class CircleDrawable extends Drawable {

    private Color color;
    private int quality;
    private float radius;
    private float offsetX;
    private float offsetY;

    public CircleDrawable() {
        quality = 8;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setQuality(int quality) {
        this.quality = quality;
    }

    @Override
    public void setBounds(float left, float top, float right, float bottom) {
        super.setBounds(left, top, right, bottom);
        g.setColor(color);
        radius = Math.min(right - left, bottom - top) / 2;
        offsetX = left + radius;
        offsetY = top + radius;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public int getIntrinsicWidth() {
        return 1;
    }

    @Override
    public int getIntrinsicHeight() {
        return 1;
    }
    
    @Override
    public void draw() {
        begin(GL11.GL_TRIANGLE_FAN);

        
        vertex(offsetX, offsetY); // center

        float step = 2f*(float) Math.PI / (float) quality;
        
        for(int i = 0; i <= quality; i++) {
            vertex((float) ( offsetX+ radius * Math.cos(step*i)), (float) (offsetY + radius * Math.sin(step*i)));
        }

       end();

    }
    
}
