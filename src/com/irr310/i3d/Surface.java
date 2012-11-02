package com.irr310.i3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.irr310.i3d.view.Activity;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class Surface {

    private Activity currentActivity;
    private Stack<Intent> intentStack = new Stack<Intent>();
    private Map<Intent, Activity> activityMap = new HashMap<Intent, Activity>();
    private Color backgroundColor;

    public Surface(I3dContext context) {
        this.context = context;
        backgroundColor = Color.black;
    }

    public void startActivity(Intent intent) {
        Activity activity = null;

        if (!intentStack.contains(intent)) {
            // New Activity
            try {
                activity = intent.getActivityClass().newInstance();
                activityMap.put(intent, activity);
                activity.setContext(context);
                activity.assignSurface(this);
                activity.setIntent(intent);
                activity.onCreate(null);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            // Set existing activity on top
            activity = activityMap.get(intent);
            intentStack.remove(intent);
        }

        // Pause current activity
        if (currentActivity != null) {
            currentActivity.onPause();
        }

        currentActivity = activity;

        if (currentActivity != null) {
            intentStack.push(intent);
            currentActivity.forceLayout();
            currentActivity.onResume();
        }
    }

    public void update(Time absTime, Time gameTime) {
        Activity lastActivity = null;
        while (currentActivity != lastActivity && currentActivity != null) {
            lastActivity = currentActivity;
            currentActivity.update(absTime, gameTime);
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void draw() {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glViewport(x, y, width, height);
        GL11.glScissor(x, y, width, height);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        // 2D
        GLU.gluOrtho2D(0, width, height, 0);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if (currentActivity != null) {
            currentActivity.draw();
        }

        GL11.glPopAttrib();
    }

    public enum LocationMode {

        ABSOLUTE, RELATIVE, ABSOLUTE_INVERT, RELATIVE_INVERT,
    }

    //
    // ------------------
    // Private stuff
    // ------------------
    //

    public int preferredX;
    public int preferredY;
    public int preferredWidth;
    public int preferredHeight;
    public int marginTop;
    public int marginBottom;
    public int marginLeft;
    public int marginRight;
    public LocationMode preferredXMode;
    public LocationMode preferredYMode;
    public LocationMode preferredWidthMode;
    public LocationMode preferredHeightMode;
    public int x;
    public int y;
    public int width;
    public int height;
    public int parentWidth;
    public int parentHeight;
//    public int mouseX = 0;
//    public int mouseY = 0;
    //private Point lastMousePosition = new Point();
    private I3dContext context;

    /**
     * Internal private method
     * 
     * @return
     */
    /*
     * public boolean isFocused() { }
     */

    /**
     * Internal private method
     * 
     * @param parentWidth
     * @param parentHeight
     */
    public void configure(int parentWidth, int parentHeight) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        configure();
    }

    /**
     * Internal private method
     */
    public void configure() {
        int tempX = 0;
        int tempY = 0;
        int tempWidth = 0;
        int tempHeight = 0;

        if (preferredXMode == LocationMode.ABSOLUTE) {
            tempX = preferredX;
        } else if (preferredXMode == LocationMode.RELATIVE) {
            tempX = (preferredX * parentWidth) / 100;
        }

        if (preferredYMode == LocationMode.ABSOLUTE) {
            tempY = preferredY;
        } else if (preferredYMode == LocationMode.RELATIVE) {
            tempY = (preferredY * parentHeight) / 100;
        }

        if (preferredWidthMode == LocationMode.ABSOLUTE) {
            tempWidth = preferredWidth;
        } else if (preferredWidthMode == LocationMode.RELATIVE) {
            tempWidth = (preferredWidth * parentWidth) / 100;
        }

        if (preferredHeightMode == LocationMode.ABSOLUTE) {
            tempHeight = preferredHeight;
        } else if (preferredHeightMode == LocationMode.RELATIVE) {
            tempHeight = (preferredHeight * parentHeight) / 100;
        }

//        mouseX = tempX;
//        mouseY = tempY;

        x = tempX + marginLeft;
        y = parentHeight - tempY - tempHeight - marginBottom;
        width = tempWidth - marginLeft - marginRight;
        height = tempHeight - marginTop - marginBottom;

        if (currentActivity != null) {
            currentActivity.forceLayout();
        }

        // gui.repack();
        // camera.setSize(width, height);
        // gui.generate();

    }

    /**
     * Internal private method
     * 
     * @param mouseX
     * @param mouseY
     * @return
     */
    public boolean contains(int mouseX, int mouseY) {

        if (mouseX < x || mouseY < y) {
            return false;
        }

        if (mouseX > x + width || mouseY > y + height) {
            return false;
        }

        return true;
    }

    /**
     * Internal private method
     * 
     * @param testMouseX
     * @param testMouseY
     * @return
     */
//    public boolean containsMouse(int testMouseX, int testMouseY) {
//
//        if (testMouseX < mouseX || testMouseY < mouseY) {
//            return false;
//        }
//
//        if (testMouseX > mouseX + width || testMouseY > mouseY + height) {
//            return false;
//        }
//
//        return true;
//    }

    /**
     * Internal private method
     * 
     * @return
     */
//    public Point getLastMousePosition() {
//        return lastMousePosition;
//    }

    /**
     * Internal private method
     * 
     * @param lastMousePosition
     */
//    public void setLastMousePosition(Point lastMousePosition) {
//        this.lastMousePosition = lastMousePosition;
//    }

    public void setContext(I3dContext context) {
        this.context = context;
    }
    
    public void onMouseEvent(V3DMouseEvent mouseEvent) {
        if (currentActivity != null) {
            currentActivity.onMouseEvent(mouseEvent);
        }
    }

}
