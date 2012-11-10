package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class LinearLayout extends ContainerView {

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
            
            view.layout(currentLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft()) ,
                        childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop()) ,
                        currentLeft + computedWidth - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight()),
                        childLayoutParams.mComputedBottom - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom()));
            
            currentLeft += computedWidth;
        }
    }

    private void layoutVertical() {
        
        int currentTop = 0;
        
        for (View view : children) {
            LayoutParams childLayoutParams = view.getLayoutParams();
            childLayoutParams.computeFrame(getLayoutParams());
            
            float computedHeight = childLayoutParams.mComputedBottom - childLayoutParams.mComputedTop;
            
            view.layout(childLayoutParams.mComputedLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft() ) ,
                        currentTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop()) + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop()),
                        childLayoutParams.mComputedRight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight()),
                        currentTop + computedHeight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom()) - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom()));
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
            myView.addChild(widget.duplicate());
        }
        myView.setLayoutOrientation(orientation);
    }
    
    @Override
    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        boolean used = false;
        
        for (View view : children) {
            
            if(mouseEvent.getX() < view.layoutParams.mLeft - view.layoutParams.mLeftPadding || mouseEvent.getX() > view.layoutParams.mRight + view.layoutParams.mRightPadding) {
                continue;
            }
            
            if(mouseEvent.getY() < view.layoutParams.mTop - view.layoutParams.mTopPadding || mouseEvent.getY() > view.layoutParams.mBottom + view.layoutParams.mBottomPadding) {
                continue;
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
