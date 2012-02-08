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
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DBoundingBox;

/**
 *
 * @author fberto
 */
public class V3DSimple2DCameraController implements V3DCameraController {

    private V3DSimple2DCamera camera;
    private float cameraXInitial = 0;
    private float cameraYInitial = 0;
    private int mouseXInitial = 0;
    private int mouseYInitial = 0;
    private boolean translating = false;
    private long lastClickTime;
    private float zoomMaxFactor = -1;
    private float zoomMinFactor = 0.5f;
    private boolean limitBound = true;
    private int fitAllButton = MouseEvent.BUTTON2;
    private int translationButton = MouseEvent.BUTTON1;
    private int rotationButton = MouseEvent.BUTTON3;
    private int instantMoveButton = MouseEvent.BUTTON2;

    private enum MovementType {

        TRANSLATE,
        ROTATE,
    }

    public V3DSimple2DCameraController(V3DSimple2DCamera camera) {
        this.camera = camera;

    }

    public void setInstantMoveButton(int instantMoveButton) {
        this.instantMoveButton = instantMoveButton;
    }

    

    public void setFitAllButton(int fitAllButton) {
        this.fitAllButton = fitAllButton;
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
        /*if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;

            switch (em.getID()) {
                case MouseEvent.MOUSE_DRAGGED: {
                    mouseDragged(em);
                }
                break;
                case MouseEvent.MOUSE_MOVED: {
                    mouseMoved(em);
                }
                break;
                case MouseEvent.MOUSE_CLICKED: {
                    mouseClicked(em);
                }
                break;
                case MouseEvent.MOUSE_ENTERED: {
                    mouseEntered(em);
                }
                break;
                case MouseEvent.MOUSE_EXITED: {
                    mouseExited(em);
                }
                break;
                case MouseEvent.MOUSE_PRESSED: {
                    mousePressed(em);
                }
                break;
                case MouseEvent.MOUSE_RELEASED: {
                    mouseReleased(em);
                }
                break;
            }

        }

        if (e instanceof MouseWheelEvent) {
            MouseWheelEvent em = (MouseWheelEvent) e;
            mouseWheelMoved(em);
        }

        if (e instanceof KeyEvent) {
            KeyEvent em = (KeyEvent) e;

            switch (em.getID()) {
                case KeyEvent.KEY_TYPED: {
                    keyTyped(em);
                }
                break;
                case KeyEvent.KEY_PRESSED: {
                    keyPressed(em);
                }
                break;
                case KeyEvent.KEY_RELEASED: {
                    keyReleased(em);
                }
                break;

            }

        }*/


    }

    public void mouseDragged(MouseEvent e) {
        mouseMoving(e);
    }

    public void mouseMoved(MouseEvent e) {
        //mouseMoving(e);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == fitAllButton) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 500) {
                camera.fitAll();
            }
            lastClickTime = currentTime;
        }

        if (e.getButton() == instantMoveButton) {

            Point2D.Float position = camera.pick(e.getX(), e.getY());
            camera.setPosition(position);
            limitBound();
        }

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
        } else if (type == MovementType.ROTATE) {
            translating = false;
        }

        
        mouseXInitial = e.getX();
        mouseYInitial = e.getY();
        cameraXInitial = camera.getPosition().x;
        cameraYInitial = camera.getPosition().y;
        
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

            Point2D.Float delta = camera.dist(
                    e.getX() - mouseXInitial,
                    e.getY() - mouseYInitial);

            float dx = -delta.x;
            float dy = delta.y;



            float theta = (float) Math.toRadians(camera.getRotation().x);

            float x = cameraXInitial + (float) Math.cos(theta) * dx + (float) Math.sin(theta) * dy;
            float y = cameraYInitial - (float) Math.sin(theta) * dx + (float) Math.cos(theta) * dy;


            camera.setPosition(x, y);
            if(limitBound) {
                if (!limitBound()) {
                    beginMove(MovementType.TRANSLATE, e);
                }
            }
        }

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ADD) {
            zoom(-1);
        }
        if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
            zoom(1);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {

        Point2D.Float before = camera.pick(e.getX(), e.getY());

        float oldZoom = camera.getZoom();

        if (e.getWheelRotation() == -1) {
            camera.setZoom(camera.getZoom() / 1.08f);
            if (limitBound && !verifyZoomIn()) {
                camera.setZoom(oldZoom);
                return;
            }

        } else {
            camera.setZoom(camera.getZoom() * 1.08f);
            if (limitBound && !verifyZoomOut()) {
                camera.setZoom(oldZoom);
                return;
            }
        }



        Point2D.Float after = camera.pick(e.getX(), e.getY());

        Point2D.Float delta = new Point2D.Float(before.x - after.x, -(after.y - before.y));


        camera.move(delta);
        if(limitBound) {
            limitBound();
        }


    }

    private void zoom(int direction) {
        
        float oldZoom = camera.getZoom();

        if (direction == -1) {
            camera.setZoom(camera.getZoom() / 1.08f);
            if (limitBound && !verifyZoomIn()) {
                camera.setZoom(oldZoom);
                return;
            }

        } else {
            camera.setZoom(camera.getZoom() * 1.08f);
            if (limitBound && !verifyZoomOut()) {
                camera.setZoom(oldZoom);
                return;
            }
        }

        if(limitBound) {
            limitBound();
        }
    }

    boolean limitBound() {
        boolean result = true;
        Point2D.Float position = camera.getPosition();

        V3DBoundingBox box = camera.getScene().getRootElement().getBoundingBox();
        float left = box.getPosition().x - box.getSize().x / 2;
        float right = box.getPosition().x + box.getSize().x / 2;
        float top = box.getPosition().y - box.getSize().y / 2;
        float bottom = box.getPosition().y + box.getSize().y / 2;

        if (position.x < left) {
            camera.setPosition(left, camera.getPosition().y);
            result = false;
        }

        if (position.x > right) {
            camera.setPosition(right, camera.getPosition().y);
            result = false;
        }

        if (position.y < top) {
            camera.setPosition(camera.getPosition().x, top);
            result = false;
        }

        if (position.y > bottom) {
            camera.setPosition(camera.getPosition().x, bottom);
            result = false;
        }
        return result;
    }

    boolean verifyZoomOut() {
        boolean result = true;

        if (zoomMinFactor != -1) {

            Point2D.Float cameraSize = camera.getSize();
            V3DBoundingBox box = camera.getScene().getRootElement().getBoundingBox();

            if (box.getSize().x < cameraSize.x * zoomMinFactor && box.getSize().y < cameraSize.y * zoomMinFactor) {
                result = false;
            }

        }
        return result;
    }

    boolean verifyZoomIn() {
        boolean result = true;

        if (zoomMaxFactor != -1) {

            Point2D.Float cameraSize = camera.getSize();
            V3DBoundingBox box = camera.getScene().getRootElement().getBoundingBox();

            if (box.getSize().x > cameraSize.x * zoomMaxFactor && box.getSize().y > cameraSize.y * zoomMaxFactor) {
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

    public boolean isLimitBound() {
        return limitBound;
    }

    public void setLimitBound(boolean limitBound) {
        this.limitBound = limitBound;
    }


    @Override
    public void notifyRemove() {
    }

}
