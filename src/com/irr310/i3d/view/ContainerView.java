package com.irr310.i3d.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.i3d.Graphics;

public abstract class ContainerView extends View implements ViewParent {

    List<View> children = new ArrayList<View>();
    
    public ContainerView() {
    }

    @Override
    public void onDraw(Graphics g) {
        for (View view : children) {
            view.draw(g);
        }
    }
    
//    @Override
//    public View duplicate() {
//        ContainerView view = new ContainerView(g);
//        for (View widget : children) {
//            view.addChild(widget.duplicate());
//        }
//        view.setLayout(getLayout().duplicate());
//        return view;
//    }
    
    public void addViewInLayout(View view) {
        children.add(view);
        view.assignParent(this);
        requestLayout();
    }
    
    public void removeView(View view) {
        children.remove(view);
        requestLayout();
    }
    
    public void removeAllView() {
        children.clear();
        requestLayout();
    }

    
    
    public List<View> getChildren() {
        return children;
    }
    
//    @Override
//    public boolean doLayout(Layout parentLayout) {
//        for (View widget : children) {
//            widget.layout(layout);
//        }
//        return true;
//    }
    
    
    

    @Override
    public void requestLayout() {
        if(getParent() != null) {
            getParent().requestLayout();
        }
    }
    
    @Override
    public View doFindViewById(String id) {
    	View outputView = null;
    	outputView = super.doFindViewById(id); 
    	if(outputView == null) {
    		for (View view : children) {
    			outputView = view.doFindViewById(id);
    			if(outputView != null) {
    				break;
    			}
            }
    	}
    	return outputView;
    }

}
