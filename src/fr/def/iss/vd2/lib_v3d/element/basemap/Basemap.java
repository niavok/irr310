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

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;

/**
 *
 * @author fberto
 */
abstract public class Basemap extends V3DElement{

    float maxWidth;
    float maxHeight;
    float offsetX;
    float offsetY;

    public Basemap(V3DContext context) {
        super(context);
    }



    public void setMaxWidth(float width) {
        maxWidth = width;
    }

    public void setMaxHeight(float height) {
        maxHeight = height;
    }

    public void setOffset(float x, float y){
        offsetX = x;
        offsetY = y;
    }

}
