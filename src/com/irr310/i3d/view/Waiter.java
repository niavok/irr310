package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;

public class Waiter extends View {

    public Waiter(Graphics g) {
        super(g);
    }

    @Override
    public void onDraw() {
    }
    
    @Override
    public View duplicate() {
        View widget = new Waiter(g);
        return widget;
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
    }

    @Override
    public void onMeasure() {
    }
}
