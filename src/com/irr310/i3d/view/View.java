package com.irr310.i3d.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dMesure;

public abstract class View {

    /**
     * The parent this view is attached to.
     * {@hide}
     *
     * @see #getParent()
     */
    protected ViewParent mParent;
    
    Graphics g;
    private String id;
    
    public View(Graphics g) {
        this.g = g;
    }
    
    public abstract void draw();
    
    public abstract boolean computeSize(); 


    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public abstract View duplicate();

    
    protected Point computeMesure(I3dMesure mesure) {
        Point point = new Point();
        if(mesure.isRelativeX()) {
            Layout parentLayout = getParent().getLayout();
            if(parentLayout.isWidthDefined()) {
                point.x = parentLayout.getWidth() * 100 / mesure.getValueX();
            } else {
                Log.error("Relative width in undefined width parent");
                point.x =  0;
            }
        } else {
            point.x = mesure.getValueX();
        }
        
        if(mesure.isRelativeY()) {
            Layout parentLayout = getParent().getLayout();
            if(parentLayout.isHeightDefined()) {
                point.y = parentLayout.getHeight() * 100 / mesure.getValueY();
            } else {
                Log.error("Relative height in undefined height parent");
                point.y =  0;
            }
        } else {
            point.y = mesure.getValueY();
        }
        
        return point;
    }
    
    
    void assignParent(ViewParent parent) {
        if (mParent == null) {
            mParent = parent;
        } else if (parent == null) {
            mParent = null;
        } else {
            throw new RuntimeException("view " + this + " being added, but"
                    + " it already has a parent");
        }
    }
    
    /**
     * Gets the parent of this view. Note that the parent is a
     * ViewParent and not necessarily a View.
     *
     * @return Parent of this view.
     */
    public final ViewParent getParent() {
        return mParent;
    }
}
