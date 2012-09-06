package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;

public class Waiter extends View {

    public Waiter(Graphics g) {
        super(g);
    }

    @Override
    public void doDraw() {
        // TODO Auto-generated method stub
    }
    
    @Override
    public View duplicate() {
        View widget = new Waiter(g);
        return widget;
    }

    @Override
    public boolean doLayout(Layout parentLayout) {
        // TODO Auto-generated method stub
        return false;
    }
}
