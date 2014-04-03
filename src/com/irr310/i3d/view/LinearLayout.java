package com.irr310.i3d.view;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.input.I3dMouseEvent.Action;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;

public class LinearLayout extends ViewGroup {

    private LayoutOrientation orientation = LayoutOrientation.HORIZONTAL;

    public enum LayoutOrientation {
        HORIZONTAL,
        VERTICAL,
    }
    
	public LinearLayout() {
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
                measuredWidth += view.getLayoutParams().mMeasuredContentWidth;
                if(view.getLayoutParams().mMeasuredContentHeight > measuredHeight) {
                    measuredHeight = view.getLayoutParams().mMeasuredContentHeight;
                }
            }
        } else {
            for (View view : children) {
                view.measure();
                measuredHeight += view.getLayoutParams().mMeasuredContentHeight;
                if(view.getLayoutParams().mMeasuredContentWidth > measuredWidth) {
                    measuredWidth = view.getLayoutParams().mMeasuredContentWidth;
                }
            }
        }
        
        if(!getLayoutParams().getLayoutMarginTop().isRelative()) {
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
        

        if(mLayoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || mLayoutParams.getMeasurePoint().getX().isRelative()) {
            mLayoutParams.mMeasuredContentWidth = measuredWidth;
        }
        if(mLayoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || mLayoutParams.getMeasurePoint().getY().isRelative()) {
            mLayoutParams.mMeasuredContentHeight = measuredHeight;
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
        
        //Simulation pass
        float fixedWidth = 0;
        float variableWidth = 0;
        mLayoutParams.setRelativeHorizontalRatio(1);
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            if(childLayoutParams.getLayoutWidthMeasure() == LayoutMeasure.FIXED && childLayoutParams.getMeasurePoint().getX().isRelative()) {
                variableWidth += childLayoutParams.mComputedRight - childLayoutParams.mComputedLeft;
            } else {
                fixedWidth += childLayoutParams.mComputedRight - childLayoutParams.mComputedLeft;
            }
           
        }
        
        float relativeRatio = (mLayoutParams.getContentWidth() - fixedWidth) / variableWidth;
        mLayoutParams.setRelativeHorizontalRatio(relativeRatio);
        
        int currentLeft = 0;

        
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            float computedWidth = childLayoutParams.mComputedRight - childLayoutParams.mComputedLeft;
//            
//            float l = currentLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft());
//            float t = childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop());
//            float r = currentLeft + computedWidth - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight());
//            float b = childLayoutParams.mComputedBottom - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom());
            
            float l = currentLeft;
            float t = childLayoutParams.mComputedTop;
            float r = currentLeft + computedWidth;
            float b = childLayoutParams.mComputedBottom;
            view.layout(l, t, r, b);
            
            currentLeft += computedWidth;
        }
    }

    private void layoutVertical() {
        
      //Simulation pass
        float fixedHeight = 0;
        float variableHeight = 0;
        mLayoutParams.setRelativeVerticalRatio(1);
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            if(childLayoutParams.getLayoutHeightMeasure() == LayoutMeasure.FIXED && childLayoutParams.getMeasurePoint().getY().isRelative()) {
                variableHeight += childLayoutParams.mComputedBottom - childLayoutParams.mComputedTop;
            } else {
                fixedHeight += childLayoutParams.mComputedBottom - childLayoutParams.mComputedTop;
            }
           
        }
        
        float relativeRatio = (mLayoutParams.getContentHeight() - fixedHeight) / variableHeight;
        mLayoutParams.setRelativeVerticalRatio(relativeRatio);
        
        
        int currentTop = 0;
        
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            float computedHeight = childLayoutParams.mComputedBottom - childLayoutParams.mComputedTop;
            
            float l = childLayoutParams.mComputedLeft;// + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft() );
            float t = currentTop;// + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop());
            float r = childLayoutParams.mComputedRight;// - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight());
            float b = currentTop + computedHeight; //- layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom());
            view.layout(l ,
                        t,
                        r,
                        b);
            currentTop += computedHeight;
        }
    }

    @Override
    public View duplicate() {
        LinearLayout view = new LinearLayout();
        duplicateTo(view);
        return view;
    }

    
    @Override
    protected void duplicateTo(View view) {
        super.duplicateTo(view);
        LinearLayout myView = (LinearLayout) view;
        for (View widget : children) {
            myView.addViewInLayout(widget.duplicate());
        }
        myView.setLayoutOrientation(orientation);
    }
    
    @Override
    public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
        boolean used = false;
        
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
