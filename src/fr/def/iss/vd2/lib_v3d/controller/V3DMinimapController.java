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

package fr.def.iss.vd2.lib_v3d.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DBoundingBox;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DRectangle;

public class V3DMinimapController implements V3DCameraController {

    float cameraXInitial = 0;
    float cameraYInitial = 0;
    int mouseXInitial = 0;
    int mouseYInitial = 0;

    private V3DSimple2DCamera controllingCamera;
    private V3DSimple2DCamera controlledCamera;
    private V3DRectangle frame;
    private V3DRectangle frameTop;
    private V3DRectangle frameBottom;
    private V3DRectangle frameLeft;
    private V3DRectangle frameRight;
    private float zoomMaxFactor = -1;
    private float zoomMinFactor = 0.5f;

    public V3DMinimapController(V3DSimple2DCamera controllingCamera, V3DSimple2DCamera controlledCamera, V3DColor borderColor, V3DColor maskColor) {
        this.controllingCamera = controllingCamera;
        this.controlledCamera = controlledCamera;

        frame = new V3DRectangle(controllingCamera.getContext());
        frame.setTangible(false);
        frame.setThickness(2f);
        frame.setPosition(0, 0, 100);

        
        controllingCamera.getScene().add(new V3DColorElement(frame, borderColor));

        frameTop = new V3DRectangle(controllingCamera.getContext());
        frameTop.setTangible(false);
        frameTop.setPosition(0, 0, 50);
        frameTop.setRenderMode(V3DRectangle.RenderMode.PLAIN);
        controllingCamera.getScene().add(new V3DColorElement(frameTop, maskColor));


        frameBottom = new V3DRectangle(controllingCamera.getContext());
        frameBottom.setTangible(false);
        frameBottom.setPosition(0, 0, 50);
        frameBottom.setRenderMode(V3DRectangle.RenderMode.PLAIN);
        controllingCamera.getScene().add(new V3DColorElement(frameBottom, maskColor));

        frameLeft = new V3DRectangle(controllingCamera.getContext());
        frameLeft.setTangible(false);
        frameLeft.setPosition(0, 0, 50);
        frameLeft.setRenderMode(V3DRectangle.RenderMode.PLAIN);
        controllingCamera.getScene().add(new V3DColorElement(frameLeft, maskColor));

        frameRight = new V3DRectangle(controllingCamera.getContext());
        frameRight.setTangible(false);
        frameRight.setPosition(0, 0, 50);
        frameRight.setRenderMode(V3DRectangle.RenderMode.PLAIN);
        controllingCamera.getScene().add(new V3DColorElement(frameRight, maskColor));





        updateFrame();

        controlledCamera.addCameraListener(new V3DSimple2DCamera.CameraListener() {

            @Override
            public void zoomChanged() {
                updateFrame();
            }

            @Override
            public void positionChanged() {
                updateFrame();
            }
        });
        
    }

    public void onEvent(V3DInputEvent e) {
        /*if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;
            switch (em.getID()) {
                case MouseEvent.MOUSE_DRAGGED:
                    {
                        mouseDragged(em);
                    }
                    break;
                case MouseEvent.MOUSE_MOVED:
                    {
                        mouseMoved(em);
                    }
                    break;
                case MouseEvent.MOUSE_CLICKED:
                    {
                        mouseClicked(em);
                    }
                    break;
                case MouseEvent.MOUSE_ENTERED:
                    {
                        mouseEntered(em);
                    }
                    break;
                case MouseEvent.MOUSE_EXITED:
                    {
                        mouseExited(em);
                    }
                    break;
                case MouseEvent.MOUSE_PRESSED:
                    {
                        mousePressed(em);
                    }
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    {
                        mouseReleased(em);
                    }
                    break;
            }
        }
        if (e instanceof MouseWheelEvent) {
            MouseWheelEvent em = (MouseWheelEvent) e;
            mouseWheelMoved(em);
        }*/
      
    }
    boolean translating = false;

    private void mouseDragged(MouseEvent em) {
        mouseMoving(em);
    }

    public void mouseMoved(MouseEvent e) {
        mouseMoving(e);
    }

    private void mouseClicked(MouseEvent em) {
        if (em.getButton() == MouseEvent.BUTTON1) {
            Point2D.Float new_position = controllingCamera.pick(em.getX(), em.getY());
            controlledCamera.setPosition(new_position);
        } else  if (em.getButton() == MouseEvent.BUTTON2) {
            controlledCamera.fitAll();
        }
        updateFrame();
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            beginMove(e);
        }
    }

    private void beginMove(MouseEvent e) {
        translating = true;
        mouseXInitial = e.getX();
        mouseYInitial = e.getY();
        cameraXInitial = controlledCamera.getPosition().x;
        cameraYInitial = controlledCamera.getPosition().y;
    }

    public void mouseReleased(MouseEvent e) {
        translating = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void mouseMoving(MouseEvent e) {
        if (translating) {
            Point2D.Float delta = controllingCamera.dist(e.getX() - mouseXInitial, e.getY() - mouseYInitial);
            float dx = delta.x;
            float dy = delta.y;
            float x = cameraXInitial + dx;
            float y = cameraYInitial - dy;
            controlledCamera.setPosition(x, y);
            if(!limitBound()) {
                beginMove(e);
            }

            updateFrame();
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        
        float oldZoom = controlledCamera.getZoom();

        if (e.getWheelRotation() == -1) {
            controlledCamera.setZoom(controlledCamera.getZoom() / 1.08F);
            if(!verifyZoomIn()) {
                controlledCamera.setZoom(oldZoom);
                return;
            }
        } else {
            controlledCamera.setZoom(controlledCamera.getZoom() * 1.08F);
            if(!verifyZoomOut()) {
                controlledCamera.setZoom(oldZoom);
                return;
            }
        }
       

        updateFrame();
        
    }

    boolean limitBound(){
        boolean result = true;
        Point2D.Float position = controlledCamera.getPosition();

        V3DBoundingBox box =  controlledCamera.getScene().getRootElement().getBoundingBox();

        float left = box.getPosition().x - box.getSize().x/2;
        float right = box.getPosition().x + box.getSize().x/2 ;
        float top = box.getPosition().y - box.getSize().y/2;
        float bottom = box.getPosition().y + box.getSize().y/2;


        if(position.x < left) {
            controlledCamera.setPosition(left, controlledCamera.getPosition().y);
            result = false;
        }

        if(position.x > right) {
            controlledCamera.setPosition(right, controlledCamera.getPosition().y);
            result = false;
        }

        if(position.y < top) {
            controlledCamera.setPosition(controlledCamera.getPosition().x, top);
            result = false;
        }

        if(position.y > bottom) {
            controlledCamera.setPosition(controlledCamera.getPosition().x, bottom);
            result = false;
        }
        return result;
    }

    boolean verifyZoomOut() {
        boolean result = true;

        if(zoomMinFactor != -1) {

            Point2D.Float cameraSize = controlledCamera.getSize();
            V3DBoundingBox box =  controlledCamera.getScene().getRootElement().getBoundingBox();

            if(box.getSize().x < cameraSize.x*zoomMinFactor && box.getSize().y < cameraSize.y*zoomMinFactor) {
                result = false;
            }

        }
        return result;
    }

    boolean verifyZoomIn() {
        boolean result = true;

        if(zoomMaxFactor != -1) {

        Point2D.Float cameraSize = controlledCamera.getSize();
        V3DBoundingBox box =  controlledCamera.getScene().getRootElement().getBoundingBox();

        if(box.getSize().x > cameraSize.x*zoomMaxFactor && box.getSize().y > cameraSize.y*zoomMaxFactor) {
            result = false;
        }

        }


        return result;
    }

    public float getZoomMaxFactor() {
        return zoomMaxFactor;
    }

    public void setZoomMaxFactor(float zoomMaxFactor) {
        this.zoomMaxFactor = zoomMaxFactor;
    }

    public float getZoomMinFactor() {
        return zoomMinFactor;
    }

    public void setZoomMinFactor(float zoomMinFactor) {
        this.zoomMinFactor = zoomMinFactor;
    }

    private void updateFrame() {
        
        Point2D.Float controlledCamerasize = controlledCamera.getSize();
        Point2D.Float controllingCamerasize = controllingCamera.getSize();
        Point2D.Float controlledCameraPosition = controlledCamera.getPosition();
        Point2D.Float controllingCameraPosition = controllingCamera.getPosition();

        frame.setSize(controlledCamerasize.x, controlledCamerasize.y);
        frame.setPosition(controlledCameraPosition.x, controlledCameraPosition.y , frame.getPosition().z);

        
        float topMargin = (controllingCamerasize.y - controlledCamerasize.y)/2 - (controlledCameraPosition.y - controllingCameraPosition.y);
        float bottomMargin = (controllingCamerasize.y - controlledCamerasize.y)/2 + (controlledCameraPosition.y - controllingCameraPosition.y);

        float rightMargin = (controllingCamerasize.x - controlledCamerasize.x)/2 - (controlledCameraPosition.x - controllingCameraPosition.x);
        float leftMargin = (controllingCamerasize.x - controlledCamerasize.x)/2 + (controlledCameraPosition.x - controllingCameraPosition.x);


        frameTop.setSize(controllingCamerasize.x, topMargin);
        frameTop.setPosition(0, controllingCamerasize.y/2 - frameTop.getSize().y/2 , frame.getPosition().z);


        frameBottom.setSize(controllingCamerasize.x, bottomMargin);
        frameBottom.setPosition(0, -controllingCamerasize.y/2 + frameBottom.getSize().y/2 , frame.getPosition().z);

        frameLeft.setSize(leftMargin, controlledCamerasize.y);
        frameLeft.setPosition(- controllingCamerasize.x/2 + frameLeft.getSize().x/2, controlledCameraPosition.y , frame.getPosition().z);

        frameRight.setSize(rightMargin, controlledCamerasize.y);
        frameRight.setPosition( controllingCamerasize.x/2 - frameRight.getSize().x/2, controlledCameraPosition.y , frame.getPosition().z);




        
    }

    @Override
    public void notifyRemove() {
    }
}
