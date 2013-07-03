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

package com.irr310.i3d.scene;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.irr310.i3d.I3dContext;
import com.irr310.i3d.scene.I3dSceneManager.FloatValuedElement;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DInputEvent;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;

/**
 *
 * @author fberto
 */
public abstract class I3dCamera {

    protected I3dScene currentScene;
    protected I3dScene hudScene;
    protected I3dScene backgroundScene;
    // 1000 object max
    //private IntBuffer buff = BufferUtils.newIntBuffer(4000);
    
    private V3DColor backgroundColor = new V3DColor(0, 0, 0, 1);
    protected GLU glu = new GLU();
    
    private List<V3DCameraController> controllerList = new CopyOnWriteArrayList<V3DCameraController>();
    protected float currentHeight;
    protected float currentWidth;
    private boolean enabled;
    private boolean configured = false;
    private Runnable cameraInitialisation;

    public I3dCamera() {
        hudScene = new I3dScene();
        backgroundScene = new I3dScene();
    }

    public void display( float width, float height) {

        this.currentWidth = width;
        this.currentHeight = height;

        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, 0, height, -2000.0, 2000.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        
        backgroundScene.display( this);
        

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        initPerspective();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glPushMatrix();

        configureView( glu);

        if(!configured) {
            configured = true;
            if(cameraInitialisation != null) {
                cameraInitialisation.run();
                cameraInitialisation = null;
            }
        }


        preDisplayScene();
        if(currentScene != null) {
            currentScene.display( this);
        }
        postDisplayScene();

        preDisplayGui();
        postDisplayGui();


        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, 0, height, -2000.0, 2000.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        hudScene.display( this);

        GL11.glPopMatrix();




    }

    public void setCameraInitialisation(Runnable cameraInitialisation) {
        this.cameraInitialisation = cameraInitialisation;
    }

    public boolean isConfigured() {
        return configured;
    }
    

    public void select( int x, int y) {
        //TODO repair
        /*int hits;
        int id;

        int viewport[] = new int[4];

        // TODO set object count add buffer size
        GL11.glSelectBuffer(buff.capacity(), buff);

        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);

        GL11.glRenderMode(GL11.GL_SELECT);

        GL11.glInitNames();

        GL11.glPushName(0);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        {
            glu.gluPickMatrix((double) (x),
                    (double) (y), //
                    5.0, 5.0, viewport, 0);

            initPerspective();

            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            configureView( glu);

            if(currentScene != null) {
                currentScene.select( this);
            }
    
            GL11.glMatrixMode(GL11.GL_PROJECTION);

        }
        GL11.glPopMatrix();

        hits = GL11.glRenderMode(GL11.GL_RENDER);

        processHits(hits, buff);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
*/
    }

    public void setScene(I3dScene scene) {
        currentScene = scene;
    }

    public I3dScene getScene() {
        return currentScene;
    }

    public V3DColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(V3DColor color) {
        backgroundColor = color;
    }

    private void processHits(int hits, IntBuffer buffer) {
        int names, ptr = 0;

        if(hits == -1) {
            hits = buffer.capacity()/4;
        }
        
        FloatValuedElement[] hitsElements = new FloatValuedElement[hits];
        
        for (int i = 0; i < hits; i++) { // for each hit

            I3dElement selectedElement = null;

            names = buffer.get(ptr++);

            ptr++;
            float depth = Float.intBitsToFloat(buffer.get(ptr)); //Min
            ptr++;
            
            for (int j = 0; j < names; j++) { // for each name
                if (j == 0) {
                    selectedElement = I3dContext.getInstance().getSceneManager().getIdAllocator().getElement(buffer.get(ptr));
                }
                ptr++;
            }

            hitsElements[i] = new FloatValuedElement(selectedElement, depth);

        }

        I3dContext.getInstance().getSceneManager().setMouseOverlapList(hitsElements);
    }
    
    protected void preDisplayScene() {
    }

    protected void postDisplayScene() {
    }

    protected void preDisplayGui() {
    }

    protected void postDisplayGui() {
    }

    public abstract float getRelativeSize();

    protected abstract void configureView( GLU glu);

    public abstract void enableRotation();

    public abstract void disableRotation();

    public abstract void center(V3DVect3 position);

    

    public void onEvent(V3DInputEvent e) {
        for (V3DCameraController listener : controllerList) {
            listener.onEvent(e);
        }
    }

    

    public void addController(V3DCameraController controller) {
        controllerList.add(controller);
    }

    public void addControllerBefore(V3DCameraController controller) {
        controllerList.add(0, controller);
    }

    public void removeController(V3DCameraController controller) {
        controller.notifyRemove();
        controllerList.remove(controller);
    }

    protected abstract void initPerspective();

    public void setSize(int width, int height) {
        this.currentWidth = width;
        this.currentHeight = height;
    }

    abstract public void fitAll();

    public I3dScene getHudScene() {
        return hudScene;
    }

    public void setHudScene(I3dScene hudScene) {
        this.hudScene = hudScene;
    }
    
    public I3dScene getBackgroundScene() {
        return backgroundScene;
    }
    
    public void setBackgroundScene(I3dScene backgroundScene) {
        this.backgroundScene = backgroundScene;
    }

    abstract public void fitAllIfInvalid();

    public void setEnabled(boolean b) {
        enabled = b;
        fireEnableChanged();
    }

    public boolean isEnabled() {
        return enabled;
    }

    


     //Listeners

    List<CameraChangeListener> cameraChangeListeners = new ArrayList<CameraChangeListener>();

    public void addCameraChangeListener(CameraChangeListener listener){
        cameraChangeListeners.add(listener);
    }

    public void removeCameraChangeListener(CameraChangeListener listener){
        cameraChangeListeners.remove(listener);
    }

    public void fireEnableChanged() {
        for(CameraChangeListener listener : cameraChangeListeners) {
            listener.enableChanged();
        }
    }

    public interface CameraChangeListener {
        public void enableChanged();
    }

    
    public float getCurrentHeight() {
        return currentHeight;
    }
    public float getCurrentWidth() {
        return currentWidth;
    }

    public abstract V3DVect3 getRotation();
    

    
}
