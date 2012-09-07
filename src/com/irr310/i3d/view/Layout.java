package com.irr310.i3d.view;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Measure;
import com.irr310.i3d.MeasurePoint;

public class Layout {

    // Output
    boolean widthDefined;
    boolean heightDefined;
    float width;
    float height;
    public Point offset;

    // Input
    private LayoutMeasure layoutWidthMeasure;
    private LayoutMeasure layoutHeightMeasure;
    private LayoutAlign layoutAlignX;
    private LayoutAlign layoutAlignY;
    private MeasurePoint measurePoint;

    public enum LayoutMeasure {
        MATCH_PARENT, WRAP_CONTENT, FIXED,
    }

    public enum LayoutAlign {
        CENTER, LEFT, TOP,
    }

    public Layout() {

        measurePoint = new MeasurePoint();

        layoutWidthMeasure = LayoutMeasure.MATCH_PARENT;
        layoutHeightMeasure = LayoutMeasure.MATCH_PARENT;
        layoutAlignX = LayoutAlign.LEFT;
        layoutAlignY = LayoutAlign.TOP;

        // Output
        this.width = -1;
        this.height = -1;
        widthDefined = false;
        heightDefined = false;
        offset = new Point();
    }

    public boolean isWidthDefined() {
        return widthDefined;
    }

    public void setWidthDefined(boolean widthDefined) {
        this.widthDefined = widthDefined;
    }

    public boolean isHeightDefined() {
        return heightDefined;
    }

    public void setHeightDefined(boolean heightDefined) {
        this.heightDefined = heightDefined;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Point getOffset() {
        return offset;
    }

    public void setOffsetX(float x) {
        offset.x = x;
    }

    public void setOffsetY(float y) {
        offset.y = y;
    }

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

    public Layout duplicate() {
        Layout layout = new Layout();
        layout.layoutWidthMeasure = layoutWidthMeasure;
        layout.layoutHeightMeasure = layoutHeightMeasure;
        layout.layoutAlignX = layoutAlignX;
        layout.layoutAlignY = layoutAlignY;
        layout.measurePoint = measurePoint;

        return layout;
    }
    
    public MeasurePoint getMeasurePoint() {
        return measurePoint;
    }
    
    
    public float computeMesure(Measure mesure) {
    	float value = 0;
        if(mesure.isRelative()) {
            if(isWidthDefined()) {
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

}
