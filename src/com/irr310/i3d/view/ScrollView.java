package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Texture;
import com.irr310.i3d.fonts.CharacterPixmap;
import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.LinearLayout.LayoutOrientation;
import com.irr310.i3d.view.ScrollView.ScrollAxis;
import com.irr310.i3d.view.TextView.Gravity;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class ScrollView extends View implements ViewParent {

    float scrollOffsetX = 0;
    float scrollOffsetY = 0;
    View child = null;
    private boolean scrolling = false;

    private ScrollAxis scrollAxis = ScrollAxis.BOTH;
    private ScrollLimits scrollLimits = ScrollLimits.SOFT;
    float scrollingBaseX = 0;
    float scrollingBaseY = 0;
    float scrollingBaseOffsetX = 0;
    float scrollingBaseOffsetY = 0;
    private float oldWidth = 0;
    private float oldHeight = 0;

    public enum ScrollAxis {
        VERTICAL, HORIZONTAL, BOTH
    }

    public enum ScrollLimits {
        /**
         * The content can be moved only if it is bigger the ScrollView. The
         * move is limited when the outer border of the content it the same
         * border of the view port
         */
        STRICT,

        /**
         * The content can be moved even if it is smaller than ScrollView. The
         * move is limited when the outer border of the content it the same
         * border of the view port
         */
        SOFT,

        /**
         * The content can be moved even if it is smaller than ScrollView. The
         * move is limited when the outer border of the content it the opposite
         * border of the view port
         */
        FREE,
    }

    public ScrollView() {
        super();
    }

    @Override
    public void onDraw(Graphics g) {

        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        
        Point translation = g.getUiTranslation();
        GL11.glScissor((int) (translation.x+layoutParams.mLeft), (int) (layoutParams.mTop + translation.y+ layoutParams.getHeight()), (int) layoutParams.getWidth(), (int) layoutParams.getHeight());
        
        g.pushUiTranslation(new Point(scrollOffsetX, scrollOffsetY));
        GL11.glTranslatef(scrollOffsetX, scrollOffsetY, 0);

        child.draw(g);

        g.popUiTranslation();
        GL11.glPopAttrib();
        GL11.glPopMatrix();

    }

    @Override
    public View duplicate() {
        ScrollView view = new ScrollView();
        duplicateTo(view);
        return view;
    }

    @Override
    protected void duplicateTo(View view) {
        super.duplicateTo(view);
        ScrollView myView = (ScrollView) view;
        myView.setScrollAxis(scrollAxis);
        myView.setScrollLimits(scrollLimits);
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

        // float oldCenterX = oldWidth / 2 - scrollOffsetX;
        // float oldCenterY = oldHeight / 2 - scrollOffsetY;
        //
        // setScrollCenter(new Point(oldCenterX, oldCenterY));

        oldWidth = layoutParams.getWidth();
        oldHeight = layoutParams.getHeight();
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

        if (mouseEvent.getAction() == Action.MOUSE_PRESSED) {

            if (child.onMouseEvent(mouseEvent.relativeTo((int) (child.layoutParams.mLeft + scrollOffsetX),
                                                         (int) (child.layoutParams.mTop + scrollOffsetY)))) {
                used = true;
            } else {
                scrolling = true;
                scrollingBaseOffsetX = scrollOffsetX;
                scrollingBaseOffsetY = scrollOffsetY;
                scrollingBaseX = mouseEvent.getX();
                scrollingBaseY = mouseEvent.getY();
                used = true;
            }
        } else if (mouseEvent.getAction() == Action.MOUSE_RELEASED) {
            if (scrolling) {
                scrolling = false;
                used = true;
            }
        } else if (mouseEvent.getAction() == Action.MOUSE_DRAGGED) {
            if (scrolling) {
                if (scrollAxis == ScrollAxis.HORIZONTAL || scrollAxis == ScrollAxis.BOTH) {
                    boolean hitLimit = false;
                    float nextScrollOffsetX = scrollingBaseOffsetX + (mouseEvent.getX() - scrollingBaseX);

                    switch (scrollLimits) {

                        case STRICT:
                            if (child.getLayoutParams().mExtraRight - child.getLayoutParams().mExtraLeft < getLayoutParams().getWidth()) {
                                hitLimit = true;
                                scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            } else if (nextScrollOffsetX >= -child.getLayoutParams().mExtraLeft) {
                                hitLimit = true;
                                scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            } else if (nextScrollOffsetX < getLayoutParams().getWidth() - child.getLayoutParams().mExtraRight) {
                                hitLimit = true;
                                scrollOffsetX = getLayoutParams().getWidth() - child.getLayoutParams().getWidth();
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            }
                            break;
                        case SOFT:
                            if (child.getLayoutParams().mExtraRight - child.getLayoutParams().mExtraLeft < getLayoutParams().getWidth()) {
                                if (nextScrollOffsetX <= -child.getLayoutParams().mExtraLeft) {
                                    hitLimit = true;
                                    scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                } else if (nextScrollOffsetX > getLayoutParams().getWidth() - child.getLayoutParams().mExtraRight) {
                                    hitLimit = true;
                                    scrollOffsetX = getLayoutParams().getWidth() - child.getLayoutParams().mExtraRight;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                }
                            } else {
                                if (nextScrollOffsetX >= -child.getLayoutParams().mExtraLeft) {
                                    hitLimit = true;
                                    scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                } else if (nextScrollOffsetX < getLayoutParams().getWidth() - child.getLayoutParams().mExtraRight) {
                                    hitLimit = true;
                                    scrollOffsetX = getLayoutParams().getWidth() - child.getLayoutParams().mExtraRight;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                }
                            }
                            break;
                        case FREE:
                            if (nextScrollOffsetX + child.getLayoutParams().mExtraRight < 0) {
                                hitLimit = true;
                                scrollOffsetX = -child.getLayoutParams().mExtraRight;
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            } else if (nextScrollOffsetX > getLayoutParams().getWidth() - child.getLayoutParams().mExtraLeft) {
                                hitLimit = true;
                                scrollOffsetX = getLayoutParams().getWidth() - child.getLayoutParams().mExtraLeft;
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            }
                            break;
                        default:
                    }

                    if (!hitLimit) {
                        scrollOffsetX = nextScrollOffsetX;
                    }

                }
                if (scrollAxis == ScrollAxis.VERTICAL || scrollAxis == ScrollAxis.BOTH) {
                    boolean hitLimit = false;
                    float nextScrollOffsetY = scrollingBaseOffsetY + (mouseEvent.getY() - scrollingBaseY);

                    switch (scrollLimits) {

                        case STRICT:
                            if (child.getLayoutParams().mExtraBottom - child.getLayoutParams().mExtraTop < getLayoutParams().getHeight()) {
                                hitLimit = true;
                                scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            } else if (nextScrollOffsetY >= -child.getLayoutParams().mExtraTop) {
                                hitLimit = true;
                                scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            } else if (nextScrollOffsetY < getLayoutParams().getHeight() - child.getLayoutParams().mExtraBottom) {
                                hitLimit = true;
                                scrollOffsetY = getLayoutParams().getHeight() - child.getLayoutParams().getHeight();
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            }
                            break;
                        case SOFT:
                            if (child.getLayoutParams().mExtraBottom - child.getLayoutParams().mExtraTop < getLayoutParams().getHeight()) {
                                if (nextScrollOffsetY <= -child.getLayoutParams().mExtraTop) {
                                    hitLimit = true;
                                    scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                } else if (nextScrollOffsetY > getLayoutParams().getHeight() - child.getLayoutParams().mExtraBottom) {
                                    hitLimit = true;
                                    scrollOffsetY = getLayoutParams().getHeight() - child.getLayoutParams().mExtraBottom;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                }
                            } else {
                                if (nextScrollOffsetY >= -child.getLayoutParams().mExtraTop) {
                                    hitLimit = true;
                                    scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                } else if (nextScrollOffsetY < getLayoutParams().getHeight() - child.getLayoutParams().mExtraBottom) {
                                    hitLimit = true;
                                    scrollOffsetY = getLayoutParams().getHeight() - child.getLayoutParams().mExtraBottom;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                }
                            }
                            break;
                        case FREE:
                            if (nextScrollOffsetY + child.getLayoutParams().mExtraBottom < 0) {
                                hitLimit = true;
                                scrollOffsetY = -child.getLayoutParams().mExtraBottom;
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            } else if (nextScrollOffsetY > getLayoutParams().getHeight() - child.getLayoutParams().mExtraTop) {
                                hitLimit = true;
                                scrollOffsetY = getLayoutParams().getHeight() - child.getLayoutParams().mExtraTop;
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            }
                            break;
                        default:
                    }

                    if (!hitLimit) {
                        scrollOffsetY = nextScrollOffsetY;
                    }

                }
                used = true;
            }
        }

        if (!used) {
            if (child.onMouseEvent(mouseEvent.relativeTo((int) (child.layoutParams.mLeft + scrollOffsetX),
                                                         (int) (child.layoutParams.mTop + scrollOffsetY)))) {
                used = true;
            } else {
                used = super.onMouseEvent(mouseEvent);
            }
        }

        return used;
    }

    @Override
    public void requestLayout() {
        getParent().requestLayout();
    }

    @Override
    public View findViewById(String id) {
        View outputView = null;
        outputView = super.findViewById(id);
        if (outputView == null) {
            outputView = child.findViewById(id);
        }
        return outputView;
    }

    public Point getScrollCenter() {
        return new Point(layoutParams.getWidth() / 2 - scrollOffsetX, layoutParams.getHeight() / 2 - scrollOffsetY);
    }

    public void setScrollCenter(Point point) {
        scrollOffsetX = layoutParams.getWidth() / 2 - point.x;
        scrollOffsetY = layoutParams.getHeight() / 2 - point.y;
    }

    public Point getScrollOffset() {
        return new Point(scrollOffsetX, scrollOffsetY);
    }

    public void setScrollOffset(Point point) {
        scrollOffsetX = point.x;
        scrollOffsetY = point.y;
    }

    public void setScrollAxis(ScrollAxis scrollAxis) {
        this.scrollAxis = scrollAxis;
    }

    public void setScrollLimits(ScrollLimits scrollLimits) {
        this.scrollLimits = scrollLimits;
    }

}
