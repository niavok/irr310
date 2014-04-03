// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.

package fr.def.iss.vd2.lib_v3d.camera;

import java.awt.Point;

import com.irr310.i3d.scene.I3dCamera;

/**
 * Un V3DCameraBinding permet de definir dans quel zone du canvas sera dessinée
 * une camera.
 *<pre>
 * 
 * La localisation du binding est défini par 4 coordonnées : x, y largeur et
 * hauteur.
 * Chacune de ces coordonnées peut être défini en absolue, en relatif et dans
 * les 2 cas, par rapport au bord gauche ou le bord droit.
 *
 *
 * 2 méthodes static utilitaires permettent de créér facilement les bindings les
 * plus classiques :
 *  - un binding qui occupe tout le canvas avec buildFullscreenCamera
 *  - un binding défini par des coordonnées absolue avec buildAbsoluteBinding
 *
 *</pre>
 * @author fberto
 */
public class V3DCameraBinding {

    public enum LocationMode {

        ABSOLUTE,
        RELATIVE,
        ABSOLUTE_INVERT,
        RELATIVE_INVERT,
    }

    /**
     * create a camera binding
     */
    public V3DCameraBinding() {
//        gui = new V3DGui(this);
    }

    /**
     * Return the gui root
     * @return
     */
//    public V3DGui getGui() {
//        return gui;
//    }

    /**
     * Define the bound camera
     * @param camera
     */
    public void setCamera(I3dCamera camera) {
        this.camera = camera;
        camera.setSize(width, height);
    }


    /**
     * Return a binding which fill all the canvas
     * @param camera
     * @return
     */
    public static V3DCameraBinding buildFullscreenCamera(I3dCamera camera) {
        V3DCameraBinding cameraBinding = new V3DCameraBinding();
        cameraBinding.camera = camera;
        cameraBinding.preferredX = 0;
        cameraBinding.preferredY = 0;
        cameraBinding.preferredWidth = 100;
        cameraBinding.preferredHeight = 100;

        cameraBinding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
        cameraBinding.preferredYMode = V3DCameraBinding.LocationMode.RELATIVE;
        cameraBinding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
        cameraBinding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

        return cameraBinding;
    }

    /**
     * Return a binding with absolute position and size
     * @param camera
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static V3DCameraBinding buildAbsoluteCamera(I3dCamera camera, int x, int y, int width, int height) {
        V3DCameraBinding cameraBinding = new V3DCameraBinding();
        cameraBinding.camera = camera;
        cameraBinding.preferredX = x;
        cameraBinding.preferredY = y;
        cameraBinding.preferredWidth = width;
        cameraBinding.preferredHeight = height;

        cameraBinding.preferredXMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        cameraBinding.preferredYMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        cameraBinding.preferredWidthMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        cameraBinding.preferredHeightMode = V3DCameraBinding.LocationMode.ABSOLUTE;

        return cameraBinding;

    }

    //
    //------------------
    // Private stuff
    //------------------
    //

    public I3dCamera camera;
    public int preferredX;
    public int preferredY;
    public int preferredWidth;
    public int preferredHeight;
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
    public int mouseX = 0;
    public int mouseY = 0;
//    private V3DGui gui;
    private Point lastMousePosition = new Point();
    
    /**
     * Internal private method
     * @return
     */
//    public boolean isFocused() {
//        return gui.isFocused();
//    }

    /**
     * Internal private method
     * @param parentWidth
     * @param parentHeight
     */
    public void compute(int parentWidth, int parentHeight) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        compute();
    }

    /**
     * Internal private method
     */
    public void compute() {
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


        mouseX = tempX;
        mouseY = tempY;

        x = tempX;
        y = parentHeight - tempY - tempHeight;
        width = tempWidth;
        height = tempHeight;

//        gui.repack();
        camera.setSize(width, height);
        //gui.generate();

    }

    /**
     * Internal private method
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
     * @param testMouseX
     * @param testMouseY
     * @return
     */
    public boolean containsMouse(int testMouseX, int testMouseY) {

        if (testMouseX < mouseX || testMouseY < mouseY) {
            return false;
        }

        if (testMouseX > mouseX + width || testMouseY > mouseY + height) {
            return false;
        }

        return true;
    }

    /**
     * Internal private method
     * @return
     */
    public Point getLastMousePosition() {
        return lastMousePosition;
    }

    /**
     * Internal private method
     * @param lastMousePosition
     */
    public void setLastMousePosition(Point lastMousePosition) {
        this.lastMousePosition = lastMousePosition;
    }
}
