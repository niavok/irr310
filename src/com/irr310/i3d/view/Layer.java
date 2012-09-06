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
    public void doDraw() {
        for (View widget : children) {
            widget.draw();
        }
    }
    
    @Override
    public View duplicate() {
        Layer view = new Layer(g);
        for (View widget : children) {
            view.addChild(widget.duplicate());
        }
        view.setLayout(getLayout().duplicate());
        return view;
    }
    
    public void addChild(View widget) {
        children.add(widget);
        widget.assignParent(this);
    }
    
    @Override
    public boolean doLayout(Layout parentLayout) {
        for (View widget : children) {
            widget.layout(layout);
        }
        return true;
    }

    @Override
    public void requestLayout() {
        // TODO Auto-generated method stub
        
    }

}
