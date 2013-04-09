package com.irr310.i3d.view;

import org.newdawn.slick.util.Log;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class RelativeLayout extends ContainerView {

	public RelativeLayout() {
	}
	
	@Override
    public void onLayout(float l, float t, float r, float b) {
	    
	    /**
	     * The extras size is used when the real display position is requered, as for ScrollView scroll limits
	     */
	    
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(layoutParams);
            //view.layout(childLayoutParams.mComputedLeft, childLayoutParams.mComputedTop, childLayoutParams.mComputedRight, childLayoutParams.mComputedBottom);
            float left = childLayoutParams.mComputedLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft());
            float top = childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop());
            float right = childLayoutParams.mComputedRight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight());
            float bottom = childLayoutParams.mComputedBottom - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom());
            
            view.layout(left  ,
                        top,
                        right,
                        bottom);
            
            if(left < getLayoutParams().mExtraLeft) {
                getLayoutParams().mExtraLeft = left;
            }
            
            if(top < getLayoutParams().mExtraTop) {
                getLayoutParams().mExtraTop= top;
            }
            
            if(right > getLayoutParams().mExtraRight) {
                getLayoutParams().mExtraRight = right;
            }
            
            if(bottom > getLayoutParams().mExtraBottom) {
                getLayoutParams().mExtraBottom = bottom;
            }
        }
        
        
    }

    @Override
    public View duplicate() {
        RelativeLayout view = new RelativeLayout();
        duplicateTo(view);
        return view;
    }
    
    @Override
    protected void duplicateTo(View view) {
        super.duplicateTo(view);
        RelativeLayout myView = (RelativeLayout) view;
        for (View childView : children) {
            myView.addViewInLayout(childView.duplicate());
        }
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
        
        if(!layoutParams.getLayoutPaddingTop().isRelative()) {
            measuredHeight +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());  
        }
        if(!layoutParams.getLayoutPaddingBottom().isRelative()) {
            measuredHeight +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());  
        }
        if(!layoutParams.getLayoutPaddingLeft().isRelative()) {
            measuredWidth +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());  
        }
        if(!layoutParams.getLayoutPaddingRight().isRelative()) {
            measuredWidth +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());  
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

    
    @Override
    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        boolean used = false;
        
        for (View view : children) {
            
            if(mouseEvent.getAction() != Action.MOUSE_DRAGGED) {
                if(mouseEvent.getX() < view.layoutParams.mLeft - view.layoutParams.mLeftPadding || mouseEvent.getX() > view.layoutParams.mRight + view.layoutParams.mRightPadding) {
                    continue;
                }
                
                if(mouseEvent.getY() < view.layoutParams.mTop - view.layoutParams.mTopPadding || mouseEvent.getY() > view.layoutParams.mBottom + view.layoutParams.mBottomPadding) {
                    continue;
                }
            }
            
            if(view.onMouseEvent(mouseEvent.relativeTo((int) view.layoutParams.mLeft,(int)  view.layoutParams.mTop))) {
                used = true;
                break;
            }
        }
        
        if(!used) {
            used = super.onMouseEvent(mouseEvent);
        }
        
        return used;
    }

    
    
    
}
