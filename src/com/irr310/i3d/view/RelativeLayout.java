package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

public class RelativeLayout extends ContainerView {

	public RelativeLayout(Graphics g) {
		super(g);
	}
	
	@Override
    public void onLayout(float l, float t, float r, float b) {
	    
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(layoutParams);
            view.layout(childLayoutParams.mComputedLeft, childLayoutParams.mComputedTop, childLayoutParams.mComputedRight, childLayoutParams.mComputedBottom);
            view.layout(childLayoutParams.mComputedLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft())  , childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()), childLayoutParams.mComputedRight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()), childLayoutParams.mComputedBottom -  + layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()));
        }
    }

    @Override
    public View duplicate() {
        RelativeLayout view = new RelativeLayout(g);
        for (View childView : children) {
            view.addChild(childView.duplicate());
        }
        view.setLayout(getLayoutParams().duplicate());
        return view;
    }

    @Override
    public void onMeasure() {
        float measuredWidth = 0;
        float measuredHeight= 0;
        
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
        
        
        for (View view : children) {
            view.measure();
            if(view.getLayoutParams().mContentWidth > measuredWidth) {
                measuredWidth = view.getLayoutParams().mContentWidth;
            }
            
            if(view.getLayoutParams().mContentHeight > measuredHeight) {
                measuredHeight = view.getLayoutParams().mContentHeight;
            }
        }
        
        
        if(layoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getX().isRelative()) {
            layoutParams.mContentWidth = measuredWidth;
        }
        if(layoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getY().isRelative()) {
            layoutParams.mContentHeight = measuredHeight;
        }
    }
    
}
