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
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DRectangle;

public class V3DFitZoomController implements V3DCameraController {

    int mouseXInitial = 0;
    int mouseYInitial = 0;
    V3DSimple2DCamera targetCamera;
    V3DSimple2DCamera inputCamera;

    boolean selecting = false;
    private V3DRectangle targetFrame;
    private V3DRectangle inputFrame;
    private V3DColorElement targetFrameColor;
    private V3DColorElement inputFrameColor;
    private Float beginPosition;
    private int fitZoomButton = MouseEvent.BUTTON3;

    public V3DFitZoomController(V3DSimple2DCamera inputCamera, V3DSimple2DCamera targetCamera) {
        this.targetCamera = targetCamera;
        this.inputCamera = inputCamera;
    }

    public int getFitZoomButton() {
        return fitZoomButton;
    }

    public void setZoomButton(int zoomButton) {
        this.fitZoomButton = zoomButton;
    }

    

    public void onEvent(V3DInputEvent e) {
        if(inputCamera == null || targetCamera == null) {
            return;
        }

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
        }*/
      
      
    }
  
    private void mouseDragged(MouseEvent e) {
        mouseMoving(e);
    }

    public void mouseMoved(MouseEvent e) {
        mouseMoving(e);
    }

   

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == fitZoomButton) {
            
            beginSelect(e);
        }
    }

    private void beginSelect(MouseEvent e) {

        selecting = true;
        mouseXInitial = e.getX();
        mouseYInitial = e.getY();

        targetFrame = new V3DRectangle();
        targetFrame.setThickness(1.0f);
        targetFrame.setSize(0, 0);
        beginPosition = inputCamera.pick(mouseXInitial, mouseYInitial);

        targetFrame.setPosition(beginPosition.x, beginPosition.y, 100);

        targetFrameColor = new V3DColorElement(targetFrame, V3DColor.blue);

        targetCamera.getScene().add(targetFrameColor);


        inputFrame = new V3DRectangle();
        inputFrame.setThickness(1.0f);
        inputFrame.setSize(0, 0);
        inputFrame.setPosition(beginPosition.x, beginPosition.y, 100);
        
        inputFrameColor = new V3DColorElement(inputFrame, V3DColor.blue);

        inputCamera.getScene().add(inputFrameColor);

    }

    public void mouseReleased(MouseEvent e) {
        if(selecting) {
            selecting = false;
            targetCamera.getScene().remove(targetFrameColor);
            inputCamera.getScene().remove(inputFrameColor);

            if(Math.abs(e.getX() - mouseXInitial)+ Math.abs(e.getY() - mouseYInitial) > 20) {

                Point2D.Float begin_position = inputCamera.pick(mouseXInitial, mouseYInitial);
                Point2D.Float dist = inputCamera.dist(e.getX() - mouseXInitial, e.getY() - mouseYInitial);

                Point2D.Float size = new Point2D.Float(Math.abs(dist.x),Math.abs(dist.y));
                Point2D.Float position = new Point2D.Float(begin_position.x,begin_position.y);
                if(dist.x < 0) {
                    position.x = position.x-size.x;
                }

                if(dist.y > 0) {
                    position.y = position.y-size.y;
                }

                targetCamera.fit(new Point2D.Float(position.x , position.y ) , size);
            }
        }
    }

 

    private void mouseMoving(MouseEvent e) {
        if (selecting) {
            Point2D.Float size = inputCamera.dist(e.getX() - mouseXInitial, e.getY() - mouseYInitial);
            
            if(size.x ==0 && size.y == 0) {
                return;
            }

            targetFrame.setSize(size.x, -size.y);
            targetFrame.setPosition(beginPosition.x+ size.x/2, beginPosition.y- size.y/2, 100);
            inputFrame.setSize(size.x, -size.y);
            inputFrame.setPosition(beginPosition.x+ size.x/2, beginPosition.y- size.y/2, 100);
            

            e.consume();
        }
    }

    void setInput(V3DSimple2DCamera input) {
        inputCamera = input;
    }

    void setTarget(V3DSimple2DCamera target) {
        targetCamera = target;
    }

    @Override
    public void notifyRemove() {
    }
}
