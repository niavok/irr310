package com.irr310.client.graphics;

import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Surface;

public class SurfaceFactory {

    /**
     * Return a surface which fill all the canvas
     * @param camera
     * @return
     */
    public static Surface buildFullscreenSurface(I3dContext context) {
        Surface surface = new Surface();
        surface.setContext(context);
        surface.preferredX = 0;
        surface.preferredY = 0;
        surface.preferredWidth = 100;
        surface.preferredHeight = 100;
        surface.marginTop = 0;
        surface.marginBottom = 0;
        surface.marginLeft = 0;
        surface.marginRight = 0;

        surface.preferredXMode = Surface.LocationMode.RELATIVE;
        surface.preferredYMode = Surface.LocationMode.RELATIVE;
        surface.preferredWidthMode = Surface.LocationMode.RELATIVE;
        surface.preferredHeightMode = Surface.LocationMode.RELATIVE;

        return surface;
    }
    
    public static Surface buildFullscreenSurface(int marginTop, int marginBottom, int marginLeft, int marginRight) {
        Surface surface = new Surface();
        surface.preferredX = 0;
        surface.preferredY = 0;
        surface.preferredWidth = 100;
        surface.preferredHeight = 100;
        surface.marginTop = marginTop;
        surface.marginBottom = marginBottom;
        surface.marginLeft = marginLeft;
        surface.marginRight = marginRight;

        surface.preferredXMode = Surface.LocationMode.RELATIVE;
        surface.preferredYMode = Surface.LocationMode.RELATIVE;
        surface.preferredWidthMode = Surface.LocationMode.RELATIVE;
        surface.preferredHeightMode = Surface.LocationMode.RELATIVE;

        return surface;
    }
    
    
    
    /**
     * Return a surface with absolute position and size
     * @param camera
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static Surface buildAbsoluteSurface(int x, int y, int width, int height) {
        Surface surface = new Surface();
        surface.preferredX = x;
        surface.preferredY = y;
        surface.preferredWidth = width;
        surface.preferredHeight = height;
        surface.marginTop = 0;
        surface.marginBottom = 0;
        surface.marginLeft = 0;
        surface.marginRight = 0;
        
        surface.preferredXMode = Surface.LocationMode.ABSOLUTE;
        surface.preferredYMode = Surface.LocationMode.ABSOLUTE;
        surface.preferredWidthMode = Surface.LocationMode.ABSOLUTE;
        surface.preferredHeightMode = Surface.LocationMode.ABSOLUTE;

        return surface;

    }

    public static Surface buildAbsoluteHeightRelativeWidthSurface(int x, int y, int width, int height) {
        Surface surface = new Surface();
        surface.preferredX = x;
        surface.preferredY = y;
        surface.preferredWidth = width;
        surface.preferredHeight = height;
        surface.marginTop = 0;
        surface.marginBottom = 0;
        surface.marginLeft = 0;
        surface.marginRight = 0;
        
        surface.preferredXMode = Surface.LocationMode.ABSOLUTE;
        surface.preferredYMode = Surface.LocationMode.ABSOLUTE;
        surface.preferredWidthMode = Surface.LocationMode.RELATIVE;
        surface.preferredHeightMode = Surface.LocationMode.ABSOLUTE;

        return surface;
    }




    
    
}
