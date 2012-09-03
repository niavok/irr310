package com.irr310.i3d.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.i3d.Graphics;

public class AbsoluteLayout extends View implements ViewParent {

    List<View> children = new ArrayList<View>();
    
    public AbsoluteLayout(Graphics g) {
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
        AbsoluteLayout absoluteLayout = new AbsoluteLayout(g);
        for (View widget : children) {
            absoluteLayout.addChild(widget.duplicate());
        }
        return absoluteLayout;
    }

    @Override
    public boolean computeSize() {
        for (View view : children) {
            view.computeSize();
        }
        return true;
    }
    
    public void addChild(View widget) {
        children.add(widget);
        widget.assignParent(this);
    }

    @Override
    public void requestLayout() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Layout getLayout() {
        return null;
    }

}
