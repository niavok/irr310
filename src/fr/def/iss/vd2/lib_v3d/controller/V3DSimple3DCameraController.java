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

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;

/**
 *
 * @author fberto
 */
public class V3DSimple3DCameraController implements V3DCameraController {

    V3DSimple3DCamera camera;
    float cameraXInitial = 0;
    float cameraYInitial = 0;
    float cameraThetaInitial = 0;
    float cameraPhiInitial = 0;
    float mouseXInitial = 0;
    float mouseYInitial = 0;
    float kx = -0.00060f;
    float ky = -0.00060f;
    float ktheta = 0.4f;
    float kphi = 0.4f;
    boolean translating = false;
    boolean rotating = false;
    private Point2D.Float mouseInitialPick;
    private int translationButton = MouseEvent.BUTTON1;
    private int rotationButton = MouseEvent.BUTTON3;

    private enum MovementType {

        TRANSLATE,
        ROTATE,
    }

    public V3DSimple3DCameraController(V3DSimple3DCamera camera) {
        this.camera = camera;

        camera.addController(this);

    }

    public void setRotationButton(int rotationButton) {
        this.rotationButton = rotationButton;
    }

    public void setTranslationButton(int translationButton) {
        this.translationButton = translationButton;
    }

    @Override
    public void onEvent(V3DInputEvent e) {
        if(e.isConsumed()) {
            return;
        }

        /*if (e instanceof MouseEvent) {
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
                    mouseClicked(em);
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
            mouseWheelMoved((MouseWheelEvent) e);
        }*/
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoving(e);
    }

    public void mouseMoved(MouseEvent e) {
        //mouseMoving(e);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        if (e.getButton() == translationButton) {
            beginMove(MovementType.TRANSLATE, e);
        } else if (e.getButton() == rotationButton) {
            beginMove(MovementType.ROTATE, e);
        }

    }

    private void beginMove(MovementType type, MouseEvent e) {
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


        cameraXInitial = camera.getPosition().x;
        cameraYInitial = camera.getPosition().y;
        cameraThetaInitial = camera.getRotation().z;
        cameraPhiInitial = camera.getRotation().x;
    }

    public void mouseReleased(MouseEvent e) {
        translating = false;
        rotating = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void mouseMoving(MouseEvent e) {



        if (translating) {

            Point2D.Float mousePick = camera.pick(e.getX(), e.getY(), 0);

            float x = cameraXInitial - mousePick.x + mouseInitialPick.x;
            float y = cameraYInitial - mousePick.y + mouseInitialPick.y;





            //mouseInitialPick = mousePick;

            camera.setPosition(x, y, camera.getPosition().z);

            cameraXInitial = x;
            cameraYInitial = y;
        }
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
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ADD) {
            float distance = camera.getDistance();
            camera.setDistance(distance / 1.08f);
        }
        if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
            float distance = camera.getDistance();
            camera.setDistance(distance * 1.08f);
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
