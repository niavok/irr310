package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Style;
import com.irr310.i3d.view.LayoutParams.LayoutGravity;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public abstract class View {

    /**
     * The parent this view is attached to.
     * {@hide}
     *
     * @see #getParent()
     */
    protected ViewParent mParent;
    protected LayoutParams layoutParams;
    protected BorderParams borderParams;
    
    protected Graphics g;
    private String id;
    private OnClickListener onClickListener = null;
    private String help;
    
    public View(Graphics g) {
        this.g = g;
        layoutParams = new LayoutParams();
        borderParams = new BorderParams();
    }
    
    public final void draw() {
        GL11.glPushMatrix();
        //GL11.glTranslatef(layout.offset.x, layout.offset.y, 0);
        GL11.glTranslatef(layoutParams.mLeft, layoutParams.mTop, 0);
        
        onDraw();
        
        GL11.glPopMatrix();
    }
    
    public abstract void onDraw();
    
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
        
        // Set padding
        if(!layoutParams.getLayoutPaddingTop().isRelative()) {
            layoutParams.mContentHeight +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());  
        }
        if(!layoutParams.getLayoutPaddingBottom().isRelative()) {
            layoutParams.mContentHeight +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());  
        }
        if(!layoutParams.getLayoutPaddingLeft().isRelative()) {
            layoutParams.mContentWidth +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());  
        }
        if(!layoutParams.getLayoutPaddingRight().isRelative()) {
            layoutParams.mContentWidth +=   layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());  
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

    protected void setBorder(BorderParams border) {
        this.borderParams = border;
    }
    
	public View findViewById(String id) {
		if(id.equals(this.id)) {
			return this;
		}
		return null;
	}

    public BorderParams getBorderParams() {
        return borderParams;
    }
    
    protected void duplicateTo(View view) {
        view.setId(getId());
        view.setLayout(getLayoutParams().duplicate());
        view.setBorder(getBorderParams().duplicate());
    }
    
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    
    public boolean performClick() {
        if (onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }
    
    public static interface OnClickListener {
        void onClick(View view); 
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public abstract boolean onMouseEvent(V3DMouseEvent mouseEvent);
}
