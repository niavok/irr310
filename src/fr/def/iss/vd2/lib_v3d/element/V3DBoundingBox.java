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

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;

/**
 *
 * @author fberto
 */
public class V3DBoundingBox {

    private V3DVect3 position = new V3DVect3(0, 0, 0);
    private V3DVect3 size = new V3DVect3(0, 0, 0);
    private V3DVect3 center = new V3DVect3(0, 0, 0);
    private V3DColor lineColor = new V3DColor(0.8f, 0.8f, 1.0f, 1.0f);
    private V3DColor faceColor = new V3DColor(0.8f, 0.8f, 1.0f, 1.0f);
    private V3DColor centerColor = new V3DColor(1.0f, 0.0f, 0.0f, 1.0f);
    private float margin = 0.1f;
    private boolean flat = false;
    private boolean showLines = true;
    private boolean showFaces = true;
    private boolean showCenter = true;
    private boolean exist = true;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isFlat() {
        return flat;
    }

    public void display() {

        if (showCenter) {
            GL11.glColor4f(centerColor.r, centerColor.g, centerColor.b, centerColor.a);
            GL11.glPointSize(4.0f);
            GL11.glBegin(GL11.GL_POINTS);

            GL11.glVertex3d(center.x, center.y, center.z);

            GL11.glEnd();
        }
        if (flat || size.z == 0) {
            displayFlat();
        } else {
            displayThick();
        }
    }

    public void displayFaces() {
        if (flat) {
            displayFlatFaces();
        } else {
            displayThickFaces();
        }
    }

    public void displayLines() {
        if (flat) {
            displayFlatLines();
        } else {
            displayThickLines();
        }
    }

    public void displayFlatLines() {
        float x0 = position.x - size.x / 2;
        float y0 = position.y - size.y / 2;
        float x1 = position.x + size.x / 2;
        float y1 = position.y + size.y / 2;

        GL11.glColor4f(lineColor.r, lineColor.g, lineColor.b, lineColor.a);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        GL11.glVertex3d(x0, y1, 0);
        GL11.glVertex3d(x0, y0, 0);
        GL11.glVertex3d(x1, y0, 0);
        GL11.glVertex3d(x1, y1, 0);

        GL11.glEnd();
    }

    public void displayFlatFaces() {
        float x0 = position.x - size.x / 2;
        float y0 = position.y - size.y / 2;
        float x1 = position.x + size.x / 2;
        float y1 = position.y + size.y / 2;
        GL11.glColor4f(faceColor.r, faceColor.g, faceColor.b, faceColor.a);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex3d(x0, y1, 0);
        GL11.glVertex3d(x0, y0, 0);
        GL11.glVertex3d(x1, y0, 0);
        GL11.glVertex3d(x1, y1, 0);

        GL11.glEnd();
    }

    public void displayFlat() {
        if (showLines) {
            displayFlatLines();
        }
        if (showFaces) {
            displayFlatFaces();
        }
    }

    public void displayThickLines() {
        float x0 = position.x - size.x / 2;
        float x1 = position.x + size.x / 2;
        float y0 = position.y - size.y / 2;
        float y1 = position.y + size.y / 2;
        float z0 = position.z - size.z / 2;
        float z1 = position.z + size.z / 2;

        GL11.glColor4f(lineColor.r, lineColor.g, lineColor.b, lineColor.a);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        GL11.glVertex3d(x0, y0, z1);
        GL11.glVertex3d(x0, y1, z1);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x1, y0, z1);

        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);

        GL11.glVertex3d(x0, y1, z0);
        GL11.glVertex3d(x0, y0, z0);
        GL11.glVertex3d(x1, y0, z0);
        GL11.glVertex3d(x1, y1, z0);

        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINES);

        GL11.glVertex3d(x0, y1, z1);
        GL11.glVertex3d(x0, y1, z0);
        GL11.glVertex3d(x0, y0, z1);
        GL11.glVertex3d(x0, y0, z0);
        GL11.glVertex3d(x1, y0, z1);
        GL11.glVertex3d(x1, y0, z0);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x1, y1, z0);

        GL11.glEnd();
    }

    public void displayThickFaces() {
        float x0 = position.x - size.x / 2;
        float x1 = position.x + size.x / 2;
        float y0 = position.y - size.y / 2;
        float y1 = position.y + size.y / 2;
        float z0 = position.z - size.z / 2;
        float z1 = position.z + size.z / 2;

        GL11.glColor4f(faceColor.r, faceColor.g, faceColor.b, faceColor.a);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex3d(x0, y0, z1);
        GL11.glVertex3d(x0, y0, z0);
        GL11.glVertex3d(x1, y0, z0);
        GL11.glVertex3d(x1, y0, z1);

        GL11.glVertex3d(x0, y1, z1);
        GL11.glVertex3d(x0, y1, z0);
        GL11.glVertex3d(x0, y0, z0);
        GL11.glVertex3d(x0, y0, z1);

        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x1, y1, z0);
        GL11.glVertex3d(x0, y1, z0);
        GL11.glVertex3d(x0, y1, z1);

        GL11.glVertex3d(x1, y0, z1);
        GL11.glVertex3d(x1, y0, z0);
        GL11.glVertex3d(x1, y1, z0);
        GL11.glVertex3d(x1, y1, z1);

        GL11.glVertex3d(x0, y0, z0);
        GL11.glVertex3d(x0, y1, z0);
        GL11.glVertex3d(x1, y1, z0);
        GL11.glVertex3d(x1, y0, z0);

        GL11.glVertex3d(x0, y1, z1);
        GL11.glVertex3d(x0, y0, z1);
        GL11.glVertex3d(x1, y0, z1);
        GL11.glVertex3d(x1, y1, z1);

        GL11.glEnd();
    }

    public void displayThick() {
        if (showLines) {
            displayThickLines();
        }
        if (showFaces) {
            displayThickFaces();
        }
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setSize(float x, float y, float z) {
        size.x = x;
        size.y = y;
        size.z = z;
    }

    public V3DVect3 getPosition() {
        return position;
    }

    public V3DVect3 getSize() {
        return size;
    }

    public V3DVect3 getCenter() {
        return center;
    }

    public V3DColor getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(V3DColor centerColor) {
        this.centerColor = centerColor;
    }

    public V3DColor getFaceColor() {
        return faceColor;
    }

    public void setFaceColor(V3DColor faceColor) {
        this.faceColor = faceColor;
    }

    public V3DColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(V3DColor lineColor) {
        this.lineColor = lineColor;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public boolean isShowCenter() {
        return showCenter;
    }

    public void setShowCenter(boolean showCenter) {
        this.showCenter = showCenter;
    }

    public boolean isShowFaces() {
        return showFaces;
    }

    public void setShowFaces(boolean showFaces) {
        this.showFaces = showFaces;
    }

    public boolean isShowLines() {
        return showLines;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    void setSize(V3DVect3 boundingBoxSize) {
        this.size.copy(boundingBoxSize);
    }

    void setPosition(V3DVect3 position) {
        this.position.copy(position);
    }
}
