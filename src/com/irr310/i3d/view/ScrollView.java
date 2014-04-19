package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.input.I3dMouseEvent.Action;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

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
        
        GL11.glScissor((int)( translation.x), (int) (- mLayoutParams.getContentHeight() - translation.y), (int) mLayoutParams.getContentWidth(), Math.max((int) mLayoutParams.getContentHeight(),0));
//        GL11.glScissor((int)( translation.x), 700, (int) mLayoutParams.getTotalWidth(), (int) mLayoutParams.getTotalHeight());
        
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
        if(child != null) {
            myView.addViewInLayout(child.duplicate());
        }
    }

    public void addViewInLayout(View view) {
        this.child = view;
        view.assignParent(this);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {

        LayoutParams childLayoutParams = child.getLayoutParams();
        childLayoutParams.computeFrame(mLayoutParams);
        child.layout(childLayoutParams.mComputedLeft + mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginLeft())
                             + mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingLeft()),
                     childLayoutParams.mComputedTop + mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginTop())
                             + mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingTop()),
                     childLayoutParams.mComputedRight - mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginRight())
                             - mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingRight()),
                     childLayoutParams.mComputedBottom - mLayoutParams.computeMesure(childLayoutParams.getLayoutMarginBottom())
                             - mLayoutParams.computeMesure(childLayoutParams.getLayoutPaddingBottom()));

        // float oldCenterX = oldWidth / 2 - scrollOffsetX;
        // float oldCenterY = oldHeight / 2 - scrollOffsetY;
        //
        // setScrollCenter(new Point(oldCenterX, oldCenterY));

        oldWidth = mLayoutParams.getContentWidth();
        oldHeight = mLayoutParams.getContentHeight();
    }

    @Override
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
        float measuredWidth = 0;
        float measuredHeight = 0;

        if (!mLayoutParams.getLayoutMarginLeft().isRelative()) {
            widthMeasureSpec -= mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        }
        if (!mLayoutParams.getLayoutMarginRight().isRelative()) {
            widthMeasureSpec -= mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginRight());
        }
        if (!mLayoutParams.getLayoutPaddingLeft().isRelative()) {
            widthMeasureSpec -= mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
        }
        if (!mLayoutParams.getLayoutPaddingRight().isRelative()) {
            widthMeasureSpec -= mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingRight());
        }
        
        child.measure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = child.getLayoutParams().mMeasuredContentHeight;
        measuredWidth = child.getLayoutParams().mMeasuredContentWidth;

        if (!mLayoutParams.getLayoutMarginTop().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginTop());
        }
        if (!mLayoutParams.getLayoutMarginBottom().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginBottom());
        }
        if (!mLayoutParams.getLayoutMarginLeft().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginLeft());
        }
        if (!mLayoutParams.getLayoutMarginRight().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutMarginRight());
        }

        if (!mLayoutParams.getLayoutPaddingTop().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingTop());
        }
        if (!mLayoutParams.getLayoutPaddingBottom().isRelative()) {
            measuredHeight += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingBottom());
        }
        if (!mLayoutParams.getLayoutPaddingLeft().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingLeft());
        }
        if (!mLayoutParams.getLayoutPaddingRight().isRelative()) {
            measuredWidth += mLayoutParams.computeMesure(mLayoutParams.getLayoutPaddingRight());
        }
    }

    @Override
    public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
        boolean used = false;
        
        if (mouseEvent.getAction() == Action.MOUSE_PRESSED) {

            if (child.onMouseEvent(mouseEvent.relativeTo((int) (child.getLayoutParams().mLeft + scrollOffsetX),
                                                         (int) (child.getLayoutParams().mTop + scrollOffsetY)))) {
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
                            if (child.getLayoutParams().mExtraRight - child.getLayoutParams().mExtraLeft < getLayoutParams().getContentWidth()) {
                                hitLimit = true;
                                scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            } else if (nextScrollOffsetX >= -child.getLayoutParams().mExtraLeft) {
                                hitLimit = true;
                                scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            } else if (nextScrollOffsetX < getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraRight) {
                                hitLimit = true;
                                scrollOffsetX = getLayoutParams().getContentWidth() - child.getLayoutParams().getContentWidth();
                                scrollingBaseOffsetX = scrollOffsetX;
                                scrollingBaseX = mouseEvent.getX();
                            }
                            break;
                        case SOFT:
                            if (child.getLayoutParams().mExtraRight - child.getLayoutParams().mExtraLeft < getLayoutParams().getContentWidth()) {
                                if (nextScrollOffsetX <= -child.getLayoutParams().mExtraLeft) {
                                    hitLimit = true;
                                    scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                } else if (nextScrollOffsetX > getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraRight) {
                                    hitLimit = true;
                                    scrollOffsetX = getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraRight;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                }
                            } else {
                                if (nextScrollOffsetX >= -child.getLayoutParams().mExtraLeft) {
                                    hitLimit = true;
                                    scrollOffsetX = -child.getLayoutParams().mExtraLeft;
                                    scrollingBaseOffsetX = scrollOffsetX;
                                    scrollingBaseX = mouseEvent.getX();
                                } else if (nextScrollOffsetX < getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraRight) {
                                    hitLimit = true;
                                    scrollOffsetX = getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraRight;
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
                            } else if (nextScrollOffsetX > getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraLeft) {
                                hitLimit = true;
                                scrollOffsetX = getLayoutParams().getContentWidth() - child.getLayoutParams().mExtraLeft;
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
                            if (child.getLayoutParams().mExtraBottom - child.getLayoutParams().mExtraTop < getLayoutParams().getContentHeight()) {
                                hitLimit = true;
                                scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            } else if (nextScrollOffsetY >= -child.getLayoutParams().mExtraTop) {
                                hitLimit = true;
                                scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            } else if (nextScrollOffsetY < getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraBottom) {
                                hitLimit = true;
                                scrollOffsetY = getLayoutParams().getContentHeight() - child.getLayoutParams().getContentHeight();
                                scrollingBaseOffsetY = scrollOffsetY;
                                scrollingBaseY = mouseEvent.getY();
                            }
                            break;
                        case SOFT:
                            if (child.getLayoutParams().mExtraBottom - child.getLayoutParams().mExtraTop < getLayoutParams().getContentHeight()) {
                                if (nextScrollOffsetY <= -child.getLayoutParams().mExtraTop) {
                                    hitLimit = true;
                                    scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                } else if (nextScrollOffsetY > getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraBottom) {
                                    hitLimit = true;
                                    scrollOffsetY = getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraBottom;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                }
                            } else {
                                if (nextScrollOffsetY >= -child.getLayoutParams().mExtraTop) {
                                    hitLimit = true;
                                    scrollOffsetY = -child.getLayoutParams().mExtraTop;
                                    scrollingBaseOffsetY = scrollOffsetY;
                                    scrollingBaseY = mouseEvent.getY();
                                } else if (nextScrollOffsetY < getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraBottom) {
                                    hitLimit = true;
                                    scrollOffsetY = getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraBottom;
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
                            } else if (nextScrollOffsetY > getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraTop) {
                                hitLimit = true;
                                scrollOffsetY = getLayoutParams().getContentHeight() - child.getLayoutParams().mExtraTop;
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
            
            if(mouseEvent.getAction() == Action.MOUSE_PRESSED) {
                Log.log("plop");
            }
            
            if (child.onMouseEvent(mouseEvent.relativeTo((int) (child.getLayoutParams().mLeft + scrollOffsetX),
                                                         (int) (child.getLayoutParams().mTop + scrollOffsetY)))) {
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
    public View doFindViewById(String id) {
        View outputView = null;
        outputView = super.doFindViewById(id);
        if (outputView == null && child != null) {
            outputView = child.doFindViewById(id);
        }
        return outputView;
    }

    public Point getScrollCenter() {
        return new Point(mLayoutParams.getContentWidth() / 2 - scrollOffsetX, mLayoutParams.getContentHeight() / 2 - scrollOffsetY);
    }

    public void setScrollCenter(Point point) {
        scrollOffsetX = mLayoutParams.getContentWidth() / 2 - point.x;
        scrollOffsetY = mLayoutParams.getContentHeight() / 2 - point.y;
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
