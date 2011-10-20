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
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
public class V3DPoint extends V3DElement {

    
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private float size = 1.0f;
    
    public V3DPoint(V3DContext context) {
        super(context);
        boundingBox.setSize(0, 0, 0);
    }



    @Override
    protected void doInit() {
       
    }

    @Override
    protected void doDisplay( V3DCamera camera) {

        GL11.glPointSize(size);

        GL11.glBegin(GL11.GL_POINTS);

        GL11.glVertex3d(0,0,0);
        
        GL11.glEnd();

    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setSize(float d) {
        size = d;
    }
  

}
