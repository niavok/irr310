// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dCylinder is free software: you can redistribute it and/or modify
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

import com.irr310.i3d.scene.element.I3dGroupElement;

import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author pgesta
 */
public class V3DCylinder extends I3dGroupElement {

    private V3DCircle topCircle;
    private V3DCircle bottomCircle;
    private V3DTube mainTube;
    private boolean showTop = true;
    private boolean showBottom = true;
    private float height;
    private float radius;
    private int quality;

    public V3DCylinder() {
        this(1, 1, 32);
    }

    public V3DCylinder(float radius, float height) {
        this(radius, height, 32);
    }

    public V3DCylinder(float radius, float height, int quality) {

        bottomCircle = new V3DCircle();
        bottomCircle.setPosition(0, 0,-height/2);
        bottomCircle.setQuality(quality);
        bottomCircle.setSize(radius);
        add(bottomCircle);

        topCircle = new V3DCircle();
        topCircle.setPosition(0, 0, height/2);
        topCircle.setQuality(quality);
        topCircle.setSize(radius);
        add(topCircle);

        mainTube = new V3DTube(radius, height, quality);
        add(mainTube);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        topCircle.setSize(radius);
        bottomCircle.setSize(radius);
        mainTube.setRadius(radius);
    }

    public float getRadius() {
        return radius;
    }

    public void setHeight(float height) {

        this.height = height;
        topCircle.setPosition(0, 0, height/2);
        bottomCircle.setPosition(0, 0,-height/2);
        mainTube.setHeight(height);
    }

    public float getHeight() {
        return this.height;
    }

    public void setQuality(int quality) {
        this.quality = quality;
        topCircle.setQuality(quality);
        bottomCircle.setQuality(quality);
        mainTube.setQuality(quality);
    }

    public int getQuality() {
        return quality;
    }

    public boolean isShowBottom() {
        return showBottom;
    }

    public void setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        remove(bottomCircle);

        if (showBottom) {
            add(bottomCircle);
        }
    }

    public boolean isShowTop() {
        return showTop;
    }

    public void setShowTop(boolean showTop) {
        this.showTop = showTop;
        remove(topCircle);

        if (showTop) {
            add(topCircle);
        }
    }
}
