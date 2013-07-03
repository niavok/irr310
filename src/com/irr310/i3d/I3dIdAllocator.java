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

package com.irr310.i3d;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.irr310.i3d.scene.element.I3dElement;

/**
 *
 * @author fberto
 * TODO: desallocation
 */
public class I3dIdAllocator {
    long nextValue = 1;

    Map<Long, WeakReference<I3dElement>> elementMap = new HashMap<Long, WeakReference<I3dElement>>();


    public long getNewId(I3dElement element) {

        long key = nextValue++;

        elementMap.put(key,new WeakReference<I3dElement>(element));

        return key;
    }

    public I3dElement getElement(long id) {
        if(elementMap.containsKey(id)) {
            return elementMap.get(id).get();
        }
        else {
            return null;
        }
    }


}
