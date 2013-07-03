package com.irr310.i3d.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.i3d.I3dIdAllocator;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DShader;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;


public class I3dSceneManager {
    private final ElementComparator elementComparator = new ElementComparator();
    private I3dIdAllocator idAllocator = new I3dIdAllocator();
    private List<FloatValuedElement> mouseOverlapDepthList = new ArrayList<FloatValuedElement>();
    private List<I3dElement> mouseOverlapList = new ArrayList<I3dElement>();
    private List<I3dMouseOverlapListener> mouseOverLapListenerList = new ArrayList<I3dMouseOverlapListener>();
    private Map<String, V3DShader> shaderMap = new HashMap<String, V3DShader>();
    private List<V3DShader> uniqueShaderList = new ArrayList<V3DShader>();
    private V3DCameraBinding mouseOverCameraBinding = null;
    
    /**
     * Internal public method Used by V3DElement to generate there id
     * 
     * @return id allocator
     */
    public I3dIdAllocator getIdAllocator() {
        return idAllocator;
    }
    
    /**
     * Internal public method Used by the cameras to update the list of
     * overlapped elements
     * 
     * @param hitsElements list of elements objects and their depth
     */
    public void setMouseOverlapList(FloatValuedElement[] hitsElements) {
        List<FloatValuedElement> newElementsList = Arrays.asList(hitsElements);
        List<FloatValuedElement> oldElementsList = mouseOverlapDepthList;
        if (!(oldElementsList.containsAll(newElementsList) && newElementsList.containsAll(oldElementsList))) {

            Collections.sort(newElementsList, elementComparator);

            mouseOverlapDepthList = newElementsList;
            mouseOverlapList = new ArrayList<I3dElement>(mouseOverlapDepthList.size());

            for (FloatValuedElement elem : mouseOverlapDepthList) {
                if (elem.element != null) {
                    mouseOverlapList.add(elem.element);
                }
            }
            fireMouseOverlapChanged();
        }

    }
    
    /**
     * Internal public method Notify all mouse overlap listener that there is a
     * change
     */
    private void fireMouseOverlapChanged() {
        for (I3dMouseOverlapListener listener : mouseOverLapListenerList) {
            listener.selectionChange();
        }
    }
    
    /**
     * Return the element currently at the front under the mouse. If no element
     * is under the mouse, return null.
     * 
     * @return front element or null
     */
    public I3dElement getMouseOverlapTop() {

        if (mouseOverlapList.size() > 0) {
            return mouseOverlapList.get(mouseOverlapList.size() - 1);
        } else {
            return null;
        }
    }
    
    /**
     * Return the element currently at the front under the mouse which is in the
     * whitelist. If none of overlapped elements are in the whitelist, return
     * null. If no element is under the mouse, return null.
     * 
     * @param whiteList
     * @return front element or null
     */
    public I3dElement getMouseOverlapTop(List<I3dElement> whiteList) {
        if (mouseOverlapList.size() > 0) {
            for (int i = mouseOverlapList.size() - 1; i >= 0; i--) {
                if (whiteList.contains(mouseOverlapList.get(i))) {
                    return mouseOverlapList.get(i);
                }
            }
            return null;
        } else {
            return null;
        }
    }
    
    /**
     * Comparator used to sort overlapped elements by depth
     */
    static private class ElementComparator implements Comparator<FloatValuedElement> {

        @Override
        public int compare(FloatValuedElement o1, FloatValuedElement o2) {
            if (o1.value < o2.value) {
                return -1;
            } else if (o1.value > o2.value) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    /**
     * Tuple associating a V3DElement with his depth
     */
    public static class FloatValuedElement {

        public I3dElement element;
        public float value;

        public FloatValuedElement(I3dElement element, float value) {
            this.element = element;
            this.value = value;
        }
    }
    
    public V3DShader createUniqueShader(V3DShader v3dShader) {
        uniqueShaderList.add(v3dShader);
        return v3dShader;
    }
    
    public V3DShader getShader(String shaderName) {
        if (shaderMap.containsKey(shaderName)) {
            return shaderMap.get(shaderName);
        } else {

            V3DShader shader = new V3DShader(shaderName) {
                /*
                 * private int lightDir;
                 * @Override protected void loadUniforms() { lightDir =
                 * ARBShaderObjects.glGetUniformLocationARB(shader, "lightDir");
                 * }
                 * @Override protected void setUniforms(V3DCamera camera) {
                 * ARBShaderObjects.glUniform3fARB(lightDir, 1f, 0f, 0f); }
                 */

            };

            shaderMap.put(shaderName, shader);

            return shader;
        }

    }
    
    /***
     * Remove the listener from the listener list
     * 
     * @param listener
     */
    public void removeMouseOverlapListener(I3dMouseOverlapListener listener) {
        mouseOverLapListenerList.remove(listener);
    }
    
    /**
     * Add a mouse overlap listener. The "selectionChange" method of the
     * listenner will be called as soon as the list of overlaped V3DElement
     * change.
     * 
     * @param listener
     */
    public void addMouseOverlapListener(I3dMouseOverlapListener listener) {
        mouseOverLapListenerList.add(listener);
    }
    
    /**
     * Get the top binding currently under the mouse
     * 
     * @return top binding
     */
    public V3DCameraBinding getMouseOverCameraBinding() {
        return mouseOverCameraBinding;
    }
}
