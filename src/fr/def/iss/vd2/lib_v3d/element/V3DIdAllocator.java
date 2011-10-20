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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fberto
 * TODO: desallocation
 */
public class V3DIdAllocator {
    long nextValue = 1;

    Map<Long, WeakReference<V3DElement>> elementMap = new HashMap<Long, WeakReference<V3DElement>>();


    public long getNewId(V3DElement element) {

        long key = nextValue++;

        elementMap.put(key,new WeakReference<V3DElement>(element));

        return key;
    }

    public V3DElement getElement(long id) {
        if(elementMap.containsKey(id)) {
            return elementMap.get(id).get();
        }
        else {
            return null;
        }
    }


}
