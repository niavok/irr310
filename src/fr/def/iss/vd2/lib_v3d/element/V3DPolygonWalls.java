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

import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
public class V3DPolygonWalls extends V3DElement {

    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private float thickness = 1.0f;
    private List<V3DVect3> pointList;
    private RenderMode renderMode = RenderMode.PLAIN;
    private float height = 1.0f;

    public enum RenderMode {

        SOLID,
        PLAIN,
    }

    public V3DPolygonWalls(V3DContext context) {
        super(context);
        boundingBox.setSize(1, 1, 1);
        boundingBox.setFlat(false);
    }

    public void setPointList(List<V3DVect3> pointList) {
        this.pointList = pointList;

        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        if (pointList.isEmpty()) {
            boundingBox.setExist(false);
        } else {
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
            // C'est mieux de faire le setHeight avant le setPointList
            V3DVect3 center = new V3DVect3((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, 0);
            V3DVect3 size = new V3DVect3(maxX - minX, maxY - minY, height);

            boundingBox.setSize(size);   
            boundingBox.setFlat(height == 0);
            boundingBox.setPosition(center); 
        }
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doDisplay( V3DCamera camera) {

        if (renderMode == RenderMode.PLAIN) {

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glBegin(GL11.GL_QUADS);

            for (int i = 0; i < pointList.size(); i++) {
                if (i == 0) {
                    drawQuad( pointList.get(pointList.size() - 1), pointList.get(i));
                } else {
                    drawQuad( pointList.get(i - 1), pointList.get(i));
                }
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        if (renderMode == RenderMode.SOLID) {

            GL11.glLineWidth(thickness);
            GL11.glBegin(GL11.GL_LINES);

            for (V3DVect3 point : pointList) {
                float z0 = -height / 2;
                float z1 =  height / 2;
                GL11.glVertex3d(point.x, point.y, z1);
                GL11.glVertex3d(point.x, point.y, z0);
            }
            GL11.glEnd();
        }
    }

    private void drawQuad( V3DVect3 point0, V3DVect3 point1) {
        float normX = -(point1.y - point0.y);
        float normY = (point1.x - point0.x);
        float normLenght = (float) Math.sqrt(normX * normX + normY * normY);
        GL11.glNormal3f(normX / normLenght, normY / normLenght, 0);
        float z0 = -height / 2;
        float z1 =  height / 2;
        GL11.glVertex3d(point0.x, point0.y, z1);
        GL11.glVertex3d(point0.x, point0.y, z0);
        GL11.glVertex3d(point1.x, point1.y, z0);
        GL11.glVertex3d(point1.x, point1.y, z1);
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setThickness(float d) {
        thickness = d;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    public void setHeight(float height) {
        // C'est mieux de faire le setHeight avant le setPointList
        this.height = height;
        V3DVect3 mod = boundingBox.getSize();
        mod.z = height;
        boundingBox.setSize(mod);
        boundingBox.setExist(true);
        boundingBox.setFlat(height == 0);
    } 

    public float getHeight() {
        return this.height;
    }
}
