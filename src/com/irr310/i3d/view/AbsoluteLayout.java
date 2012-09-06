package com.irr310.i3d.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.Layout.LayoutMeasure;

public class AbsoluteLayout extends View implements ViewParent {

    List<View> children = new ArrayList<View>();

    public AbsoluteLayout(Graphics g) {
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
        AbsoluteLayout view = new AbsoluteLayout(g);
        for (View widget : children) {
            view.addChild(widget.duplicate());
        }
        view.setLayout(getLayout().duplicate());
        return view;
    }

    @Override
    public boolean doLayout(Layout parentLayout) {
        
        for (View view : children) {
            view.layout(layout);
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

}
