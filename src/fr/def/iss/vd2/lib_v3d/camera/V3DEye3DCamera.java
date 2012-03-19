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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.element.V3DBoundingBox;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;

/**
 * @author fberto
 */
public class V3DEye3DCamera extends V3DCamera {

    private V3DVect3 position = new V3DVect3(0, 0, 0);
    private V3DVect3 eye = new V3DVect3(0, 0, 0);
    private V3DVect3 top = new V3DVect3(0, 0, 0);
    private float distance = 10;
    private float perspective = 45;
    private boolean showCenter = false;

    public V3DEye3DCamera(V3DContext context) {
        super(context);
    }

    @Override
    protected void configureView(GLU glu) {
        GLU.gluLookAt(eye.x, eye.y, eye.z, position.x, position.y, position.z, top.x, top.y, top.z);
    }

    public V3DVect3 getPosition() {
        return position;
    }

    public void setPosition(V3DVect3 position) {
        this.position.copy(position);
        firePositionChanged();
    }

    public void setEye(V3DVect3 eye) {
        this.eye = eye;
    }
    
    public void setTop(V3DVect3 top) {
        this.top = top;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        firePositionChanged();
    }

    public float getDistance() {
        return distance;
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

            // y
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
            GL11.glVertex3d(0, -1, 0);
            GL11.glVertex3d(0, 1, 0);

            GL11.glVertex3d(0.1, 0.9, 0);
            GL11.glVertex3d(0, 1, 0);

            GL11.glVertex3d(-0.1, 0.9, 0);
            GL11.glVertex3d(0, 1, 0);

            // z
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
            GL11.glVertex3d(position.x, position.y, position.z);

        }
        GL11.glEnd();

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

    @Override
    public void enableRotation() {
        GL11.glPopMatrix();
    }

    @Override
    public void disableRotation() {
        GL11.glPushMatrix();
        GL11.glRotatef(getRotation().z + 90, 0, 0, 1.0f);
        GL11.glRotatef(getRotation().x - 90, -1.0f, 0, 0.0f);

    }

    @Override
    public float getRelativeSize() {
        return (float) (distance * Math.tan(Math.toRadians(perspective / 2f)));
    }

    @Override
    protected void initPerspective() {
        if (currentHeight <= 0) { // avoid a divide by zero error!
            currentHeight = 1;
        }
        final float h = currentWidth / currentHeight;

        GLU.gluPerspective(perspective, h, distance * 0.001f, distance * 100000.0f);

    }

    public Point2D.Float pick(int pickX, int pickY, float pickH) {

        /*final double ratio = currentWidth / currentHeight;

        double y;
        double x;

        // apply rotation

        double pickXT1 = 2 * ((pickX - currentWidth / 2) / currentWidth) * ratio;
        double pickYT1 = 2 * ((pickY - currentHeight / 2) / currentHeight);

        // heigh of the camera
        double a = (distance * Math.sin(Math.toRadians(rotation.x))) - pickH;

        // size of screen at 0
        double N = (distance * Math.tan(Math.toRadians(perspective / 2)));

        // pick distance to center
        double n = pickYT1 * N;

        // angle enter camera center and y ray
        double beta = -Math.atan(n / distance);

        // distance between bottom projection to pick projection
        double y1 = (a * Math.tan(Math.toRadians(90 - rotation.x) + beta));

        // distance between bottom projection to center projection
        double y2 = (a * Math.tan(Math.toRadians(90 - rotation.x)));

        double y0 = y1 - y2;

        // x compute

        double h1 = (a / Math.cos(Math.toRadians(90 - rotation.x) + beta));

        double h2 = h1 * Math.cos(beta);

        // size of screen at 0
        double M = (h2 * Math.tan(Math.toRadians(perspective / 2)));

        // pick distance to center
        double x0 = pickXT1 * M;

        double xRot = x0 * Math.cos(Math.toRadians(rotation.z)) + y0 * Math.sin(Math.toRadians(rotation.z));
        double yRot = y0 * Math.cos(Math.toRadians(rotation.z)) - x0 * Math.sin(Math.toRadians(rotation.z));

        y = yRot + position.y;
        x = xRot + position.x;

        return new Point2D.Float((float) x, (float) y);*/

        return new Point2D.Float(0,0);
    }

    public float getfitDistance(V3DVect3 size) {
        /*float sizeX = (float) Math.abs(size.x * Math.sin(Math.toRadians(rotation.z)))
                + (float) Math.abs(size.y * Math.cos(Math.toRadians(rotation.z)));
        float sizeY = (float) Math.abs(size.z * Math.cos(Math.toRadians(rotation.x)) + Math.sin(Math.toRadians(rotation.x))
                * (Math.abs(size.y * Math.sin(Math.toRadians(rotation.z))) + Math.abs(size.x * Math.cos(Math.toRadians(rotation.z)))));

        if (currentHeight <= 0) { // avoid a divide by zero error!
            currentHeight = 1;
        }
        final float h = currentWidth / currentHeight;

        if (sizeY <= 0) { // avoid a divide by zero error!
            sizeY = 1;
        }

        float targetH = sizeX / sizeY;
        float l = 0;

        if (targetH < h) {
            l = 1.1f * Math.abs(sizeY) / 2;
        } else {
            l = 1.1f * Math.abs(sizeX) / (2 * h);
        }

        if (Float.isInfinite(l)) {
            l = 0;
        }

        return (l / (float) Math.tan(Math.toRadians(perspective / 2f)));*/
        return 0;
    }

    public void fit(final V3DVect3 center, final V3DVect3 size) {
        /*Runnable fitAction = new Runnable() {

            @Override
            public void run() {
                setPosition(center);
                setDistance(getfitDistance(size));
            }
        };

        if (isConfigured()) {
            fitAction.run();
        } else {
            setCameraInitialisation(fitAction);
        }*/

    }

    public void fit(V3DBoundingBox boundingBox) {
        V3DVect3 scenePosition = boundingBox.getPosition();
        V3DVect3 sceneSize = boundingBox.getSize();
        V3DVect3 center = new V3DVect3(scenePosition.x, scenePosition.y, scenePosition.z);
        fit(center, sceneSize.multiply(1.2f));
    }

    @Override
    public void fitAll() {
        V3DBoundingBox bbox = currentScene.getRootElement().getBoundingBox();
        fit(bbox);
    }

    public boolean containsAll() {
        V3DElement rootElt = null;
        if (currentScene != null) {
            rootElt = currentScene.getRootElement();
        }
        if (rootElt == null) {
            return true;
        } else {
            V3DVect3 sceneSize = rootElt.getBoundingBox().getSize();
            return (getfitDistance(sceneSize) < distance);
        }
    }

    @Override
    public void fitAllIfInvalid() {
        if (distance == 0) {
            fitAll();
        }
    }

    // Position and Rotation controller

    List<Camera3DChangeListener> camera3DChangeListeners = new ArrayList<Camera3DChangeListener>();

    public void addCameraChangeListener(Camera3DChangeListener listener) {
        camera3DChangeListeners.add(listener);
    }

    public void removeCameraChangeListener(Camera3DChangeListener listener) {
        camera3DChangeListeners.remove(listener);
    }

    public void firePositionChanged() {
        for (Camera3DChangeListener listener : camera3DChangeListeners) {
            listener.positionChanged();
        }
    }

    public void fireRotationChanged() {
        for (Camera3DChangeListener listener : camera3DChangeListeners) {
            listener.rotationChanged();
        }
    }

    @Override
    public void center(V3DVect3 position) {
        setPosition(position);
    }

    public interface Camera3DChangeListener {
        public void positionChanged();

        public void rotationChanged();
    }

    @Override
    public V3DVect3 getRotation() {
        return new V3DVect3(0, 0, 0);
    }

}
