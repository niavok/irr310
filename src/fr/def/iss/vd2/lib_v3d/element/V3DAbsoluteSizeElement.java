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
public class V3DAbsoluteSizeElement extends V3DElement {

    private V3DElement childElement = null;
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private V3DVect3 absoluteSize = new V3DVect3(0, 0, 0);
    private boolean maxOnly = false;
    
    public V3DAbsoluteSizeElement(V3DContext context) {
        super(context);
    }

    public V3DAbsoluteSizeElement(V3DElement element, V3DVect3 size) {
        super(element.getContext());
        childElement = element;
        boundingBox.setSize(new V3DVect3(0, 0, 0));

        this.absoluteSize = size;
    }



    public void setElement(V3DElement element) {
        childElement = element;
    }


    @Override
    protected void doDisplay( V3DCamera camera) {
        if(childElement == null) {
            return;
        }

        float zoomFactor = camera.getRelativeSize();

        V3DVect3 boundingBoxSize = absoluteSize.multiply(zoomFactor);
        V3DVect3 scale = boundingBoxSize.divideBy(childElement.getBoundingBox().getSize());

        if(!maxOnly) {
            

            GL11.glPushMatrix();
            GL11.glScalef(scale.x, scale.y, scale.z);

            childElement.display( camera);

            GL11.glPopMatrix();
        } else {

            float min = Math.min(scale.x, scale.y);
            if(scale.z != 0) {
                min = Math.min(min, scale.z);
            }

            
            GL11.glPushMatrix();
            GL11.glScalef(min ,min ,min);

            childElement.display( camera);

            GL11.glPopMatrix();

        }
    }

    @Override
    protected void doInit() {
        if(childElement == null) {
            return;
        }
        childElement.init();
    }

    @Override
    protected void doSelect( V3DCamera camera, long parentId) {
        if(childElement == null) {
            return;
        }

        float zoomFactor = camera.getRelativeSize();

        V3DVect3 boundingBoxSize = absoluteSize.multiply(zoomFactor);
        V3DVect3 scale = boundingBoxSize.divideBy(childElement.getBoundingBox().getSize());



        if(!maxOnly) {
           
            GL11.glPushMatrix();
            GL11.glScalef(scale.x, scale.y, scale.z);

           childElement.select( camera, parentId);

            GL11.glPopMatrix();
        } else {

            float min = Math.min(scale.x, scale.y);
            if(scale.z != 0) {
                min = Math.min(min, scale.z);
            }

            GL11.glPushMatrix();
            GL11.glScalef(min ,min ,min);

           childElement.select( camera, parentId);

            GL11.glPopMatrix();

        }


     

     
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
       
        return boundingBox;
    }

    public boolean isMaxOnly() {
        return maxOnly;
    }

    public void setMaxOnly(boolean maxOnly) {
        this.maxOnly = maxOnly;
    }

    public void setAbsoluteSize(V3DVect3 absoluteSize) {
        this.absoluteSize = absoluteSize;
    }

    

}
