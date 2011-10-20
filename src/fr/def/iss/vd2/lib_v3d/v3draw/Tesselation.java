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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fberto
 */
public class Tesselation {

    private List<List<V3DrawVertex>> vertexListList = new ArrayList<List<V3DrawVertex>>();
    private List<Integer> vertexTypeList = new ArrayList<Integer>();


    public List<List<V3DrawVertex>> getVertexListList() {
        return vertexListList;
    }

    public List<Integer> getVertexTypeList() {
        return vertexTypeList;
    }




}
