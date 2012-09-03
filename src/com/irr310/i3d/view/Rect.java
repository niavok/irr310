package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.Color;
import com.irr310.i3d.I3dVec2;

public class Rect extends View {

    private I3dVec2 size = new I3dVec2(10,10);
    private Color backgroundColor;

    public Rect(Graphics g) {
        super(g);
        backgroundColor = Color.pink;
    }

    @Override
    public void draw() {
        g.setColor(backgroundColor);
        g.drawFilledRectangle(0, 0, size.getWidth(), size.getHeight());
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    public void setSize(I3dVec2 size) {
        this.size = size;
    }
    
    @Override
    public View duplicate() {
        Rect widget = new Rect(g);
        widget.setBackgroundColor(backgroundColor);
        widget.setSize(size);
        return widget;
    }
    
    @Override
    public boolean computeSize() {
        // TODO Auto-generated method stub
        return false;
    }
}
