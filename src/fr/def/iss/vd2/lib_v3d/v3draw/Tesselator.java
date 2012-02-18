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
package fr.def.iss.vd2.lib_v3d.v3draw;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallback;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

/**
 *
 * @author fberto
 */
public class Tesselator {

    Tesselation tesselation = new Tesselation();
    private final GLUtessellator tessellator;

    public Tesselator() {

        tessellator = GLU.gluNewTess();

        GLUtessellatorCallback callback = new GLUtessellatorCallbackAdapter() {

            List<V3DrawVertex> currentVertexList = new ArrayList<V3DrawVertex>();
            int type = -1;

            @Override
            public void begin(int i) {
                currentVertexList = new ArrayList<V3DrawVertex>();
                type = i;
            }

            @Override
            public void end() {
                tesselation.getVertexTypeList().add(type);
                tesselation.getVertexListList().add(currentVertexList);
            }

            @Override
            public void error(int errnum) {
                System.err.println("Tessellation Error " + errnum);
            }

            @Override
            public void vertex(Object vertexData) {
                float[] pointer;
                if (vertexData instanceof float[]) {
                    V3DrawVertex vertex = new V3DrawVertex();
                    pointer = (float[]) vertexData;
                    
                  

                    vertex.position.x = pointer[0];
                    vertex.position.y = pointer[1];
                    vertex.position.z = pointer[2];
                    vertex.texture.x = pointer[3];
                    vertex.texture.y = pointer[4];
                    vertex.normal.x = pointer[5];
                    vertex.normal.y = pointer[6];
                    vertex.normal.z = pointer[7];

                    currentVertexList.add(vertex);
                }
            }

            @Override
            public void combine(double[] coords, Object[] data, 
                    float[] weight, Object[] outData) {
                double[] vertex = new double[3];

                vertex[0] = coords[0];
                vertex[1] = coords[1];
                vertex[2] = coords[2];
                outData[0] = vertex;
            }
        };

        //Repair
        /*glu.gluTessCallback(tessellator, GLU.GLU_TESS_VERTEX, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_BEGIN, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_END, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_ERROR, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_COMBINE, callback);*/


    }

    public void begin() {
    	//Repair
    	/*
        glu.gluTessProperty(tessellator, 
                GLU.GLU_TESS_WINDING_RULE, 
                GLU.GLU_TESS_WINDING_NONZERO);

        glu.gluTessNormal(tessellator, 0, 0, 0);

        glu.gluTessBeginPolygon(tessellator,null);
        glu.gluTessBeginContour(tessellator);
        */
    }

    public void end() {
    	//Repair
    	/*
        glu.gluTessEndContour(tessellator);

        glu.gluTessEndPolygon(tessellator);
        */
    }

    public void addVertex(V3DrawVertex vertex) {

        float[] userData = new float[8];
        userData[0] = vertex.position.x;
        userData[1] = vertex.position.y;
        userData[2] = vertex.position.z;
        userData[3] = vertex.texture.x;
        userData[4] = vertex.texture.y;
        userData[5] = vertex.normal.x;
        userData[6] = vertex.normal.y;
        userData[6] = vertex.normal.z;

        //Repair
        /*
        glu.gluTessVertex(tessellator, vertex.position.toDoubleArray(), 0, userData);
        */
    }

    public Tesselation getResult() {
        return tesselation;
    }

    public void delete() {
    	//Repair
    	/*
        glu.gluDeleteTess(tessellator);
        */
    	
    }
}
