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

import com.irr310.i3d.scene.I3dCamera;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;

/**
 *
 * @author fberto
 */
public class V3DTriangle extends I3dElement implements LineDrawable {

    private V3DVect3 size = new V3DVect3(1, 1, 0);
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private float thickness = 1.0f;
    private int stippleFactor = 1;
    private short stipplePattern = (short) 0xFFFF;
    
    private RenderMode renderMode = RenderMode.SOLID;

    

    public enum RenderMode {
        SOLID,
        PLAIN,
    }

    public V3DTriangle() {
        boundingBox.setSize(1, 1, 0);
        boundingBox.setFlat(true);
    }

    public void setSize(float x, float y) {
        size.x = x;
        size.y = y;
        boundingBox.setSize(x, y, 0);
        boundingBox.setFlat(true);
    }


    @Override
    protected void doInit() {
       
    }

    @Override
    protected void doDisplay( I3dCamera camera) {
        float x0 = -size.x/2;
        float y0 = -size.y/2;
        float x1 = size.x/2;
        float y1 = size.y/2;

        GL11.glLineWidth(thickness);

        if(renderMode == RenderMode.SOLID) {
            GL11.glEnable(GL11.GL_LINE_STIPPLE);
            GL11.glLineStipple(stippleFactor, stipplePattern);

            GL11.glBegin(GL11.GL_LINE_LOOP);

            GL11.glVertex3d(x0, y1, 0);
            GL11.glVertex3d((x1+x0)/2, y0, 0);
            GL11.glVertex3d(x1, y1, 0);

            GL11.glEnd();

            GL11.glDisable(GL11.GL_LINE_STIPPLE);
        } else {
            GL11.glBegin(GL11.GL_TRIANGLES);

            GL11.glVertex3d(x0, y1, 0);
            GL11.glVertex3d((x1+x0)/2, y0, 0);
            GL11.glVertex3d(x1, y1, 0);

            GL11.glEnd();
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

    public V3DVect3 getSize() {
        return size;
    }


}
