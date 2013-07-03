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

import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DRectangle;

public class V3D3DPickTestController implements V3DCameraController {

      int mouseXInitial = 0;
    int mouseYInitial = 0;
    V3DSimple3DCamera targetCamera;
    V3DSimple3DCamera inputCamera;
    Point2D.Float begin_position;

    boolean selecting = false;
    private V3DRectangle selectFraming;

    public V3D3DPickTestController(V3DSimple3DCamera inputCamera, V3DSimple3DCamera targetCamera) {
        this.targetCamera = targetCamera;
        this.inputCamera = inputCamera;
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
        if (e.getButton() == MouseEvent.BUTTON1) {

            beginSelect(e);
        }
    }

    private void beginSelect(MouseEvent e) {

        selecting = true;
        mouseXInitial = e.getX();
        mouseYInitial = e.getY();

        selectFraming = new V3DRectangle();
        selectFraming.setThickness(1.0f);
        selectFraming.setSize(0, 0);
        begin_position = inputCamera.pick(mouseXInitial, mouseYInitial,0);

        selectFraming.setPosition(begin_position.x, begin_position.y, 0);


        targetCamera.getScene().add(selectFraming);




    }

    public void mouseReleased(MouseEvent e) {
        if(selecting) {
            selecting = false;
            targetCamera.getScene().remove(selectFraming);
        }
    }



    private void mouseMoving(MouseEvent e) {
        if (selecting) {
            Point2D.Float currentPos = inputCamera.pick(e.getX(), e.getY(),0);
            selectFraming.setSize(currentPos.x - begin_position.x, (currentPos.y - begin_position.y));

            e.consume();
        }
    }

    void setInput(V3DSimple3DCamera input) {
        inputCamera = input;
    }

    void setTarget(V3DSimple3DCamera target) {
        targetCamera = target;
    }

    @Override
    public void notifyRemove() {
    }
  
}
