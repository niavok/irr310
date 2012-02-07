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
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.controller.listener.V3DInsertListener;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;

/**
 * This controlleur destroy itself after the element's insertion
 * @author fberto
 */
public class V3DInsertion2DController implements V3DCameraController {

    V3DSimple2DCamera camera;
    boolean moving = false;
    private V3DElement elementToInsert;
    private V3DInsertListener listener;

    public V3DInsertion2DController(V3DSimple2DCamera camera) {
        this.camera = camera;

    }

    public void setElementToInsert(V3DElement elementToInsert) {

        if (this.elementToInsert != null) {
            camera.getScene().remove(elementToInsert);
        }
        this.elementToInsert = elementToInsert;

        if (this.elementToInsert != null) {
            elementToInsert.setShowMode(false, false);
            camera.getScene().add(elementToInsert);
        }
    }

    public void setInsertListener(V3DInsertListener listener) {
        this.listener = listener;
    }

    @Override
    public void onEvent(V3DInputEvent e) {

        if(e.isConsumed()) {
            return;
        }

        /*if (e instanceof V3DMouseEvent) {
            V3DMouseEvent em = (V3DMouseEvent) e;

            if (em.getID() == MouseEvent.MOUSE_DRAGGED || em.getID() == MouseEvent.MOUSE_MOVED) {
                mouseMoving(em);
            }

            if (em.getID() == MouseEvent.MOUSE_CLICKED && em.getButton() == MouseEvent.BUTTON1 && em.getClickCount() == 1) {
                if (elementToInsert != null) {
                    e.consume();
                    camera.getScene().remove(elementToInsert);
                    camera.removeController(this);
                    Point2D.Float pos = camera.pick(em.getX(), em.getY());
                    elementToInsert.setPosition(pos.x, pos.y, 1);
                    if (listener != null) {
                        listener.insered(elementToInsert.getPosition());
                    }
                }
            }

        }*/

    }

    private void mouseMoving(MouseEvent e) {

        if (elementToInsert != null) {
            elementToInsert.setShowMode(true, false);
            Point2D.Float pos = camera.pick(e.getX(), e.getY());
            elementToInsert.setPosition(pos.x, pos.y, 1);
            if (listener != null) {
                listener.move(elementToInsert.getPosition());
            }
        }
    }

    public void cancel() {
        camera.getScene().remove(elementToInsert);
        camera.removeController(this);
    }

    public V3DElement getElementToInsert() {
        return elementToInsert;
    }

    @Override
    public void notifyRemove() {
    }
}
