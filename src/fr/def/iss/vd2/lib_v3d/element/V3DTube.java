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

import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author pgesta
 */
public class V3DTube extends V3DElement {

    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private List<V3DVect3> pointList = new ArrayList<V3DVect3>();
    private float thickness = 1.0f;
    private RenderMode renderMode = RenderMode.PLAIN;
    private float height;
    private float radius;
    private int quality;

    public enum RenderMode {

        SOLID,
        PLAIN,
    }

    public V3DTube(V3DContext context) {
        this(context, 1, 1, 16);
    }

    public V3DTube(V3DContext context, float radius, float height) {
        this(context, radius, height, 16);
    }

    public V3DTube(V3DContext context, float radius, float height, int quality) {
        super(context);
        setHeight(height);
        setRadius(radius);
        setQuality(quality); // Force la génération des sommets du périmètre
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doDisplay( V3DCamera camera) {

        if (renderMode == RenderMode.PLAIN) {

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glBegin(GL11.GL_QUADS);

            for (int i = 0; i < pointList.size(); i++) {
                if (i == 0) {
                    drawQuad( pointList.get(pointList.size() - 1), pointList.get(i));
                } else {
                    drawQuad( pointList.get(i - 1), pointList.get(i));
                }
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        if (renderMode == RenderMode.SOLID) {

            GL11.glLineWidth(thickness);
            GL11.glBegin(GL11.GL_LINES);

            for (V3DVect3 point : pointList) {
                float z0 = -height / 2;
                float z1 =  height / 2;
                GL11.glVertex3d(point.x, point.y, z1);
                GL11.glVertex3d(point.x, point.y, z0);
            }
            GL11.glEnd();
        }
        /* Dessine des normales sur les sommets en gradients de couleurs
        GL11.glLineWidth(thickness);
        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        GL11.GLLineStipple(1, (short) 0xFFFF);
        GL11.glPushAttrib(GL11.GL_COLOR);
        GL11.glBegin(GL11.GL_LINES);
        
        for (int i = 0; i < pointList.size(); i++) {
        drawNorm(  pointList.get(i), radius * 0.6f);
        }
        GL11.glPopAttrib();
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_STIPPLE); */
    }

    private void drawQuad( V3DVect3 point0, V3DVect3 point1) {
        float z0 = -height / 2;
        float z1 =  height / 2;

        float normLenght = 1f / radius;
        GL11.glNormal3f(point0.x / normLenght, point0.y / normLenght, 0);
        GL11.glVertex3d(point0.x, point0.y, z1);
        GL11.glVertex3d(point0.x, point0.y, z0);

        GL11.glNormal3f(point1.x / normLenght, point1.y / normLenght, 0);
        GL11.glVertex3d(point1.x, point1.y, z0);
        GL11.glVertex3d(point1.x, point1.y, z1);
    }

    /* Dessine des normales sur les sommets en gradients de couleurs
    private void drawNorm( V3DVect3 origine, float ratio) {

    GL11.glColor3f(0.8f,0.4f, 0);

    GL11.glVertex3d(origine.x, origine.y, 0);
    GL11.glColor3f(0.4f,0.8f, 0);
    GL11.glVertex3d(origine.x * ratio, origine.y * ratio, 0);

    GL11.glColor3f(0.8f,0.4f, 0);
    GL11.glVertex3d(origine.x, origine.y, height);
    GL11.glColor3f(0,0.8f, 0.4f);
    GL11.glVertex3d(origine.x * ratio, origine.y * ratio, height);
    } */

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setThickness(float d) {
        thickness = d;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    public void setHeight(float height) {
        this.height = height;
        V3DVect3 mod = boundingBox.getSize();
        mod.z = height;
        boundingBox.setSize(mod);
    }

    public float getHeight() {
        return this.height;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;

        // Forcer reconstruction des sommets du périmètre de la base du tube
        buildPerimeter();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {

        this.radius = radius;

        // Forcer reconstruction des sommets du périmètre de la base du tube
        buildPerimeter();
    }

    public void buildPerimeter() {

        pointList.clear();

        // Détermination pas angulaire pour créer des sommets sur le périmètre
        float omega = 2f * (float) Math.PI / (float) quality;

        // Parcours du périmètre pour instancier les sommets
        for (int i = 0; i <= quality; i++) {
            pointList.add(new V3DVect3((float) (radius * Math.cos(omega * i)), (float) (radius * Math.sin(omega * i)), 0f));
        }
        boundingBox.setExist(true);
        boundingBox.setSize(2 * radius, 2 * radius, height);
        boundingBox.setFlat(false);
        boundingBox.setPosition(0, 0, 0); // Boîte englobante toujours en relatif
    }
}
