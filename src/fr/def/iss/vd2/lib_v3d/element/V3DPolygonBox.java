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

import java.util.ArrayList;
import java.util.List;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;

/**
 *
 * @author fberto
 * @refactored by pgesta
 */
public class V3DPolygonBox extends V3DGroupElement {

    private List<V3DPolygonWalls> WallList = new ArrayList<V3DPolygonWalls>();
    private V3DPolygon topPolygon;
    private V3DPolygon bottomPolygon;
    private boolean showTop = true;
    private boolean showBottom = true;
    private float height;

    public V3DPolygonBox(V3DContext context) {

        this(context, 1);
    }

    public V3DPolygonBox(V3DContext context, float height) {

        super(context);

        this.height = height;

        bottomPolygon = new V3DPolygon(context);
        bottomPolygon.setPosition(0, 0,-height/2);
        add(bottomPolygon);
              
        topPolygon = new V3DPolygon(context);
        topPolygon.setPosition(0, 0, height/2);
        add(topPolygon);
    }

    public void setPointList(List<V3DVect3> pointList, boolean isHole) {
        
        // Déréférencement de tous les composants à dessiner du V3DGroupElement
        // polygones de fond et couvercle, les murs en trous ou en pleins
        clear();
        
        // Déréférencement de tous les murs du V3DPolygonBox
        WallList.clear();

        // Affectation au V3DPolygonBox de son nouveau polygone de référence
        addPointList(pointList, isHole);

        // Re-référecement au V3DGroupElement du polygone de fond si visible
        if (showBottom) {
            add(bottomPolygon);
        }

        // Re-référecement au V3DGroupElement du polygone couvercle si visible
        if (showTop) {
            add(topPolygon);
        }
    }

    public void addPointList(List<V3DVect3> pointList, boolean isHole) {

        V3DPolygonWalls polygonWall = new V3DPolygonWalls(getContext());
        polygonWall.setRenderMode(V3DPolygonWalls.RenderMode.PLAIN);
        polygonWall.setHeight(height);
        polygonWall.setPointList(pointList);

        // Redéfinition des sommets des polygones de fond et de couvercle
        topPolygon.addPointList(pointList, isHole);
        bottomPolygon.addPointList(pointList, isHole);

        // Référencement au V3DGroupElement du mur argument
        add(polygonWall);

        // Référencement à la liste des murs du V3DPolygonBox du mur argument
        WallList.add(polygonWall);
    }

    public void setHeight(float height) {

        this.height = height;
        topPolygon.setPosition(0, 0, height/2);
        bottomPolygon.setPosition(0, 0,-height/2);

        // Actualisation de la hauteur des murs du V3DPolygonBox
        for (V3DPolygonWalls aWall : WallList) {
                aWall.setHeight(height);
            }
    }

    public float getHeight() {
        return this.height;
    }

    public boolean isShowBottom() {
        return showBottom;
    }

    public void setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        remove(bottomPolygon);

        if (showBottom) {
            add(bottomPolygon);
        }
    }

    public boolean isShowTop() {
        return showTop;
    }

    public void setShowTop(boolean showTop) {
        this.showTop = showTop;
        remove(topPolygon);

        if (showTop) {
            add(topPolygon);
        }
    }

}
