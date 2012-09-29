package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.view.LayoutParams.LayoutAlign;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

public abstract class View {

    /**
     * The parent this view is attached to.
     * {@hide}
     *
     * @see #getParent()
     */
    protected ViewParent mParent;
    protected LayoutParams layoutParams;
    
    Graphics g;
    private String id;
    
    public View(Graphics g) {
        this.g = g;
        layoutParams = new LayoutParams();
    }
    
    public final void draw() {
        GL11.glPushMatrix();
        //GL11.glTranslatef(layout.offset.x, layout.offset.y, 0);
        GL11.glTranslatef(layoutParams.mLeft, layoutParams.mTop, 0);
        
        doDraw();
        
        GL11.glPopMatrix();
    }
    
    public abstract void doDraw();
    
    public void measure() {
        if(layoutParams.getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
            if(!layoutParams.getMeasurePoint().getX().isRelative()) {
                layoutParams.mContentWidth = layoutParams.computeMesure(layoutParams.getMeasurePoint().getX());
            }
        }
        
        if(layoutParams.getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
            if(!layoutParams.getMeasurePoint().getY().isRelative()) {
                layoutParams.mContentHeight = layoutParams.computeMesure(layoutParams.getMeasurePoint().getY());
            }
        }
        
        // Set margin
        if(!layoutParams.getLayoutMarginTop().isRelative()) {
            layoutParams.mContentHeight +=   layoutParams.computeMesure(layoutParams.getLayoutMarginTop());  
        }
        if(!layoutParams.getLayoutMarginBottom().isRelative()) {
            layoutParams.mContentHeight +=   layoutParams.computeMesure(layoutParams.getLayoutMarginBottom());  
        }
        if(!layoutParams.getLayoutMarginLeft().isRelative()) {
            layoutParams.mContentWidth +=   layoutParams.computeMesure(layoutParams.getLayoutMarginLeft());  
        }
        if(!layoutParams.getLayoutMarginRight().isRelative()) {
            layoutParams.mContentWidth +=   layoutParams.computeMesure(layoutParams.getLayoutMarginRight());  
        }
        onMeasure();
    }
    
    public void layout(float l, float t, float r, float b) {
        boolean changed = layoutParams.setFrame(l, t, r, b);
        //if (changed) {
            onLayout(l, t, r, b);
        //}
    }

    public abstract void onLayout(float l, float t, float r, float b);
    
    public abstract void onMeasure();
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public abstract View duplicate();

    public final LayoutParams getLayoutParams() {
        return layoutParams;
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
    
    protected void setLayout(LayoutParams layout) {
        this.layoutParams = layout;
    }

	public View findViewById(String id) {
		if(id.equals(this.id)) {
			return this;
		}
		return null;
	}
}
