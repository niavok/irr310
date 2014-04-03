package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.input.I3dMouseEvent;

public class Waiter extends View {

    public Waiter() {
    }

    @Override
    public void onDraw(Graphics g) {
    }
    
    @Override
    public View duplicate() {
        View widget = new Waiter();
        return widget;
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
    }

    @Override
    public void onMeasure() {
    }
}
