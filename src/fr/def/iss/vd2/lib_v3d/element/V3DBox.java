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

import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
public class V3DBox extends V3DElement implements LineDrawable {

    private V3DVect3 size = new V3DVect3(1, 1, 1);
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private float thickness = 1.0f;
    private int stippleFactor = 1;
    private short stipplePattern = (short) 0xFFFF;
    private RenderMode renderMode = RenderMode.PLAIN;

    public enum RenderMode {

        SOLID,
        PLAIN,
    }

    public V3DBox(V3DContext context) {
        super(context);
        boundingBox.setSize(1, 1, 1);
        boundingBox.setFlat(false);
    }

    public void setSize(V3DVect3 size) {
        this.size.copy(size);
        boundingBox.setSize(size);
    }

    public V3DVect3 getSize() {
        return size;
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doDisplay( V3DCamera camera) {
        float x0 = -size.x / 2;
        float x1 =  size.x / 2;
        float y0 = -size.y / 2;
        float y1 =  size.y / 2;
        float z0 = -size.z / 2; 
        float z1 =  size.z / 2;  

        if (renderMode == RenderMode.PLAIN) {

            //GL11.glEnable(GL11.GL_LIGHTING);

            GL11.glBegin(GL11.GL_QUADS);

            GL11.glNormal3f(0, 1, 0);
            GL11.glVertex3d(x0, y0, z1);
            GL11.glVertex3d(x0, y0, z0);
            GL11.glVertex3d(x1, y0, z0);
            GL11.glVertex3d(x1, y0, z1);

            GL11.glNormal3f(1, 0, 0);
            GL11.glVertex3d(x0, y1, z1);
            GL11.glVertex3d(x0, y1, z0);
            GL11.glVertex3d(x0, y0, z0);
            GL11.glVertex3d(x0, y0, z1);

            GL11.glNormal3f(0, -1, 0);
            GL11.glVertex3d(x1, y1, z1);
            GL11.glVertex3d(x1, y1, z0);
            GL11.glVertex3d(x0, y1, z0);
            GL11.glVertex3d(x0, y1, z1);

            GL11.glNormal3f(-1, 0, 0);
            GL11.glVertex3d(x1, y0, z1);
            GL11.glVertex3d(x1, y0, z0);
            GL11.glVertex3d(x1, y1, z0);
            GL11.glVertex3d(x1, y1, z1);

            GL11.glNormal3f(0, 0, 1);
            GL11.glVertex3d(x0, y0, z0);
            GL11.glVertex3d(x0, y1, z0);
            GL11.glVertex3d(x1, y1, z0);
            GL11.glVertex3d(x1, y0, z0);

            GL11.glNormal3f(0, 0, -1);
            GL11.glVertex3d(x0, y1, z1);
            GL11.glVertex3d(x0, y0, z1);
            GL11.glVertex3d(x1, y0, z1);
            GL11.glVertex3d(x1, y1, z1);

            GL11.glEnd();

            GL11.glDisable(GL11.GL_LIGHTING);
        }
        if (renderMode == RenderMode.SOLID) {

            GL11.glLineWidth(thickness);

            GL11.glEnable(GL11.GL_LINE_STIPPLE);
            GL11.glLineStipple(stippleFactor, stipplePattern);

            GL11.glBegin(GL11.GL_LINE_LOOP);

            GL11.glVertex3d(x0, y0, z1);
            GL11.glVertex3d(x0, y1, z1);
            GL11.glVertex3d(x1, y1, z1);
            GL11.glVertex3d(x1, y0, z1);

            GL11.glEnd();

            GL11.glBegin(GL11.GL_LINE_LOOP);

            GL11.glVertex3d(x0, y1, z0);
            GL11.glVertex3d(x0, y0, z0);
            GL11.glVertex3d(x1, y0, z0);
            GL11.glVertex3d(x1, y1, z0);

            GL11.glEnd();

            GL11.glBegin(GL11.GL_LINES);

            GL11.glVertex3d(x0, y1, z1);
            GL11.glVertex3d(x0, y1, z0);
            GL11.glVertex3d(x0, y0, z1);
            GL11.glVertex3d(x0, y0, z0);
            GL11.glVertex3d(x1, y0, z1);
            GL11.glVertex3d(x1, y0, z0);
            GL11.glVertex3d(x1, y1, z1);
            GL11.glVertex3d(x1, y1, z0);

            GL11.glEnd();

            GL11.glDisable(GL11.GL_LINE_STIPPLE);
        }
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void setThickness(float d) {
        thickness = d;
    }

    @Override
    public void setStipple(int factor, short pattern) {
        stippleFactor = factor;
        stipplePattern = pattern;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }
}
