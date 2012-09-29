package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;

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
        
        for (View view : children) {
            view.measure();
            if(view.getLayoutParams().mContentWidth > measuredWidth) {
                measuredWidth = view.getLayoutParams().mContentWidth;
            }
            
            if(view.getLayoutParams().mContentHeight > measuredHeight) {
                measuredHeight = view.getLayoutParams().mContentHeight;
            }
        }
        
        layoutParams.mContentWidth = measuredWidth;
        layoutParams.mContentHeight = measuredHeight;
    }
    
}
