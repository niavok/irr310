package com.irr310.i3d;

import com.irr310.i3d.view.Drawable;

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

}
