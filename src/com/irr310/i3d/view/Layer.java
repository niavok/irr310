package com.irr310.i3d.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.i3d.Graphics;

public class Layer extends View implements ViewParent {

    List<View> children = new ArrayList<View>();
    
    public Layer(Graphics g) {
        super(g);
    }

    @Override
    public void draw() {
        for (View widget : children) {
            widget.draw();
        }
    }
    
    @Override
    public View duplicate() {
        Layer absoluteLayout = new Layer(g);
        for (View widget : children) {
            absoluteLayout.addChild(widget.duplicate());
        }
        return absoluteLayout;
    }
    
    public void addChild(View widget) {
        children.add(widget);
        widget.assignParent(this);
    }
    
    @Override
    public boolean computeSize() {
        for (View widget : children) {
            widget.computeSize();
        }
        return true;
    }

    @Override
    public void requestLayout() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Layout getLayout() {
        // TODO Auto-generated method stub
        return null;
    }

}
