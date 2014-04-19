package com.irr310.i3d.view;

import com.irr310.common.tools.Log;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.input.I3dMouseEvent.Action;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;

public class RelativeLayout extends ViewGroup {

	public RelativeLayout() {
	}
	
	@Override
    public void onLayout(float l, float t, float r, float b) {
	    
	    /**
	     * The extras size is used when the real display position is requered, as for ScrollView scroll limits
	     */
	    
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(mLayoutParams);
            //view.layout(childLayoutParams.mComputedLeft, childLayoutParams.mComputedTop, childLayoutParams.mComputedRight, childLayoutParams.mComputedBottom);
//            float left = childLayoutParams.mComputedLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft());
//            float top = childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop());
//            float right = childLayoutParams.mComputedRight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight());
//            float bottom = childLayoutParams.mComputedBottom - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom());

            float left = childLayoutParams.mComputedLeft;
            float top = childLayoutParams.mComputedTop;
            float right = childLayoutParams.mComputedRight;
            float bottom = childLayoutParams.mComputedBottom;

            
            view.layout(left  ,
                        top,
                        right,
                        bottom);
            
            
            float extraLeft = childLayoutParams.mComputedLeft + mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft());
            float extraTop = childLayoutParams.mComputedTop + mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop());
            float extraRight = childLayoutParams.mComputedRight - mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight());
            float extraBottom = childLayoutParams.mComputedBottom - mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom());
          
            
            
            
            if(extraLeft < getLayoutParams().mExtraLeft) {
                getLayoutParams().mExtraLeft = extraLeft;
            }
            
            if(extraTop < getLayoutParams().mExtraTop) {
                getLayoutParams().mExtraTop= extraTop;
            }
            
            if(extraRight > getLayoutParams().mExtraRight) {
                getLayoutParams().mExtraRight = extraRight;
            }
            
            if(extraBottom > getLayoutParams().mExtraBottom) {
                getLayoutParams().mExtraBottom = extraBottom;
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
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
        float measuredWidth = 0;
        float measuredHeight= 0;
        
        if(!mLayoutParams.getLayoutMarginTop().isRelative()) {
            measuredHeight +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());  
        }
        if(!mLayoutParams.getLayoutMarginBottom().isRelative()) {
            measuredHeight +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginBottom());  
        }
        if(!mLayoutParams.getLayoutMarginLeft().isRelative()) {
            measuredWidth +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());  
        }
        if(!mLayoutParams.getLayoutMarginRight().isRelative()) {
            measuredWidth +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginRight());  
        }
        
        if(!mLayoutParams.getLayoutPaddingTop().isRelative()) {
            measuredHeight +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());  
        }
        if(!mLayoutParams.getLayoutPaddingBottom().isRelative()) {
            measuredHeight +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingBottom());  
        }
        if(!mLayoutParams.getLayoutPaddingLeft().isRelative()) {
            measuredWidth +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());  
        }
        if(!mLayoutParams.getLayoutPaddingRight().isRelative()) {
            measuredWidth +=   mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingRight());  
        }
        
        float minLeft = 0;
        float minTop = 0;
        float maxBottom = 0;
        float maxRight = 0;
        
        
        for (View view : children) {
            view.measure(widthMeasureSpec, heightMeasureSpec);
            
            if(view.getLayoutParams().mMeasuredContentWidth > measuredWidth) {
                measuredWidth = view.getLayoutParams().mMeasuredContentWidth;
            }
            
            if(view.getLayoutParams().mMeasuredContentHeight > measuredHeight) {
                measuredHeight = view.getLayoutParams().mMeasuredContentHeight;
            }
        }
        
        
        if(mLayoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || mLayoutParams.getMeasurePoint().getX().isRelative()) {
            mLayoutParams.mMeasuredContentWidth = measuredWidth;
        }
        if(mLayoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || mLayoutParams.getMeasurePoint().getY().isRelative()) {
            mLayoutParams.mMeasuredContentHeight = measuredHeight;
        }
    }

    
    @Override
    public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
        boolean used = false;
        
        if(mouseEvent.getAction() == Action.MOUSE_PRESSED) {
            Log.log("plop");
        }
        
        float localXOffset = mLayoutParams.mLeftMargin + mLayoutParams.mLeftPadding;
        float localYOffset = mLayoutParams.mTopMargin + mLayoutParams.mTopPadding;
        
        for (View view : children) {
            
            if(mouseEvent.getAction() != Action.MOUSE_DRAGGED) {
                if(mouseEvent.getX() < localXOffset + view.getLayoutParams().mLeft + view.getLayoutParams().mLeftMargin || mouseEvent.getX() > localXOffset + view.getLayoutParams().mRight - view.getLayoutParams().mRightMargin) {
                    continue;
                }
                
                if(mouseEvent.getY() < localYOffset + view.getLayoutParams().mTop + view.getLayoutParams().mTopMargin || mouseEvent.getY() > localYOffset + view.getLayoutParams().mBottom - view.getLayoutParams().mBottomMargin) {
                    continue;
                }
            }
            
            if(view.onMouseEvent(mouseEvent.relativeTo((int) (view.getLayoutParams().mLeft + localXOffset) ,(int) (view.getLayoutParams().mTop + localYOffset)))) {
                used = true;
                break;
            }
        }
        
        if(!used) {
            used = super.onMouseEvent(mouseEvent);
        }
        
        return used;
    }

    @Override
    public boolean onKeyEvent(V3DKeyEvent keyEvent) {
        boolean used = false;
        
        for (View view : children) {
            if(view.onKeyEvent(keyEvent)) {
                used = true;
                break;
            }
        }
        
        if(!used) {
            used = super.onKeyEvent(keyEvent);
        }
        
        return used;
    }
    
    @Override
    public boolean onControllerEvent(V3DControllerEvent controllerEvent) {
        boolean used = false;
        
        for (View view : children) {
            if(view.onControllerEvent(controllerEvent)) {
                used = true;
                break;
            }
        }
        
        if(!used) {
            used = super.onControllerEvent(controllerEvent);
        }
        
        return used;
    }
    
}
