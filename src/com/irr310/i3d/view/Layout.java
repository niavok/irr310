package com.irr310.i3d.view;

public class Layout {

    boolean widthDefined;
    boolean heightDefined;
    int width;
    int height;
    
    
    public Layout( int width, int height) {
        this.width = width;
        this.height = height;
        widthDefined = true;
        heightDefined = true;
        
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
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    
    
    
}
