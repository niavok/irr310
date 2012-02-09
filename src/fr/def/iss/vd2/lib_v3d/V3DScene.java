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

package fr.def.iss.vd2.lib_v3d;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;

/**
 * A V3DScene is an agregate of element positionned in a void world.
 * This is the root of the scene graph
 * @author fberto
 */
public class V3DScene {


    private V3DGroupElement rootElement;


    /**
     * Create a scene
     * @param context
     */
    public V3DScene(V3DContext context) {
        rootElement = new V3DGroupElement(context);
    }

    /**
     * Draw the scene.
     * Place 3 lights and draw the root element
     * @param gl context gl to draw the image
     * @param camera point of view of the draw
     */
    public void display(V3DCamera camera) {

        float[] lightAmbient1 = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] lightDiffuse1 = {0.75f, 0.75f, 0.75f, 1.0f};
        float[] lightPosition1 = {100.0f, 100.0f, -100.0f, 0.0f};

        FloatBuffer lightPosition0= BufferUtils.createFloatBuffer(4);
        lightPosition0.put(70.0f) ;
        lightPosition0.put(50.0f) ;
        lightPosition0.put(100.0f) ;
        lightPosition0.put(0.0f) ;
        lightPosition0.flip();
        
        //GL11.GLLightf(GL11.GL_LIGHT1, GL11.GL_AMBIENT, lightAmbient1);   // Setup The Ambient Light
        //GL11.GLLightf(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightDiffuse1);   // Setup The Diffuse Light
        //GL11.GLLightf(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition1, 0); // Position The Light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition0);
        
        GL11.glEnable(GL11.GL_LIGHT1);  // Enable Light One

        float[] lightAmbient2 = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] lightDiffuse2 = {0.3f, 0.3f, 0.3f, 1.0f};
        float[] lightPosition2 = {100.0f, -100.0f, -100.0f, 0.0f};

        //GL11.GLLightfv(GL11.GL_LIGHT2, GL11.GL_AMBIENT, lightAmbient2, 0);   // Setup The Ambient Light
        //GL11.GLLightfv(GL11.GL_LIGHT2, GL11.GL_DIFFUSE, lightDiffuse2, 0);   // Setup The Diffuse Light
        //GL11.GLLightfv(GL11.GL_LIGHT2, GL11.GL_POSITION, lightPosition2, 0); // Position The Light

        //GL11.glEnable(GL11.GL_LIGHT2);  // Enable Light One

        float[] lightAmbient3 = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] lightDiffuse3 = {0.2f, 0.2f, 0.20f, 1.0f};
        float[] lightPosition3 = {-100.0f, -100.0f, -100.0f, 0.0f};

        //GL11.GLLightfv(GL11.GL_LIGHT3, GL11.GL_AMBIENT, lightAmbient3, 0);   // Setup The Ambient Light
        //GL11.GLLightfv(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, lightDiffuse3, 0);   // Setup The Diffuse Light
        //GL11.GLLightfv(GL11.GL_LIGHT3, GL11.GL_POSITION, lightPosition3, 0); // Position The Light

        //GL11.glEnable(GL11.GL_LIGHT3);  // Enable Light One

        //GL11.GLLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);
        

        float[] globalAmbiant = {0.0f, 0.0f, 0.0f, 1.0f};
        //GL11.GLLightModelfv(GL11.GL_LIGHT_MODEL_AMBIENT, globalAmbiant,0);

        rootElement.display(camera);
        //rootElement.select( camera, 0);
    }

    /**
     * Add an element at the root of the scene
     * @param element
     */
    public void add(V3DElement element) {
        rootElement.add(element);
    }

    public void select( V3DCamera camera) {
        rootElement.select( camera, 0);
    }

    public V3DGroupElement getRootElement(){
        return rootElement;
    }

    public void clear() {
        rootElement.clear();
    }

    public void remove(V3DElement element) {
        rootElement.remove(element);
    }

}
