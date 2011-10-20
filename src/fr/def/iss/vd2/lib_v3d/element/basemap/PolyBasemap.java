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

package fr.def.iss.vd2.lib_v3d.element.basemap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DBoundingBox;

/**
 *
 * @author fberto
 */
public class PolyBasemap extends Basemap{
    //PolyLineLoader loader;
    private V3DBoundingBox boundingBox = new V3DBoundingBox();

    int listId;

    FloatBuffer vertices;
    IntBuffer stripVertexCount;
    float maxX;
    float maxY;
    float minX;
    float minY;
    private float innerScale = 1;

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void load(FloatBuffer vertices, IntBuffer stripVertexCount, float maxX, float maxY, float minX, float minY) {
        this.vertices = vertices;
        this.stripVertexCount = stripVertexCount;
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;

        float generalMax = Math.max(maxY, maxX);

        innerScale = 10/generalMax;



        boundingBox = getBoundingBox();

        float w = (maxX - minX)*innerScale;
        float h = (maxY - minY)*innerScale;


        boundingBox.setPosition(0, 0, 0);
        boundingBox.setSize(w, h, 0);
        boundingBox.setFlat(true);

    }

    public PolyBasemap(V3DContext context) {
        super(context);
    }
    

    @Override
    protected void doInit() {
        /*listId = GL11.GLGenLists(1);
        GL11.GLNewList(listId, GL11.GL_COMPILE);
        draw();
        GL11.glEndList();*/
    }

    @Override
    protected void doDisplay( V3DCamera cameraDistance) {
        GL11.glLineWidth(1.0f);
        //GL11.GLCallList(listId);
        draw();
    };

    private void draw() {

        vertices.clear();
        stripVertexCount.clear();

        float dx = -(minX/2 + maxX/2)*innerScale;
        float dy = -(minY/2 + maxY/2)*innerScale;


        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, 0);
        GL11.glScalef(innerScale, innerScale, innerScale);

        while (stripVertexCount.hasRemaining()) {
            int lineLenght = stripVertexCount.get();

            GL11.glBegin(GL11.GL_LINE_STRIP);           	// Draw line
            for(int j = 0; j < lineLenght; j++){
                
                if (vertices.remaining() >= 2) {
                    float x = vertices.get();
                    float y = vertices.get();
                    GL11.glVertex3f(x, y, 0.0f);
                } else {
                    // we should log the problem, but this method is called too
                    // often (each time GL redraws). We don't want to flood the
                    // logs, so we just ignore the problem silently. The
                    // correctness of the data should be done at load time.
                }
            }
            
            GL11.glEnd();
            
        }

        GL11.glPopMatrix();

      
   }



}
