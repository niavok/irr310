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

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;

/**
 *
 * @author fberto
 */
public class V3DSimple3DRotateOnlyCameraController implements V3DCameraController {

    private V3DSimple3DCamera camera;
    private float cameraThetaInitial = 0;
    private float cameraPhiInitial = 0;
    private float mouseXInitial = 0;
    private float mouseYInitial = 0;
    private float ktheta = 0.4f;
    private float kphi = 0.4f;
    private boolean rotating = false;
    private int rotationButton = MouseEvent.BUTTON3;

    private enum MovementType {

        TRANSLATE,
        ROTATE,
    }

    public V3DSimple3DRotateOnlyCameraController(V3DSimple3DCamera camera) {
        this.camera = camera;

        camera.addController(this);

    }

    public void setRotationButton(int rotationButton) {
        this.rotationButton = rotationButton;
    }

    @Override
    public void onEvent(InputEvent e) {
        if(e.isConsumed()) {
            return;
        }

        if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;
            
            switch(em.getID()) {
                case MouseEvent.MOUSE_DRAGGED:
                {
                    mouseDragged(em);
                }break;
                case MouseEvent.MOUSE_MOVED:
                {
                    mouseMoved(em);
                }break;
                case MouseEvent.MOUSE_CLICKED:
                {
                    mouseDragged(em);
                }break;
                case MouseEvent.MOUSE_ENTERED:
                {
                    mouseEntered(em);
                }break;
                case MouseEvent.MOUSE_EXITED:
                {
                    mouseExited(em);
                }break;
                case MouseEvent.MOUSE_PRESSED:
                {
                    mousePressed(em);
                }break;
                case MouseEvent.MOUSE_RELEASED:
                {
                    mouseReleased(em);
                }break;
            }
            
        }

        if (e instanceof MouseWheelEvent) {
            //mouseWheelMoved((MouseWheelEvent) e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoving(e);
    }

    public void mouseMoved(MouseEvent e) {
        mouseMoving(e);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        if (e.getButton() == rotationButton) {
            beginMove(MovementType.ROTATE, e);
        }

    }

    private void beginMove(MovementType type, MouseEvent e) {
        if (type == MovementType.ROTATE) {
            rotating = true;
        }


        mouseXInitial = e.getX();
        mouseYInitial = e.getY();
        cameraThetaInitial = camera.getRotation().z;
        cameraPhiInitial = camera.getRotation().x;
    }

    public void mouseReleased(MouseEvent e) {
        rotating = false;
    }

    public void mouseEntered(MouseEvent e) {
        /*if(e.)

        translating = false;
        rotating = false;*/
    }

    public void mouseExited(MouseEvent e) {
        /*translating = false;
        rotating = false;*/
    }

    private void mouseMoving(MouseEvent e) {

        if (rotating) {
            float theta = cameraThetaInitial + ((float) e.getX() - mouseXInitial) * ktheta;

            float phi = cameraPhiInitial + ((float) e.getY() - mouseYInitial) * kphi;
            if (phi < 0) {
                phi = 0;
                mouseYInitial = (float) e.getY();
                cameraPhiInitial = 0;
            }

            if (phi >= 90) {
                phi = 90;
                mouseYInitial = (float) e.getY();
                cameraPhiInitial = 90;
            }
            camera.setRotation(phi, 0, theta);
            camera.fitAll();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ADD) {
            camera.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z + 3);
        }
        if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
            camera.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z - 3);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {


        float distance = camera.getDistance();

        if (e.getWheelRotation() == -1) {
            camera.setDistance(distance / 1.08f);
        } else {
            camera.setDistance(distance * 1.08f);
        }


    }

    @Override
    public void notifyRemove() {
    }
}
