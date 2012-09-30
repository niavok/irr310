package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

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
        
        if(!layoutParams.getLayoutMarginTop().isRelative()) {
            measuredHeight +=   layoutParams.computeMesure(layoutParams.getLayoutMarginTop());  
        }
        if(!layoutParams.getLayoutMarginBottom().isRelative()) {
            measuredHeight +=   layoutParams.computeMesure(layoutParams.getLayoutMarginBottom());  
        }
        if(!layoutParams.getLayoutMarginLeft().isRelative()) {
            measuredWidth +=   layoutParams.computeMesure(layoutParams.getLayoutMarginLeft());  
        }
        if(!layoutParams.getLayoutMarginRight().isRelative()) {
            measuredWidth +=   layoutParams.computeMesure(layoutParams.getLayoutMarginRight());  
        }
        

        if(layoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getX().isRelative()) {
            layoutParams.mContentWidth = measuredWidth;
        }
        if(layoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getY().isRelative()) {
            layoutParams.mContentHeight = measuredHeight;
        }
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
            
            view.layout(currentLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft())  , childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()), currentLeft + computedWidth -  + layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()), childLayoutParams.mComputedBottom - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()));
            
            currentLeft += computedWidth;
        }
    }

    private void layoutVertical() {
        
        int currentTop = 0;
        
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            float computedHeight = childLayoutParams.mComputedBottom - childLayoutParams.mComputedTop;
            
            view.layout(childLayoutParams.mComputedLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft())  , currentTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()), childLayoutParams.mComputedRight -  + layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()), currentTop + computedHeight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()));
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
