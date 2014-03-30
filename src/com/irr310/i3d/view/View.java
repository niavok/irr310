package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.Style;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public abstract class View {

    /**
     * The parent this view is attached to. {@hide}
     * 
     * @see #getParent()
     */
    protected ViewParent mParent;
    protected LayoutParams mLayoutParams;
    private BorderParams borderParams;

    private String id = "";
    private OnClickListener onClickListener = null;
    private String help;
    private OnMouseEventListener onMouseEventListener;
    private OnKeyEventListener onKeyEventListener;
    private OnControllerEventListener onControllerEventListener;
    private boolean visible = true;
    private StyleRenderer styleRenderer;
    private Style selectedStyle = new Style();
    private Style idleStyle = new Style();
    private ViewState state = ViewState.IDLE;

    public enum ViewState {
        IDLE,
        OVER,
        ACTIVE,
        SELECTED
    }
    
    public View() {
        mLayoutParams = new LayoutParams();
        borderParams = new BorderParams();
        styleRenderer = new StyleRenderer(this);
    }

    public void draw(Graphics g) {
        if (!visible) {
            return;
        }

        GL11.glPushMatrix();
        // GL11.glTranslatef(layout.offset.x, layout.offset.y, 0);
        g.setColor(Color.randomLightOpaqueColor());
//        g.drawFilledRectangle(mLayoutParams.mLeft, mLayoutParams.mTop, mLayoutParams.getTotalWidth(), mLayoutParams.getTotalHeight());
        
        // Margin
        float tranlationXIncludingMargin = mLayoutParams.mLeft+ mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        float tranlationYIncludingMargin = mLayoutParams.mTop + mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());
        GL11.glTranslatef(tranlationXIncludingMargin, tranlationYIncludingMargin, 0);
        g.pushUiTranslation(new Point(tranlationXIncludingMargin, tranlationYIncludingMargin));
        g.setColor(Color.randomDarkOpaqueColor());
//        g.drawFilledRectangle(0, 0, layoutParams.getBorderWidth(), layoutParams.getBorderHeight());
        styleRenderer.draw(g);
        
        
        
        //Padding
        float tranlationXPadding = mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
        float tranlationYPadding= mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());
        
        GL11.glTranslatef(tranlationXPadding, tranlationYPadding, 0);
        g.pushUiTranslation(new Point(tranlationXPadding, tranlationYPadding));
        
        
        g.setColor(Color.randomLightOpaqueColor());
//        g.drawFilledRectangle(0, 0, mLayoutParams.getContentWidth(), mLayoutParams.getContentHeight());
        onDraw(g);
        
        // Pop padding
        g.popUiTranslation();

        // Pop margin
        g.popUiTranslation();
        
        
        GL11.glPopMatrix();
    }

    public abstract void onDraw(Graphics g);

    public void measure() {
        if (mLayoutParams.getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
            if (!mLayoutParams.getMeasurePoint().getX().isRelative()) {
                mLayoutParams.mMeasuredContentWidth = mLayoutParams.computeMesure(mLayoutParams.getMeasurePoint().getX());
            }
        }

        if (mLayoutParams.getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
            if (!mLayoutParams.getMeasurePoint().getY().isRelative()) {
                mLayoutParams.mMeasuredContentHeight = mLayoutParams.computeMesure(mLayoutParams.getMeasurePoint().getY());
            }
        }

        // Set margin
        if (!mLayoutParams.getLayoutMarginTop().isRelative()) {
            mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());
        }
        if (!mLayoutParams.getLayoutMarginBottom().isRelative()) {
            mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginBottom());
        }
        if (!mLayoutParams.getLayoutMarginLeft().isRelative()) {
            mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        }
        if (!mLayoutParams.getLayoutMarginRight().isRelative()) {
            mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginRight());
        }

        // Set padding
        if (mLayoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED) {
            if (!mLayoutParams.getLayoutPaddingTop().isRelative()) {
                mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());
            }
            if (!mLayoutParams.getLayoutPaddingBottom().isRelative()) {
                mLayoutParams.mMeasuredContentHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingBottom());
            }
        }
        
        if (mLayoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED) {
            if (!mLayoutParams.getLayoutPaddingLeft().isRelative()) {
                mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
            }
            if (!mLayoutParams.getLayoutPaddingRight().isRelative()) {
                mLayoutParams.mMeasuredContentWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingRight());
            }
        }

        onMeasure();
    }

    public void layout(float l, float t, float r, float b) {
        boolean changed = mLayoutParams.setFrame(l, t, r, b);
        mLayoutParams.setExtrasFrame(l, t, r, b);
        // if (changed) {
        onLayout(l, t, r, b);
        // }
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

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }
    
    public void setState(ViewState state) {
        if(this.state != state) {
            this.state = state;
            switch (state) {
                case SELECTED:
                    if(selectedStyle != null) {
                        selectedStyle.apply(this);
                    } else {
                        idleStyle.apply(this);
                    }
                break;
                case IDLE:
                default:
                    idleStyle.apply(this);
                break;    
            }
        }
    }

    public ViewState getState() {
        return state;
    }
    
    void assignParent(ViewParent parent) {
        if (mParent == null) {
            mParent = parent;
        } else if (parent == null) {
            mParent = null;
        } else {
            throw new RuntimeException("view " + this + " being added, but" + " it already has a parent");
        }
    }

    /**
     * Gets the parent of this view. Note that the parent is a ViewParent and
     * not necessarily a View.
     * 
     * @return Parent of this view.
     */
    public ViewParent getParent() {
        return mParent;
    }

    protected void setLayout(LayoutParams layout) {
        mLayoutParams = layout;
    }

    protected void setBorder(BorderParams border) {
        this.borderParams = border;
    }

    public final View findViewById(String id) {
        View view = doFindViewById(id);
        if(view != null) {
            return view;
        }
        throw new RessourceLoadingException("Fail to find view '"+id+"' in '"+this.id+"'");
    }

    public View doFindViewById(String id) {
        if (id.equals(this.id)) {
            return this;
        }
        return null;
    }

    public BorderParams getBorderParams() {
        return borderParams;
    }

    protected void duplicateTo(View view) {
        view.setId(getId());
        view.setSelectedStyle(selectedStyle.duplicate());
        view.setIdleStyle(idleStyle.duplicate());
        view.setLayout(getLayoutParams().duplicate());
        view.setBorder(getBorderParams().duplicate());
        view.getIdleStyle().apply(view);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnMouseListener(OnMouseEventListener onMouseEventListener) {
        this.onMouseEventListener = onMouseEventListener;
    }
    
    public void setOnKeyListener(OnKeyEventListener onKeyEventListener) {
        this.onKeyEventListener = onKeyEventListener;
    }
    
    public void setOnControllerListener(OnControllerEventListener onControllerEventListener) {
        this.onControllerEventListener = onControllerEventListener;
    }

    public boolean performClick(V3DMouseEvent mouseEvent) {
        if (onClickListener != null) {
            onClickListener.onClick(mouseEvent, this);
            return true;
        }
        return false;
    }

    public static interface OnClickListener {
        void onClick(V3DMouseEvent mouseEvent, View view);
    }

    public static interface OnMouseEventListener {
        boolean onMouseEvent(V3DMouseEvent mouseEvent);
    }
    
    public static interface OnKeyEventListener {
        boolean onKeyEvent(V3DKeyEvent keyEvent);
    }
    
    public static interface OnControllerEventListener {
        boolean onControllerEvent(V3DControllerEvent controllerEvent);
    }

    public void setHelp(String help) {
        this.help = help;
    }

    // public abstract boolean onMouseEvent(V3DMouseEvent mouseEvent);

    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        if (!visible) {
            return false;
        }

        if (mouseEvent.getAction() == Action.MOUSE_CLICKED) {
            if (performClick(mouseEvent)) {
                return true;
            }
        }

        if (onMouseEventListener != null) {
            return onMouseEventListener.onMouseEvent(mouseEvent);
        }

        return false;
    }
    
    public boolean onKeyEvent(V3DKeyEvent keyEvent) {
        if (!visible) {
            return false;
        }

        if (onKeyEventListener != null) {
            return onKeyEventListener.onKeyEvent(keyEvent);
        }

        return false;
    }
    
    public boolean onControllerEvent(V3DControllerEvent controllerEvent) {
        if (!visible) {
            return false;
        }

        if (onControllerEventListener != null) {
            return onControllerEventListener.onControllerEvent(controllerEvent);
        }

        return false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSelectedStyle(Style style) {
        selectedStyle = style;
    }
    
    public void setIdleStyle(Style style) {
        idleStyle = style;
    }
    
    public Style getIdleStyle() {
        return idleStyle;
    }
    
    public Style getSelectedStyle() {
        return selectedStyle;
    }

}
