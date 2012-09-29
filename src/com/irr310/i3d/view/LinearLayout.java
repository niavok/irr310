package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;

public class LinearLayout extends ContainerView {

    private LayoutOrientation orientation = LayoutOrientation.HORIZONTAL;

    public enum LayoutOrientation {
        HORIZONTAL,
        VERTICAL,
    }
    
	public LinearLayout(Graphics g) {
		super(g);
	}

    public void setLayoutOrientation(LayoutOrientation orientation) {
        this.orientation = orientation;
        
    }

    @Override
    public void onMeasure() {
        float measuredWidth = 0;
        float measuredHeight= 0;
        
        if(orientation == LayoutOrientation.HORIZONTAL) {
            for (View view : children) {
                view.measure();
                measuredWidth += view.getLayoutParams().mContentWidth;
                if(view.getLayoutParams().mContentHeight > measuredHeight) {
                    measuredHeight = view.getLayoutParams().mContentHeight;
                }
            }
        } else {
            for (View view : children) {
                view.measure();
                measuredHeight += view.getLayoutParams().mContentHeight;
                if(view.getLayoutParams().mContentWidth > measuredWidth) {
                    measuredWidth = view.getLayoutParams().mContentWidth;
                }
            }
        }
        layoutParams.mContentWidth = measuredWidth;
        layoutParams.mContentHeight = measuredHeight;
    }
    
    @Override
    public void onLayout(float l, float t, float r, float b) {
        
        if(orientation == LayoutOrientation.VERTICAL) {
            layoutVertical();
        } else {
            layoutHorizontal();
        }
    }

    private void layoutHorizontal() {
        int currentLeft = 0;
        
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            float computedWidth = childLayoutParams.mComputedRight - childLayoutParams.mComputedLeft;
            
            view.layout(currentLeft, childLayoutParams.mComputedTop, currentLeft + computedWidth, childLayoutParams.mComputedBottom);
            currentLeft += computedWidth;
        }
    }

    private void layoutVertical() {
        
        int currentTop = 0;
        
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            float computedHeight = childLayoutParams.mComputedBottom - childLayoutParams.mComputedTop;
            
            view.layout(childLayoutParams.mComputedLeft, currentTop, childLayoutParams.mComputedRight, currentTop + computedHeight);
            currentTop += computedHeight;
        }
    }

    @Override
    public View duplicate() {
        LinearLayout view = new LinearLayout(g);
        for (View widget : children) {
            view.addChild(widget.duplicate());
        }
        view.setLayoutOrientation(orientation);
        view.setLayout(getLayoutParams().duplicate());
        return view;
    }

    

    
}
