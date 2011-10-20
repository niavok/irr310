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

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
public class V3DNeutralElement extends V3DElement {

    V3DElement childElement = null;
    private V3DBoundingBox boundingBox = new V3DBoundingBox();

    public V3DNeutralElement(V3DContext context) {
        super(context);
    }

    public V3DNeutralElement(V3DElement element) {
        super(element.getContext());
        childElement = element;
    }

    public void setElement(V3DElement element) {
        childElement = element;
    }

    @Override
    protected void doDisplay( V3DCamera camera) {
        if (childElement == null) {
            return;
        }
        childElement.display( camera);
    }

    @Override
    protected void doInit() {
        if (childElement == null) {
            return;
        }
        childElement.init();
    }

    @Override
    protected void doSelect( V3DCamera camera, long parentId) {
        if (childElement == null) {
            return;
        }
        childElement.select( camera, parentId);
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        if (childElement == null) {
            return new V3DBoundingBox();
        }
        boundingBox.setSize(childElement.getBoundingBox().getSize().multiply(childElement.getScale()));
        boundingBox.setPosition(childElement.getBoundingBox().getPosition().add(childElement.getPosition()));
        boundingBox.setFlat(childElement.getBoundingBox().isFlat());
        boundingBox.setExist(childElement.getBoundingBox().isExist());

        return boundingBox;
    }
}
