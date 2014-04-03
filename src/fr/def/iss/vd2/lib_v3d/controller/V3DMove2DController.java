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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.irr310.i3d.I3dContext;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.controller.listener.V3DMoveListener;

public class V3DMove2DController implements V3DCameraController {

    Map<I3dElement, I3dElement> moveMap = new HashMap<I3dElement, I3dElement>();
    V3DSimple2DCamera camera;
    Point popUpPosition = new Point(0, 0);
    float elementXInitial = 0;
    float elementYInitial = 0;
    float mouseXInitial = 0;
    float mouseYInitial = 0;
    int lastPressedButton = 0;
    boolean moving = false;
    private I3dElement elementToMove;
    private I3dElement clickedElement;
    private V3DMoveListener listener;
    private boolean enable = true;
    private boolean firstOnMove = false;
    private Point2D.Float lastMousPos;
    private int translationButton = MouseEvent.BUTTON1;

    public V3DMove2DController(V3DSimple2DCamera camera) {
        this.camera = camera;

    }

    public void setTranslationButton(int translationButton) {
        this.translationButton = translationButton;
    }



    public void setMoveMap(Map<I3dElement, I3dElement> moveMap) {
        this.moveMap = moveMap;
    }

    public void setMoveEndedListener(V3DMoveListener listener) {
        this.listener = listener;
    }

    public void setMakeFirstOnMove(boolean first) {
        firstOnMove = first;
    }

    @Override
    public void onEvent(V3DInputEvent e) {
        if (!enable || e.isConsumed()) {
            return;
        }

        if (e instanceof I3dMouseEvent) {
            I3dMouseEvent em = (I3dMouseEvent) e;

            if (em.getAction() == I3dMouseEvent.Action.MOUSE_PRESSED) {
                lastPressedButton = em.getButton();
            }

            if (em.getAction() == I3dMouseEvent.Action.MOUSE_RELEASED) {
                mouseReleased(em);
            }

            if (em.getAction() == I3dMouseEvent.Action.MOUSE_DRAGGED && lastPressedButton == translationButton) {

                beginMove(em);
            }
        }

    }

    private void beginMove(I3dMouseEvent e) {

        I3dElement overElement = I3dContext.getInstance().getSceneManager().getMouseOverlapTop(new ArrayList<I3dElement>(moveMap.keySet()));

        if (moving) {
            if(mouseMoving(e)) {
                e.consume();
            }
        } else if (overElement != null && moveMap.containsKey(overElement)) {
            moving = true;

            clickedElement = overElement;
            elementToMove = moveMap.get(overElement);

            Point2D.Float pos = camera.pick(e.getX(), e.getY());

            mouseXInitial = pos.x;
            mouseYInitial = pos.y;


            elementXInitial = elementToMove.getPosition().x;
            elementYInitial = elementToMove.getPosition().y;

            e.consume();

            if (firstOnMove) {
                camera.removeController(this);
                camera.addControllerBefore(this);
            }
        }
    }

    public void mouseReleased(I3dMouseEvent e) {
        moving = false;
        if (listener != null && clickedElement != null) {
            listener.moveEnded(clickedElement, elementToMove);
            clickedElement = null;
        }

    }

    private boolean mouseMoving(I3dMouseEvent e) {



        if (moving) {


            Point2D.Float pos = camera.pick(e.getX(), e.getY());

            lastMousPos = pos;


            float dx = (pos.x - mouseXInitial);
            float dy = (pos.y - mouseYInitial);

            if(dx == 0 && dy == 0) {
                return false;
            }

            float x = elementXInitial + dx;
            float y = elementYInitial + dy;

            elementToMove.setPosition(x, y, elementToMove.getPosition().z);
            if (listener != null) {
                listener.move(clickedElement, elementToMove);
            }
        }

        return true;

    }

    public void setEnable(boolean b) {
        enable = b;
    }

    public void reload() {
        elementXInitial = elementToMove.getPosition().x;
        elementYInitial = elementToMove.getPosition().y;
        mouseXInitial = lastMousPos.x;
        mouseYInitial = lastMousPos.y;

    }

    @Override
    public void notifyRemove() {
    }
}
