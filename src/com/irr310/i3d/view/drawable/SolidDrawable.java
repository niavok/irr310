package com.irr310.i3d.view.drawable;

import com.irr310.i3d.Color;

public class SolidDrawable extends Drawable {

    private Color color;

    public SolidDrawable() {
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setBounds(float left, float top, float right, float bottom) {
        super.setBounds(left, top, right, bottom);
        g.setColor(color);
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
