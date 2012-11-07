package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Texture;
import com.irr310.i3d.fonts.CharacterPixmap;
import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.LinearLayout.LayoutOrientation;
import com.irr310.i3d.view.TextView.Gravity;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class ScrollView extends View implements ViewParent {

    float scrollOffsetX = 0;
    float scrollOffsetY = 0;
    View child = null;
    private boolean scrolling = false;

    float scrollingBaseX = 0;
    float scrollingBaseY = 0;
    float scrollingBaseOffsetX = 0;
    float scrollingBaseOffsetY = 0;

    public ScrollView(Graphics g) {
        super(g);
    }

    @Override
    public void onDraw() {

        GL11.glPushMatrix();
        GL11.glTranslatef(scrollOffsetX, scrollOffsetY, 0);

        child.draw();

        GL11.glPopMatrix();

    }

    @Override
    public View duplicate() {
        ScrollView view = new ScrollView(g);
        duplicateTo(view);
        return view;
    }

    @Override
    protected void duplicateTo(View view) {
        super.duplicateTo(view);
        ScrollView myView = (ScrollView) view;
        myView.addChild(child.duplicate());
    }

    public void addChild(View view) {
        this.child = view;
        view.assignParent(this);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        LayoutParams childLayoutParams = child.getLayoutParams();
        childLayoutParams.computeFrame(layoutParams);
        child.layout(childLayoutParams.mComputedLeft + layoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft())
                             + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft()),
                     childLayoutParams.mComputedTop + layoutParams.computeMesure(childLayoutParams.getLayoutMarginTop())
                             + layoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop()),
                     childLayoutParams.mComputedRight - layoutParams.computeMesure(childLayoutParams.getLayoutMarginRight())
                             - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight()),
                     childLayoutParams.mComputedBottom - layoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom())
                             - layoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom()));
    }

    @Override
    public void onMeasure() {
        float measuredWidth = 0;
        float measuredHeight = 0;

        child.measure();
        measuredHeight = child.getLayoutParams().mContentHeight;
        measuredWidth = child.getLayoutParams().mContentWidth;

        if (!layoutParams.getLayoutMarginTop().isRelative()) {
            measuredHeight += layoutParams.computeMesure(layoutParams.getLayoutMarginTop());
        }
        if (!layoutParams.getLayoutMarginBottom().isRelative()) {
            measuredHeight += layoutParams.computeMesure(layoutParams.getLayoutMarginBottom());
        }
        if (!layoutParams.getLayoutMarginLeft().isRelative()) {
            measuredWidth += layoutParams.computeMesure(layoutParams.getLayoutMarginLeft());
        }
        if (!layoutParams.getLayoutMarginRight().isRelative()) {
            measuredWidth += layoutParams.computeMesure(layoutParams.getLayoutMarginRight());
        }

        if (!layoutParams.getLayoutPaddingTop().isRelative()) {
            measuredHeight += layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());
        }
        if (!layoutParams.getLayoutPaddingBottom().isRelative()) {
            measuredHeight += layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());
        }
        if (!layoutParams.getLayoutPaddingLeft().isRelative()) {
            measuredWidth += layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());
        }
        if (!layoutParams.getLayoutPaddingRight().isRelative()) {
            measuredWidth += layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());
        }

        if (layoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getX().isRelative()) {
            layoutParams.mContentWidth = measuredWidth;
        }
        if (layoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getY().isRelative()) {
            layoutParams.mContentHeight = measuredHeight;
        }
    }

    @Override
    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        boolean used = false;

        if (scrolling) {

            if (mouseEvent.getAction() == Action.MOUSE_RELEASED) {
                scrolling = false;
            } else if (mouseEvent.getAction() == Action.MOUSE_DRAGGED) {
                scrollOffsetX = scrollingBaseOffsetX + (mouseEvent.getX() - scrollingBaseX);
                scrollOffsetY = scrollingBaseOffsetY + (mouseEvent.getY() - scrollingBaseY);
            }
            used = true;
        } else {

            if (child.onMouseEvent(mouseEvent.relativeTo((int) (child.layoutParams.mLeft + scrollOffsetX),
                                                         (int) (child.layoutParams.mTop + scrollOffsetY)))) {
                used = true;
            } else {
                if (mouseEvent.getAction() == Action.MOUSE_PRESSED) {
                    scrolling = true;
                    scrollingBaseOffsetX = scrollOffsetX;
                    scrollingBaseOffsetY = scrollOffsetY;
                    scrollingBaseX = mouseEvent.getX();
                    scrollingBaseY = mouseEvent.getY();
                    used = true;
                }
            }
        }

        return used;
    }

    @Override
    public void requestLayout() {
        // TODO Auto-generated method stub

    }

}