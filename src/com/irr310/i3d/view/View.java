package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.BorderParams.CornerStyle;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public abstract class View {

    /**
     * The parent this view is attached to. {@hide}
     * 
     * @see #getParent()
     */
    protected ViewParent mParent;
    protected LayoutParams layoutParams;
    protected BorderParams borderParams;

    private String id;
    private OnClickListener onClickListener = null;
    private String help;
    private OnMouseEventListener onMouseEventListener;
    private boolean visible = true;

    public View() {
        layoutParams = new LayoutParams();
        borderParams = new BorderParams();
    }

    public final void draw(Graphics g) {
        if (!visible) {
            return;
        }

        GL11.glPushMatrix();
        // GL11.glTranslatef(layout.offset.x, layout.offset.y, 0);
        GL11.glTranslatef(layoutParams.mLeft, layoutParams.mTop, 0);

        drawDecorations(g);
        onDraw(g);

        GL11.glPopMatrix();
    }

    private void drawDecorations(Graphics g) {
        GL11.glColor3f(1, 0, 0);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        float height = layoutParams.getHeight() + layoutParams.computeMesure(layoutParams.getLayoutPaddingTop())
                + layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());
        float width = layoutParams.getWidth() + layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft())
                + layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());
        float left = -layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());
        float top = -layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());
        float right = left + width;
        float bottom = top + height;

        GL11.glVertex2d(left, top);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(right, top + height);
        GL11.glVertex2d(left, top + height);
        GL11.glEnd();

        if (borderParams.getBackground() != null) {

            Drawable background = borderParams.getBackground();
            background.setGraphics(g);
            background.setBounds(left, top, right, bottom);
            float cornerLeftTopSize = layoutParams.computeMesure(borderParams.getCornerLeftTopSize());
            float cornerLeftBottomSize = layoutParams.computeMesure(borderParams.getCornerLeftBottomSize());
            float cornerRightTopSize = layoutParams.computeMesure(borderParams.getCornerRightTopSize());
            float cornerRightBottomSize = layoutParams.computeMesure(borderParams.getCornerRightBottomSize());

            // Top left
            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerLeftTopStyle() == CornerStyle.SQUARE) {
                background.vertex(left, top);
                background.vertex(left + cornerLeftTopSize, top);
                background.vertex(left + cornerLeftTopSize, top + cornerLeftTopSize);
                background.vertex(left, top + cornerLeftTopSize);
            }

            background.vertex(left + cornerLeftTopSize, top);
            background.vertex(left + width / 2, top);
            background.vertex(left + width / 2, top + height / 2);
            background.vertex(left + cornerLeftTopSize, top + height / 2);

            background.vertex(left, top + cornerLeftTopSize);
            background.vertex(left + cornerLeftTopSize, top + cornerLeftTopSize);
            background.vertex(left + cornerLeftTopSize, top + height / 2);
            background.vertex(left, top + height / 2);

            background.end();

            if (borderParams.getCornerLeftTopStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(left + cornerLeftTopSize, top);
                background.vertex(left + cornerLeftTopSize, top + cornerLeftTopSize);
                background.vertex(left, top + cornerLeftTopSize);
                background.end();
            }

            // Top right

            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerRightTopStyle() == CornerStyle.SQUARE) {
                background.vertex(right, top);
                background.vertex(right - cornerRightTopSize, top);
                background.vertex(right - cornerRightTopSize, top + cornerRightTopSize);
                background.vertex(right, top + cornerRightTopSize);
            }

            background.vertex(right - cornerRightTopSize, top);
            background.vertex(right - width / 2, top);
            background.vertex(right - width / 2, top + height / 2);
            background.vertex(right - cornerRightTopSize, top + height / 2);

            background.vertex(right, top + cornerLeftTopSize);
            background.vertex(right - cornerRightTopSize, top + cornerRightTopSize);
            background.vertex(right - cornerRightTopSize, top + height / 2);
            background.vertex(right, top + height / 2);

            background.end();

            if (borderParams.getCornerRightTopStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(right - cornerRightTopSize, top);
                background.vertex(right - cornerRightTopSize, top + cornerRightTopSize);
                background.vertex(right, top + cornerRightTopSize);
                background.end();
            }

            // Bottom left
            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerLeftBottomStyle() == CornerStyle.SQUARE) {
                background.vertex(left, bottom);
                background.vertex(left + cornerLeftBottomSize, bottom);
                background.vertex(left + cornerLeftBottomSize, bottom - cornerLeftBottomSize);
                background.vertex(left, bottom - cornerLeftBottomSize);
            }

            background.vertex(left + cornerLeftBottomSize, bottom);
            background.vertex(left + width / 2, bottom);
            background.vertex(left + width / 2, bottom - height / 2);
            background.vertex(left + cornerLeftBottomSize, bottom - height / 2);

            background.vertex(left, bottom - cornerLeftBottomSize);
            background.vertex(left + cornerLeftBottomSize, bottom - cornerLeftBottomSize);
            background.vertex(left + cornerLeftBottomSize, bottom - height / 2);
            background.vertex(left, bottom - height / 2);

            background.end();

            if (borderParams.getCornerLeftBottomStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(left + cornerLeftBottomSize, bottom);
                background.vertex(left + cornerLeftBottomSize, bottom - cornerLeftBottomSize);
                background.vertex(left, bottom - cornerLeftBottomSize);
                background.end();
            }

            // Bottom right
            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerRightBottomStyle() == CornerStyle.SQUARE) {
                background.vertex(right, bottom);
                background.vertex(right - cornerRightBottomSize, bottom);
                background.vertex(right - cornerRightBottomSize, bottom - cornerRightBottomSize);
                background.vertex(right, bottom - cornerLeftBottomSize);
            }

            background.vertex(right - cornerRightBottomSize, bottom);
            background.vertex(right - width / 2, bottom);
            background.vertex(right - width / 2, bottom - height / 2);
            background.vertex(right - cornerRightBottomSize, bottom - height / 2);

            background.vertex(right, bottom - cornerRightBottomSize);
            background.vertex(right - cornerRightBottomSize, bottom - cornerRightBottomSize);
            background.vertex(right - cornerRightBottomSize, bottom - height / 2);
            background.vertex(right, bottom - height / 2);

            background.end();

            if (borderParams.getCornerRightBottomStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(right - cornerRightBottomSize, bottom);
                background.vertex(right - cornerRightBottomSize, bottom - cornerRightBottomSize);
                background.vertex(right, bottom - cornerRightBottomSize);
                background.end();
            }

            
            background.close();
        }

    }

    public abstract void onDraw(Graphics g);

    public void measure() {
        if (layoutParams.getLayoutWidthMeasure() == LayoutMeasure.FIXED) {
            if (!layoutParams.getMeasurePoint().getX().isRelative()) {
                layoutParams.mContentWidth = layoutParams.computeMesure(layoutParams.getMeasurePoint().getX());
            }
        }

        if (layoutParams.getLayoutHeightMeasure() == LayoutMeasure.FIXED) {
            if (!layoutParams.getMeasurePoint().getY().isRelative()) {
                layoutParams.mContentHeight = layoutParams.computeMesure(layoutParams.getMeasurePoint().getY());
            }
        }

        // Set margin
        if (!layoutParams.getLayoutMarginTop().isRelative()) {
            layoutParams.mContentHeight += layoutParams.computeMesure(layoutParams.getLayoutMarginTop());
        }
        if (!layoutParams.getLayoutMarginBottom().isRelative()) {
            layoutParams.mContentHeight += layoutParams.computeMesure(layoutParams.getLayoutMarginBottom());
        }
        if (!layoutParams.getLayoutMarginLeft().isRelative()) {
            layoutParams.mContentWidth += layoutParams.computeMesure(layoutParams.getLayoutMarginLeft());
        }
        if (!layoutParams.getLayoutMarginRight().isRelative()) {
            layoutParams.mContentWidth += layoutParams.computeMesure(layoutParams.getLayoutMarginRight());
        }

        // Set padding
        if (!layoutParams.getLayoutPaddingTop().isRelative()) {
            layoutParams.mContentHeight += layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());
        }
        if (!layoutParams.getLayoutPaddingBottom().isRelative()) {
            layoutParams.mContentHeight += layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());
        }
        if (!layoutParams.getLayoutPaddingLeft().isRelative()) {
            layoutParams.mContentWidth += layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());
        }
        if (!layoutParams.getLayoutPaddingRight().isRelative()) {
            layoutParams.mContentWidth += layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());
        }

        onMeasure();
    }

    public void layout(float l, float t, float r, float b) {
        boolean changed = layoutParams.setFrame(l, t, r, b);
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

    public final LayoutParams getLayoutParams() {
        return layoutParams;
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
        view.setLayout(getLayoutParams().duplicate());
        view.setBorder(getBorderParams().duplicate());
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnMouseListener(OnMouseEventListener onMouseEventListener) {
        this.onMouseEventListener = onMouseEventListener;
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

    public static interface OnMouseEventListener {
        boolean onMouseEvent(V3DMouseEvent mouseEvent);
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
            if (performClick()) {
                return true;
            }
        }

        if (onMouseEventListener != null) {
            return onMouseEventListener.onMouseEvent(mouseEvent);
        }

        return false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
