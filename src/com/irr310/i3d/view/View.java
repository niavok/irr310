package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.Layout.LayoutAlign;
import com.irr310.i3d.view.Layout.LayoutMeasure;

public abstract class View {

    /**
     * The parent this view is attached to.
     * {@hide}
     *
     * @see #getParent()
     */
    protected ViewParent mParent;
    protected Layout layout;
    
    Graphics g;
    private String id;
    
    public View(Graphics g) {
        this.g = g;
        layout = new Layout();
    }
    
    public final void draw() {
        GL11.glPushMatrix();
        GL11.glTranslatef(layout.offset.x, layout.offset.y, 0);
        
        doDraw();
        
        GL11.glPopMatrix();
    }
    
    public abstract void doDraw();
    
    public final boolean layout(Layout parentLayout) {
        
        if(layout.getLayoutWidthMeasure() == LayoutMeasure.MATCH_PARENT) {
            if(parentLayout.widthDefined) {
                layout.width = parentLayout.width;
                layout.widthDefined = true;
            } else {
                Log.error("'match parent' width an undefined parent width");
            }
        } else if (layout.getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
            layout.width =  parentLayout.computeMesure(layout.getMeasurePoint().getX());
            layout.widthDefined = true;
        } else {
            throw new RuntimeException("Not implemented");
        }
        
        if(layout.getLayoutHeightMeasure() == LayoutMeasure.MATCH_PARENT) {
            if(parentLayout.heightDefined) {
                layout.height = parentLayout.height;
                layout.heightDefined = true;
            } else {
                Log.error("'match parent' height an undefined parent height");
            }
        } else if (layout.getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
            layout.height =  parentLayout.computeMesure(layout.getMeasurePoint().getY());
            layout.heightDefined = true;
        } else {
            throw new RuntimeException("Not implemented");
        }
        
        if(layout.getLayoutAlignX() == LayoutAlign.CENTER) {
            if(parentLayout.widthDefined && layout.widthDefined) {
                layout.setOffsetX(parentLayout.width/2 - layout.width /2);
            } else {
                Log.error("'center' align with an undefined parent width or local width");
            }
        }

        if(layout.getLayoutAlignY() == LayoutAlign.CENTER) {
            if(parentLayout.heightDefined && layout.heightDefined) {
                layout.setOffsetY(parentLayout.height/2 - layout.height /2);
            } else {
                Log.error("'center' align with an undefined parent height or local height");
            }
        }
        
        return doLayout(parentLayout);
    }
    
    public abstract boolean doLayout(Layout parentLayout);

    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public abstract View duplicate();

    public final Layout getLayout() {
        return layout;
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
    
    protected void setLayout(Layout layout) {
        this.layout = layout;
    }

	public View findViewById(String id) {
		if(id.equals(this.id)) {
			return this;
		}
		return null;
	}
}
