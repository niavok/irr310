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

package com.irr310.i3d.scene.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import com.irr310.client.graphics.GraphicalElement;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Part;
import com.irr310.i3d.scene.I3dCamera;
import com.irr310.i3d.scene.I3dEye3DCamera;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;

/**
 * @author fberto
 */

public class I3dFollow3DCameraController implements V3DCameraController, GraphicalElement {
    I3dEye3DCamera camera;
    float cameraXInitial = 0;
    float cameraYInitial = 0;
    float cameraTheta = 0;
    float cameraPhi = 0;
    float cameraThetaInitial = 0;
    float cameraPhiInitial = 0;
    float mouseXInitial = 0;
    float mouseYInitial = 0;
    float kx = -0.00060f;
    float ky = -0.00060f;
    float ktheta = -0.005f;
    float kphi = -0.005f;
    boolean translating = false;
    boolean rotating = false;
    private Point2D.Float mouseInitialPick;
    private int translationButton = MouseEvent.BUTTON1;
    private int rotationButton = MouseEvent.BUTTON3;
    private Followable element;
    private TransformMatrix eye;
    private TransformMatrix target;
    private TransformMatrix top;
    private float distance;
    private float xOffset;
    private float yOffset;
    private float zOffset;

    private enum MovementType {

        TRANSLATE, ROTATE,
    }

    public interface Followable {

        TransformMatrix getTransform();
        
    }
    
    public I3dFollow3DCameraController(I3dEye3DCamera camera) {
        this.camera = camera;

        camera.addController(this);
        eye = TransformMatrix.identity();
        target = TransformMatrix.identity();
        top = TransformMatrix.identity();
        
        distance = 500;
        xOffset = -2;
        yOffset = -30;
        zOffset = 2;
        
    }

    public void setFollowed(Followable element) {
        this.element = element;

    }

    public void setRotationButton(int rotationButton) {
        this.rotationButton = rotationButton;
    }

    public void setTranslationButton(int translationButton) {
        this.translationButton = translationButton;
    }

    @Override
    public void onEvent(V3DInputEvent e) {
        if (e.isConsumed()) {
            return;
        }

        if (e instanceof V3DMouseEvent) {
            V3DMouseEvent em = (V3DMouseEvent) e;
            switch (em.getAction()) {
                case MOUSE_DRAGGED: {
                    mouseDragged(em);
                }
                    break;
                case MOUSE_MOVED: {
                    mouseMoved(em);
                }
                    break;
                case MOUSE_CLICKED: {
                    mouseClicked(em);
                }
                    break;
                case MOUSE_PRESSED: {
                    mousePressed(em);
                }
                    break;
                case MOUSE_RELEASED: {
                    mouseReleased(em);
                }
                    break;
                case MOUSE_WHEEL_UP:
                case MOUSE_WHEEL_DOWN:{
                    mouseWheelMoved(em);        
                }
                    break;
            }
        }
    }

    public void mouseDragged(V3DMouseEvent e) {
        mouseMoving(e);
    }

    public void mouseMoved(V3DMouseEvent e) {
        // mouseMoving(e);
    }

    public void mouseClicked(V3DMouseEvent e) {
    }

    public void mousePressed(V3DMouseEvent e) {

        beginMove(MovementType.ROTATE, e);
        
//        if (e.getButton() == translationButton) {
//            beginMove(MovementType.TRANSLATE, e);
//        } else if (e.getButton() == rotationButton) {
//            beginMove(MovementType.ROTATE, e);
//        }

    }

    private void beginMove(MovementType type, V3DMouseEvent e) {
        if (type == MovementType.TRANSLATE) {
            translating = true;
            rotating = false;
        } else if (type == MovementType.ROTATE) {
            translating = false;
            rotating = true;
        }

        mouseInitialPick = camera.pick(e.getX(), e.getY(), 0);
        mouseXInitial = e.getX();
        mouseYInitial = e.getY();

//        cameraXInitial = camera.getPosition().x;
//        cameraYInitial = camera.getPosition().y;
        cameraThetaInitial = cameraTheta;
        cameraPhiInitial = cameraPhi;
    }

    public void mouseReleased(V3DMouseEvent e) {
        translating = false;
        rotating = false;
    }

    public void mouseEntered(V3DMouseEvent e) {
    }

    public void mouseExited(V3DMouseEvent e) {
    }

    private void mouseMoving(V3DMouseEvent e) {

//        
//        if (translating) {
//            Point2D.Float mousePick = camera.pick(e.getX(), e.getY(), 0);
//            float x = cameraXInitial - mousePick.x + mouseInitialPick.x;
//            float y = cameraYInitial - mousePick.y + mouseInitialPick.y; // mouseInitialPick
//                                                                         // =
//                                                                         // mousePick;
//            camera.setPosition(x, y, camera.getPosition().z);
//            cameraXInitial = x;
//            cameraYInitial = y;
//        }
        if (rotating) {
            float theta = cameraThetaInitial + ((float) e.getX() - mouseXInitial) * ktheta;
            float phi = cameraPhiInitial + ((float) e.getY() - mouseYInitial) * kphi;
            if (phi <= -Math.PI/2) {
                phi = (float) -(Math.PI/2);
                mouseYInitial = (float) e.getY();
                cameraPhiInitial = (float) -(Math.PI/2);;
            }
            if (phi >= Math.PI/2) {
                phi = (float) (Math.PI/2);
                mouseYInitial = (float) e.getY();
                cameraPhiInitial = (float) (Math.PI/2);
            }
            
            cameraPhi = phi;
            cameraTheta = theta;
        }
         
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        /*
         * if (e.getKeyCode() == KeyEvent.VK_ADD) { float distance =
         * camera.getDistance(); camera.setDistance(distance / 1.08f); } if
         * (e.getKeyCode() == KeyEvent.VK_SUBTRACT) { float distance =
         * camera.getDistance(); camera.setDistance(distance * 1.08f); }
         */
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseWheelMoved(V3DMouseEvent e) {

        if (e.getAction() == Action.MOUSE_WHEEL_UP) {
            distance = distance / 1.08f;
        } else {
            distance = distance * 1.08f;
        }

    }

    @Override
    public void notifyRemove() {
    }
    
    @Override
    public void init(Timestamp time) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void update(Timestamp time) {
        if (element != null) {
            TransformMatrix transform = element.getTransform();
            Vec3 translation = transform.getTranslation();

            // Target
            target = TransformMatrix.identity();
            // target.translate(2,0,2);
            // target.translate(0,0,2);
            target.translate(xOffset, yOffset, zOffset);
            // target.translate(0,0,0);
            
            target.rotateX(cameraPhi);
            target.rotateZ(cameraTheta);
            target.preMultiply(transform);
            
            
            V3DVect3 targetPosition = target.getTranslation().toV3DVect3();
            camera.setPosition(targetPosition);

            // Eye
            eye = TransformMatrix.identity();
            // eye.translate(2,-20,2);
            // eye.translate(0,-20,2);
            // eye.translate(0,4,2);
            
            eye.translate(xOffset, yOffset, zOffset);
            
            
            eye.translate(0, -distance, 0);
            
            eye.rotateX(cameraPhi);
            eye.rotateZ(cameraTheta);
            // eye.translate(4,10,5);
            eye.preMultiply(transform);
            
            
            V3DVect3 eyePosition = eye.getTranslation().toV3DVect3();
            camera.setEye(eyePosition);

            top = TransformMatrix.identity();

            TransformMatrix rotation = transform.identity();
            rotation.preMultiply(transform);
            rotation.translate(transform.getTranslation().negative());

//            Vec3 topVect = new Vec3(distance - yOffset, -xOffset, 0).cross(new Vec3(-xOffset, distance - yOffset, -zOffset)).normalize();
            Vec3 topVect = new Vec3(0, 0, 1);
            
            
            
            
            
            
            

            // top.translate(1,0,1);
            top.translate(topVect);
            top.rotate(new Vec3(Math.toDegrees(cameraPhi), 0, 0));
            top.rotate(new Vec3(0, 0, Math.toDegrees(cameraTheta)));

            top.preMultiply(rotation);
            V3DVect3 topPosition = top.getTranslation().toV3DVect3();
            
            camera.setTop(topPosition);
        }
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public void destroy() {
        // TODO find what was the use of this ?
        //renderer.destroyElement(this);
    }

    @Override
    public boolean isDisplayable() {
        return false;
    }

    @Override
    public I3dElement getV3DElement() {
        return null;
    }

    public I3dCamera getCamera() {
        return camera;
    }

    public Followable getFollowed() {
        return element;
    }

    public Vec3 getEye() {
        return eye.getTranslation();
    }

    public Vec3 getTarget() {
        return target.getTranslation();
    }

    public Vec3 getTop() {
        return top.getTranslation();
    }

    public void configure(float distance, float x, float y, float z) {
        this.distance = distance;
        xOffset = x;
        yOffset = y;
        zOffset = z;
        
    }
}
