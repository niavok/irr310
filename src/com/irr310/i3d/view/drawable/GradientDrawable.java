package com.irr310.i3d.view.drawable;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;

public class GradientDrawable extends Drawable {

    private Color startColor;
    private Color stopColor;
    private int angle = 0;

    public GradientDrawable() {
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }
    
    public void setStopColor(Color stopColor) {
        this.stopColor = stopColor;
    }
    
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    @Override
    public void setBounds(float left, float top, float right, float bottom) {
        super.setBounds(left, top, right, bottom);
    }
       
    
    @Override
    public void vertex(float x, float y) {
        float mix =0;
        
        if(angle == 0) {
            mix = (y - top) / (bottom - top);
        } else  {
            mix = (x - left) / (right- left);
        }
        
        g.setColor(Color.mix(startColor, stopColor, mix));
        super.vertex(x, y);
    }

    
    @Override
    public void close() {
        
    }

    @Override
    public int getIntrinsicWidth() {
        return -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return -1;
    }

}
