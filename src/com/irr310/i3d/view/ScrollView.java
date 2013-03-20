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
import com.irr310.i3d.view.TextView.Gravity;
import com.irr310.server.Time;

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
    // contains old width and height of user screen view
    // use when resizing windows
    private float oldWidth = 0;
    private float oldHeight = 0;
    
    Point minscrollZone = new Point(0,0);
    Point maxscrollZone = new Point(0,0);
    
    public ScrollView() {
        super();
    }

    @Override
    public void onDraw(Graphics g) {

        GL11.glPushMatrix();
        GL11.glTranslatef(scrollOffsetX, scrollOffsetY, 0);

        child.draw(g);
        g.setColor(Color.black);
        g.drawLine((int)minscrollZone.x, (int)minscrollZone.y,(int) maxscrollZone.x, (int)maxscrollZone.y);

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

        float oldCenterX = oldWidth / 2 - scrollOffsetX;
        float oldCenterY = oldHeight / 2 - scrollOffsetY;

        // scroll center is recompute when user change layout of the screen
        setScrollCenter(new Point(oldCenterX, oldCenterY));

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

        if (scrolling) {
            if (mouseEvent.getAction() == Action.MOUSE_RELEASED) {
                scrolling = false;
            } else if (mouseEvent.getAction() == Action.MOUSE_DRAGGED) {
            	this.setScrollOffset(new Point(scrollingBaseOffsetX + (mouseEvent.getX() - scrollingBaseX),
            			scrollingBaseOffsetY + (mouseEvent.getY() - scrollingBaseY)));
                
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

        if (!used) {
            used = super.onMouseEvent(mouseEvent);
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
    	// create a new point for scroll center
    	Point scrollcenter_point = new Point(layoutParams.getWidth() / 2 - scrollOffsetX, layoutParams.getHeight() / 2 - scrollOffsetY);
    	// return this point
        return scrollcenter_point;
    }

    public void setScrollCenter(Point point) {
    	// create a new point for scroll center
    	Point scrollcenter_point = new Point(layoutParams.getWidth() / 2 - point.x, layoutParams.getHeight() / 2 - point.y);
    	// set scroll offset for this point (into scrollzone)
    	setScrollOffset(scrollcenter_point);
    }

    public Point getScrollOffset() {
        return new Point(scrollOffsetX, scrollOffsetY);
    }

    public void setScrollOffset(Point point) {
        // do not go out of the scroll zone
    	scrollOffsetX = point.x;
        if (scrollOffsetX < minscrollZone.x) {scrollOffsetX = minscrollZone.x;}
        if (scrollOffsetX > maxscrollZone.x) {scrollOffsetX = maxscrollZone.x;}
        
        scrollOffsetY = point.y;
        if (scrollOffsetY < minscrollZone.y) {scrollOffsetY = minscrollZone.y;}
        if (scrollOffsetY > maxscrollZone.y) {scrollOffsetY = maxscrollZone.y;}
        
        System.out.println("scrollOffsetX = "+scrollOffsetX);
        System.out.println("scrollOffsetY = "+scrollOffsetY);
    }
    
    public void setScrollZone(Point min, Point max) {
    	// define min and max for scrollzone
    	minscrollZone.x = min.x - layoutParams.getWidth()/2;
    	minscrollZone.y = min.y - layoutParams.getHeight()/2;
    	maxscrollZone.x = max.x ;
    	maxscrollZone.y = max.y ;
    }

}
