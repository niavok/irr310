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

import java.nio.ByteBuffer;

import fr.def.iss.vd2.lib_v3d.V3DVect3;

/**
 *
 * @author fberto
 */
public class V3DrawVertex{

    public V3DVect3 position = new V3DVect3(0,0,0);
    public V3DVect3 texture = new V3DVect3(0,0,0);
    public V3DVect3 normal = new V3DVect3(0,0,0);

    public V3DrawVertex() {
        
    }

    public V3DrawVertex(ByteBuffer buffer) {
        texture.x = buffer.getFloat();
        texture.y = buffer.getFloat();
        normal.x = buffer.getFloat();
        normal.y = buffer.getFloat();
        normal.z = buffer.getFloat();
        position.x = buffer.getFloat();
        position.y = buffer.getFloat();
        position.z = buffer.getFloat();
    }



    

}
