package com.irr310.i3d;

import com.irr310.i3d.view.Drawable;

public class GradientDrawable extends Drawable {

    private Color startColor;
    private Color stopColor;

    public GradientDrawable() {
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }
    
    public void setStopColor(Color stopColor) {
        this.stopColor = stopColor;
    }

}
