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

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import fr.def.iss.vd2.lib_v3d.V3DInputEvent;

/**
 *
 * @author fberto
 */
public class V3DAuto3DCameraController implements V3DCameraController {

    private V3DSimple3DCamera camera;
    private float cameraThetaInitial = 0;
    private float cameraPhiInitial = 0;
    private float mouseXInitial = 0;
    private float mouseYInitial = 0;
    private float ktheta = -0.4f;
    private float kphi = 0.4f;
    private float phiMax = 70f;
    private float phiMin = 20f;

    public float getMaxAngle() {
        return phiMax;
    }

    public void setMaxAngle(float maxAngle) {
        this.phiMax = maxAngle;
    }

    public float getMinAngle() {
        return phiMin;
    }

    public void setMinAngle(float minAngle) {
        this.phiMin = minAngle;
    }
    private boolean rotating = false;

    
    public V3DAuto3DCameraController(V3DSimple3DCamera camera) {
        this.camera = camera;

        camera.addController(this);
        
    }

    @Override
    public void onEvent(V3DInputEvent e) {
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
            mouseWheelMoved((MouseWheelEvent) e);
        }*/ 
        //TODO: repair
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

        if (e.getButton() == MouseEvent.BUTTON1) {
            beginMove(e);
        }
    }

    private void beginMove(MouseEvent e) {
        rotating = true;
        
        mouseXInitial = e.getX();
        mouseYInitial = e.getY();
        cameraThetaInitial = camera.getRotation().z;
        cameraPhiInitial = camera.getRotation().x;
    }

    public void mouseReleased(MouseEvent e) {
        rotating = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {        
    }

    private void mouseMoving(MouseEvent e) {

        if (rotating) {
            float theta = cameraThetaInitial + ((float) e.getX() - mouseXInitial) * ktheta;

            float phi = cameraPhiInitial + ((float) e.getY() - mouseYInitial) * kphi;
            if (phi < phiMin) {
                phi = phiMin;
                mouseYInitial = (float) e.getY();
                cameraPhiInitial = phiMin;
            }

            if (phi >= phiMax) {
                phi = phiMax;
                mouseYInitial = (float) e.getY();
                cameraPhiInitial = phiMax;
            }
           
            camera.setRotation(phi, 0, theta);
            camera.fitAll();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
      
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    @Override
    public void notifyRemove() {
    }
}
