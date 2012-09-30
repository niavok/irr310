package com.irr310.i3d.view;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Measure;
import com.irr310.i3d.MeasurePoint;

public class LayoutParams {

    // Output
    boolean layouted;
    
    //float width;
    //float height;
    //public Point offset;
    
    public float mLeft;
    public float mRight;
    public float mTop;
    public float mBottom;
    
    public float mComputedLeft;
    public float mComputedRight;
    public float mComputedTop;
    public float mComputedBottom;
    public float mContentWidth;
    public float mContentHeight;

    // Input
    private LayoutMeasure layoutWidthMeasure;
    private LayoutMeasure layoutHeightMeasure;
    private LayoutAlign layoutAlignX;
    private LayoutAlign layoutAlignY;
    private MeasurePoint measurePoint;

    private Measure layoutMarginLeftMeasure;

    private Measure layoutMarginRightMeasure;

    private Measure layoutMarginTopMeasure;

    private Measure layoutMarginBottomMeasure;

    public enum LayoutMeasure {
        MATCH_PARENT, WRAP_CONTENT, FIXED,
    }

    public enum LayoutAlign {
        CENTER, LEFT, TOP, BOTTOM, RIGHT
    }

    public LayoutParams() {

        measurePoint = new MeasurePoint();

        layoutWidthMeasure = LayoutMeasure.WRAP_CONTENT;
        layoutHeightMeasure = LayoutMeasure.WRAP_CONTENT;
        layoutAlignX = LayoutAlign.LEFT;
        layoutAlignY = LayoutAlign.TOP;

        // Output
//        this.width = -1;
//        this.height = -1;
//        widthDefined = false;
//        heightDefined = false;
//        offset = new Point();
        mLeft = 0;
        mTop = 0;
        mRight = 0;
        mBottom = 0;
        mComputedLeft = 0;
        mComputedTop = 0;
        mComputedRight = 0;
        mComputedBottom = 0;
        mContentHeight = 0;
        mContentWidth = 0;
        layoutMarginTopMeasure = new Measure(0, false);
        layoutMarginBottomMeasure = new Measure(0, false);
        layoutMarginRightMeasure = new Measure(0, false);
        layoutMarginLeftMeasure = new Measure(0, false);
    }

//
//    public void setWidthDefined(boolean widthDefined) {
//        this.widthDefined = widthDefined;
//    }
//
    public boolean isLayouted() {
        return layouted;
    }
//
//    public void setHeightDefined(boolean heightDefined) {
//        this.heightDefined = heightDefined;
//    }

    
    protected boolean setFrame(float left, float top, float right, float bottom) {
        boolean changed = false;

        if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
            changed = true;
            mLeft = left;
            mTop = top;
            mRight = right;
            mBottom = bottom;
        }
        layouted = true;
        return changed;
    }
    
    public float getWidth() {
        return mRight - mLeft;
    }

//    public void setWidth(int width) {
//        this.width = width;
//    }

    public float getHeight() {
        return mBottom - mTop;
    }

//    public void setHeight(float height) {
//        this.height = height;
//    }

    public Point getOffset() {
        return new Point(mLeft, mTop);
    }

//    public void setOffsetX(float x) {
//        offset.x = x;
//    }
//
//    public void setOffsetY(float y) {
//        offset.y = y;
//    }

    public LayoutMeasure getLayoutWidthMeasure() {
        return layoutWidthMeasure;
    }

    public void setLayoutWidthMeasure(LayoutMeasure layoutWidthMeasure) {
        this.layoutWidthMeasure = layoutWidthMeasure;
    }

    public LayoutMeasure getLayoutHeightMeasure() {
        return layoutHeightMeasure;
    }

    public void setLayoutHeightMeasure(LayoutMeasure layoutHeightMeasure) {
        this.layoutHeightMeasure = layoutHeightMeasure;
    }

    public LayoutAlign getLayoutAlignX() {
        return layoutAlignX;
    }

    public void setLayoutAlignX(LayoutAlign layoutAlignX) {
        this.layoutAlignX = layoutAlignX;
    }

    public LayoutAlign getLayoutAlignY() {
        return layoutAlignY;
    }

    public void setLayoutAlignY(LayoutAlign layoutAlignY) {
        this.layoutAlignY = layoutAlignY;
    }

    public void setWidthMeasure(Measure measure) {
        this.measurePoint.setX(measure);
    }

    public void setHeightMeasure(Measure measure) {
        this.measurePoint.setY(measure);
    }
    
    public void setMarginLeftMeasure(Measure measure) {
        this.layoutMarginLeftMeasure = measure;
    }
    
    public void setMarginRightMeasure(Measure measure) {
        this.layoutMarginRightMeasure = measure;
    }
    
    
    public void setMarginTopMeasure(Measure measure) {
        this.layoutMarginTopMeasure = measure;
    }
    
    public void setMarginBottomMeasure(Measure measure) {
        this.layoutMarginBottomMeasure = measure;
    }
    

    public LayoutParams duplicate() {
        LayoutParams layout = new LayoutParams();
        layout.layoutWidthMeasure = layoutWidthMeasure;
        layout.layoutHeightMeasure = layoutHeightMeasure;
        layout.layoutAlignX = layoutAlignX;
        layout.layoutAlignY = layoutAlignY;
        layout.measurePoint = measurePoint;
        layout.layoutMarginTopMeasure = layoutMarginTopMeasure;
        layout.layoutMarginBottomMeasure= layoutMarginBottomMeasure;
        layout.layoutMarginLeftMeasure = layoutMarginLeftMeasure;
        layout.layoutMarginRightMeasure = layoutMarginRightMeasure;

        return layout;
    }
    
    public MeasurePoint getMeasurePoint() {
        return measurePoint;
    }
    
    
    public float computeMesure(Measure mesure) {
    	float value = 0;
        if(mesure.isRelative()) {
            if(isLayouted()) {
                value = getWidth() * mesure.getValue() / 100;
            } else {
                Log.error("Relative width in undefined width parent");
                value =  0;
            }
        } else {
            value = mesure.getValue();
        }
        
        return value;
    }

    public void computeFrame(LayoutParams parentLayout) {
        
        float width = 0;
        float height = 0;
        
        if(getLayoutWidthMeasure() == LayoutMeasure.MATCH_PARENT) {
              width = parentLayout.mRight - parentLayout.mLeft;
          } else if (getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
              width = parentLayout.computeMesure(getMeasurePoint().getX());
              width += parentLayout.computeMesure(getLayoutMarginLeft());
              width += parentLayout.computeMesure(getLayoutMarginRight());
          } else if (getLayoutWidthMeasure() == LayoutMeasure.WRAP_CONTENT) {
              width = mContentWidth;
          } else {
              throw new RuntimeException("Not implemented");
          }
          
          if(getLayoutHeightMeasure() == LayoutMeasure.MATCH_PARENT) {
              height = parentLayout.mBottom - parentLayout.mTop;
          } else if (getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
              height = parentLayout.computeMesure(getMeasurePoint().getY());
              height += parentLayout.computeMesure(getLayoutMarginTop());
              height += parentLayout.computeMesure(getLayoutMarginBottom());
          } else if (getLayoutHeightMeasure() == LayoutMeasure.WRAP_CONTENT) {
              height = mContentHeight;
          } else {
              throw new RuntimeException("Not implemented");
          }
          
          if(getLayoutAlignX() == LayoutAlign.CENTER) {
              mComputedLeft = parentLayout.getWidth() /2 - width /2;
          } else if (getLayoutAlignX() == LayoutAlign.LEFT) {
              mComputedLeft = 0;
          } else if (getLayoutAlignX() == LayoutAlign.RIGHT) {
              mComputedLeft = parentLayout.getWidth() - width;
          }
    
          if(getLayoutAlignY() == LayoutAlign.CENTER) {
              mComputedTop = parentLayout.getHeight()/2 - height /2;
          }else if (getLayoutAlignY() == LayoutAlign.TOP) {
              mComputedTop = 0;
          } else if (getLayoutAlignY() == LayoutAlign.BOTTOM) {
              mComputedTop = parentLayout.getHeight() - height;
          }
          
          mComputedRight = mComputedLeft + width;
          mComputedBottom = mComputedTop + height;
    }

    
    public Measure getLayoutMarginBottom() {
        return layoutMarginBottomMeasure;
    }
    
    public Measure getLayoutMarginLeft() {
        return layoutMarginLeftMeasure;
    }
    
    public Measure getLayoutMarginRight() {
        return layoutMarginRightMeasure;
    }
    
    public Measure getLayoutMarginTop() {
        return layoutMarginTopMeasure;
    }
}
