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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author fberto
 */
public class V3DOrderedGroupElement<T extends Comparable<T>> extends V3DCommonGroupElement {

    List<KeyAndElement<T>> childrenList = new ArrayList<KeyAndElement<T>>();

    private static class KeyAndElement<T extends Comparable<T>> implements Comparable<KeyAndElement<T>> {
        public final T key;
        public final V3DElement element;

        public KeyAndElement(T key, V3DElement element) {
            this.key = key;
            this.element = element;
        }

        @Override
        public int compareTo(KeyAndElement<T> o) {
            return key.compareTo(o.key);
        }

    }

    private List<V3DElement> elementList = new AbstractList<V3DElement>() {

        @Override
        public V3DElement get(int index) {
            return childrenList.get(index).element;
        }

        @Override
        public int size() {
            return childrenList.size();
        }

        @Override
        public V3DElement remove(int index) {
            return childrenList.remove(index).element;
        }
    };

    public V3DOrderedGroupElement(V3DContext context) {
        super(context);
    }

    public void addOrdered(V3DElement element, T key) {
        assert(element != null);
        KeyAndElement<T> kae = new KeyAndElement<T>(key, element);
        int insertPoint = Collections.binarySearch(childrenList, kae);
        if (insertPoint < 0) {
            insertPoint = -insertPoint-1;
        }
        childrenList.add(insertPoint, kae);
    }

    public void sort() {
        Collections.sort(childrenList);
    }

    @Override
    protected List<V3DElement> doGetChildren() {
        return elementList;
    }

}
