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
package fr.def.iss.vd2.lib_v3d.element;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallback;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
public class V3DPolygon extends V3DElement {

    private List<List<V3DVect3>> pointListList = new ArrayList<List<V3DVect3>>();
    private List<Boolean> holeList = new ArrayList<Boolean>();
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private GLUtessellator tessellator;
    private List<List<double[]>> vertexListList = new ArrayList<List<double[]>>();
    private List<Integer> vertexTypeList = new ArrayList<Integer>();
    private boolean needCompute = false;
    private float thickness = 1.0f;
    private RenderMode renderMode = RenderMode.PLAIN;

    public enum RenderMode {

        SOLID,
        PLAIN,
    }

    public V3DPolygon(V3DContext context) {
        super(context);
        boundingBox.setSize(0, 0, 0);
    }

    public void setPointList(List<V3DVect3> pointList) {
        pointListList.clear();
        holeList.clear();
        addPointList(pointList, false);
    }

    public void addPointList(List<V3DVect3> pointList, boolean isHole) {
        pointListList.add(pointList);
        holeList.add(isHole);

        boundingBox.setExist(false);

        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < pointListList.size(); i++) {
            if (!pointList.isEmpty()) {
                boundingBox.setExist(true);


                for (V3DVect3 point : pointList) {
                    if (point.x < minX) {
                        minX = point.x;
                    }
                    if (point.y < minY) {
                        minY = point.y;
                    }
                    if (point.x > maxX) {
                        maxX = point.x;
                    }
                    if (point.y > maxY) {
                        maxY = point.y;
                    }
                }
                V3DVect3 center = new V3DVect3((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, 0);
                V3DVect3 size = new V3DVect3(maxX - minX, maxY - minY, 0);

                boundingBox.setSize(size);
                boundingBox.setFlat(true);
                boundingBox.setPosition(center);
            }
        }
        needCompute = true;
    }

    @Override
    protected void doInit() {
        final GLU glu = new GLU();
        tessellator = glu.gluNewTess();

        GLUtessellatorCallback callback = new GLUtessellatorCallbackAdapter() {

            List<double[]> currentVertexList = new ArrayList<double[]>();
            int type = -1;

            @Override
            public void begin(int i) {
                currentVertexList = new ArrayList<double[]>();
                type = i;
            }

            @Override
            public void end() {
                vertexTypeList.add(type);
                vertexListList.add(currentVertexList);
            }

            @Override
            public void error(int errnum) {
                System.err.println("Tessellation Error " + errnum);
            }

            @Override
            public void vertex(Object vertexData) {

                double[] pointer;
                if (vertexData instanceof double[]) {
                    pointer = (double[]) vertexData;
                    currentVertexList.add(pointer);
                }
            }

            @Override
            public void combine(double[] coords, Object[] data, //
                    float[] weight, Object[] outData) {
                double[] vertex = new double[3];

                vertex[0] = coords[0];
                vertex[1] = coords[1];
                vertex[2] = coords[2];
                outData[0] = vertex;
            }
        };

        /*GLUgluTessCallback(tessellator, GLU.GLU_TESS_VERTEX, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_BEGIN, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_END, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_ERROR, callback);
        glu.gluTessCallback(tessellator, GLU.GLU_TESS_COMBINE, callback);*/
        
        //TODO repair

    }

    /*private void computePolygon() {
        vertexListList.clear();
        vertexTypeList.clear();

        GLU glu = new GLU();
        glu.gluTessNormal(tessellator, 0, 0, 1);
        glu.gluTessProperty(tessellator, //
                GLU.GLU_TESS_WINDING_RULE, //
                GLU.GLU_TESS_WINDING_POSITIVE);

        glu.gluTessBeginPolygon(tessellator, null);

        for (int i = 0; i < pointListList.size(); i++) {
            List<V3DVect3> pointList = pointListList.get(i);

            List<V3DVect3> orientedPointList = oriente(pointList, holeList.get(i));

            glu.gluTessBeginContour(tessellator);

            for (V3DVect3 point : orientedPointList) {
                glu.gluTessVertex(tessellator, point.toDoubleArray(), 0, point.toDoubleArray());
            }

            glu.gluTessEndContour(tessellator);
        }
        glu.gluTessEndPolygon(tessellator);

    }*/

    private List<V3DVect3> oriente(List<V3DVect3> pointList, Boolean isHole) {
        float totalAngle = 0;
        int size = pointList.size();
        if (size < 3) {
            return pointList;
        }

        for (int i = 0; i < size; i++) {
            int j = i + 1;
            int k = i + 2;


            if (j >= size) {
                j = j - size;
            }

            if (k >= size) {
                k = k - size;
            }


            V3DVect3 v1 = pointList.get(j).substract(pointList.get(i));
            V3DVect3 v2 = pointList.get(k).substract(pointList.get(j));

            float localAngle = v2.getAngle() - v1.getAngle();

            if (localAngle > Math.PI) {
                localAngle = (float) (localAngle - 2 * Math.PI);
            }

            if (localAngle < -Math.PI) {
                localAngle = (float) (localAngle + 2 * Math.PI);
            }

            totalAngle = totalAngle + localAngle;


        }

        List<V3DVect3> result;

        //        if (totalAngle < 0 ^ !notHole) {

        if (totalAngle < 0 ^ isHole) {
            result = new ArrayList<V3DVect3>();
            for (int i = size - 1; i >= 0; i--) {
                result.add(pointList.get(i));
            }
        } else {
            result = pointList;
        }

        return result;
    }

    @Override
    protected void doDisplay( V3DCamera camera) {

        if (renderMode == RenderMode.PLAIN) {
            displayPlainPolygon();
        } else {
            displaySolidPolygon();
        }



    }

    @Override
    protected void doSelect( V3DCamera camera, long parentId) {
        if (parentId != 0) {
            displayPlainPolygon();
        }
    }

    private void displayPlainPolygon() {
        if (needCompute) {
        	// TODO repair
            //computePolygon();
            needCompute = false;
        }

        for (int i = 0; i < vertexTypeList.size(); i++) {

            List<double[]> vertexList = vertexListList.get(i);

            GL11.glBegin(vertexTypeList.get(i));
            for (double[] vertex : vertexList) {
                GL11.glVertex3d(vertex[0], vertex[1], 0);
            }
            GL11.glEnd();


        }
    }

    private void displaySolidPolygon() {
        GL11.glLineWidth(thickness);

        GL11.glBegin(GL11.GL_LINES);

        for (int j = 0; j < pointListList.size(); j++) {
            List<V3DVect3> pointList = pointListList.get(j);

            for (int i = 0; i < pointList.size(); i++) {
                if (i == 0) {
                    drawLine( pointList.get(pointList.size() - 1), pointList.get(i));
                } else {
                    drawLine( pointList.get(i - 1), pointList.get(i));
                }

            }
        }

        GL11.glEnd();
    }

    private void drawLine( V3DVect3 point0, V3DVect3 point1) {
        GL11.glVertex3d(point0.x, point0.y, 0);
        GL11.glVertex3d(point1.x, point1.y, 0);
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }
}
