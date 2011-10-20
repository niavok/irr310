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
public class V3DLine extends V3DElement implements LineDrawable {

    private V3DVect3 locationA = new V3DVect3(0, 0, 0);
    private V3DVect3 locationB = new V3DVect3(1, 0, 0);
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private float thickness = 1.0f;
    private int stippleFactor = 1;
    private short stipplePattern = (short) 0xFFFF;

    public V3DLine(V3DContext context) {
        super(context);
        boundingBox.setSize(0, 0, 0);
    }

    public void setLocation(V3DVect3 location1, V3DVect3 location2) {

        locationA = location1;
        locationB = location2;

        V3DVect3 center = location1.add(location2.substract(location1).divideBy(2f));

        boundingBox.setSize(Math.max(location2.x, location1.x) - Math.min(location2.x, location1.x),
                Math.max(location2.y, location1.y) - Math.min(location2.y, location1.y),
                Math.max(location2.z, location1.z) - Math.min(location2.z, location1.z));
        boundingBox.setFlat(false);
        boundingBox.setPosition(center);
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doDisplay( V3DCamera camera) {

        GL11.glLineWidth(thickness);

        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        GL11.glLineStipple(stippleFactor, stipplePattern);

        GL11.glBegin(GL11.GL_LINES);

        GL11.glVertex3d(locationA.x, locationA.y, locationA.z);
        GL11.glVertex3d(locationB.x, locationB.y, locationB.z);

        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_STIPPLE);
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
}
