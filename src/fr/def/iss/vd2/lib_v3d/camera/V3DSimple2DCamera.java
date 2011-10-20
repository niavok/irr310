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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;

/**
 *
 * @author fberto
 */
public class V3DSimple2DCamera extends V3DCamera {

    private Point2D.Float position = new Point2D.Float(0, 0);
    private float rotation = 0;
    private float zoom = 10;
    private float lastWidth = 0;
    private float lastHeight = 0;
    private boolean showCenter = false;

    public V3DSimple2DCamera(V3DContext context) {
        super(context);
    }

    @Override
    protected void configureView( GLU glu) {

        if (lastWidth != width || lastHeight != height) {
            fireZoomChanged();
        }
        lastWidth = width;
        lastHeight = height;


        GL11.glRotatef(-90, 0, 0, 1.0f);
        GL11.glRotatef(rotation, 0, 0, 1.0f);


        glu.gluLookAt(position.x, position.y, 0, position.x, position.y, -200, 1, 0, 0);

    }

    public Point2D.Float getPosition() {
        return position;
    }

    public void move(Point2D.Float offset) {
        Point2D.Float currentPosition = getPosition();
        setPosition(currentPosition.x + offset.x, currentPosition.y + offset.y);
    }

    public void setPosition(Point2D.Float position) {
        this.position = position;
        firePositionChanged();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        firePositionChanged();
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        fireZoomChanged();
    }

    public void setShowCenter(boolean showCenter) {
        this.showCenter = showCenter;
    }

    @Override
    protected void preDisplayScene() {
        if (showCenter) {
            displayTarget();
        }
    }

    private void displayTarget() {

        GL11.glColor4f(1.0f, 0.5f, 0.5f, 0.8f);
        GL11.glLineWidth(1.2f);
        GL11.glBegin(GL11.GL_LINES);
        {

            GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.8f);
            GL11.glVertex3d(-1, 0, 0);
            GL11.glVertex3d(1, 0, 0);

            GL11.glVertex3d(0.9, 0.1, 0);
            GL11.glVertex3d(1, 0, 0);

            GL11.glVertex3d(0.9, -0.1, 0);
            GL11.glVertex3d(1, 0, 0);

            //y
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
            GL11.glVertex3d(0, -1, 0);
            GL11.glVertex3d(0, 1, 0);

            GL11.glVertex3d(0.1, 0.9, 0);
            GL11.glVertex3d(0, 1, 0);

            GL11.glVertex3d(-0.1, 0.9, 0);
            GL11.glVertex3d(0, 1, 0);

            //z
            GL11.glColor4f(0.0f, 0.0f, 1.0f, 0.8f);
            GL11.glVertex3d(0, 0, -1);
            GL11.glVertex3d(0, 0, 1);

            GL11.glVertex3d(0.1, 0, 0.9);
            GL11.glVertex3d(0, 0, 1);

            GL11.glVertex3d(-0.1, 0, 0.9);
            GL11.glVertex3d(0, 0, 1);


        }
        GL11.glEnd();

        GL11.glPointSize(1.2f);
        GL11.glBegin(GL11.GL_POINTS);
        {
            GL11.glVertex3d(position.x, position.y, 0);

        }
        GL11.glEnd();


    }

    @Override
    public void enableRotation() {
        GL11.glPopMatrix();
    }

    @Override
    public void disableRotation() {
        GL11.glPushMatrix();
        GL11.glRotatef(-rotation, 0, 0, 1.0f);
    }

    @Override
    public float getRelativeSize() {
        /*if (height <= 0) { // avoid a divide by zero error!
        height = 1;
        }
        final float h = width / height;
        return h * zoom;*/


        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }

        float x = 2 * zoom / height;

        return x;


    }

    public Point2D.Float getSize() {
        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        final float h = width / height;

        return new Point2D.Float(2 * zoom * h, 2 * zoom);
    }

    @Override
    protected void initPerspective() {
        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        final float h = width / height;

        GL11.glOrtho(-zoom * h, zoom * h, -zoom, zoom, -2000.0, 2000.0);


    }

    public Point2D.Float pick(int pickX, int pickY) {

        final float h = width / height;

        pickY = (int) height - pickY;

        float x = ((float) pickX) / ((float) width) * (2 * zoom * h) + (-zoom * h) + position.x;
        float y = ((float) pickY) / ((float) height) * (2 * zoom) + (-zoom) + position.y;

        return new Point2D.Float(x, y);
    }

    public Point unPick(float x, float y) {

        final float h = width / height;

        int pickX = (int) (((x - position.x + zoom * h) * width) / (2 * zoom * h));
        int pickY = (int) (((y - position.y + zoom) * height) / (2 * zoom));

        pickY = (int) height - pickY;


        return new Point(pickX, pickY);
    }

    public Point2D.Float dist(int distX, int distY) {

        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }

        if (width <= 0) { // avoid a divide by zero error!
            width = 1;
        }
        final float h = width / height;

        float x = ((float) distX) / ((float) width) * (2 * zoom * h);
        float y = ((float) distY) / ((float) height) * (2 * zoom);

        return new Point2D.Float(x, y);
    }

    public Point distMouse(Point2D.Float dist) {

        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        final float h = width / height;

        int x = (int) (((float) width) * dist.x / (2 * zoom * h));
        int y = (int) (((float) height) * dist.y / (2 * zoom));

        return new Point(x, y);
    }

    public void fit(Point2D.Float leftTopCorner, Point2D.Float size) {
        fit(leftTopCorner, size, 5);
    }

    public float getFitZoom(Point2D.Float size, int padding) {

        float fitZoom = 0;

        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        final float h = width / height;

        if (h > 0) {
            //Compute zoom
            float usableWidth = size.x * (1 + padding / 100f);
            float usableHeight = size.y * (1 + padding / 100f);


            float zoomWidth = usableWidth / (2f * h);
            float zoomHeight = usableHeight / 2f;

            fitZoom = Math.max(zoomWidth, zoomHeight);
            if (fitZoom == 0) {
                fitZoom = 1;
            }

        }

        return fitZoom;
    }

    public void fit(final Point2D.Float leftTopCorner, final Point2D.Float size, final int padding) {



        Runnable fitAction = new Runnable() {

            @Override
            public void run() {
                zoom = getFitZoom(size, padding);

                setPosition(size.x / 2 + leftTopCorner.x, size.y / 2 + leftTopCorner.y);

                fireZoomChanged();
            }
        };

        if (isConfigured()) {
            fitAction.run();
        } else {
            setCameraInitialisation(fitAction);
        }





    }

    @Override
    public void fitAll() {
        fitAll(5);
    }

    public void fitAll(int padding) {
        V3DVect3 scenePosition = currentScene.getRootElement().getBoundingBox().getPosition();
        V3DVect3 sceneSize = currentScene.getRootElement().getBoundingBox().getSize();

        fit(new Point2D.Float(scenePosition.x - sceneSize.x / 2f, scenePosition.y - sceneSize.y / 2f),
                new Point2D.Float(sceneSize.x, sceneSize.y), padding);
    }

    public boolean containsAll() {

        V3DVect3 sceneSize = currentScene.getRootElement().getBoundingBox().getSize();
        return zoom > getFitZoom(new Point2D.Float(sceneSize.x, sceneSize.y), 5);
    }
    //Zoom Listener
    List<CameraListener> cameraListenerList = new ArrayList<CameraListener>();

    public void addCameraListener(CameraListener listener) {
        cameraListenerList.add(listener);
    }

    public void removeCameraChangeListener(CameraListener listener) {
        cameraListenerList.remove(listener);
    }

    private void fireZoomChanged() {
        for (CameraListener listener : cameraListenerList) {
            listener.zoomChanged();
        }
    }

    private void firePositionChanged() {
        for (CameraListener listener : cameraListenerList) {
            listener.positionChanged();
        }
    }

    @Override
    public void fitAllIfInvalid() {
        if (zoom == 0) {
            fitAll();
        }
    }

    @Override
    public void center(V3DVect3 position) {
        setPosition(position.x, position.y);
    }

    public interface CameraListener {

        public void zoomChanged();

        public void positionChanged();
    }
}
