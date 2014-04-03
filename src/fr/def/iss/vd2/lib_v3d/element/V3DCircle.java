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
import org.lwjgl.util.glu.GLU;

import com.irr310.i3d.scene.I3dCamera;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.utils.I3dColor;

import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author fberto
 */
public class V3DCircle extends I3dElement {

    private float radius = 0.5f;
    private float innerRadius = 0.f;
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private float thickness = 2.0f;
    private GLU glu = new GLU();
    //private GLUquadric qobj;
    private RenderMode renderMode = RenderMode.PLAIN;
    private int quality = 16;
    private boolean customColor = false;
    private I3dColor innerColor = I3dColor.white;
    private I3dColor outerColor = I3dColor.white;



    public enum RenderMode {
        PLAIN,
        SOLID,
    }


    public V3DCircle() {
        boundingBox.setSize(1, 1, 0);
        boundingBox.setFlat(true);
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public void setColors(I3dColor innerColor,I3dColor outerColor) {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        customColor = true;
    }

    public void setSize(float x) {
        radius = x;
        boundingBox.setSize(2*radius, 2*radius, 0);
        boundingBox.setFlat(true);
    }

    public void setInnerRadius(float x) {
        innerRadius = x;
    }


    @Override
    protected void doInit() {
    	//TODO repair
    	//qobj = GLU.glugluNewQuadric();
    }

    @Override
    protected void doDisplay( I3dCamera camera) {
        
        GL11.glLineWidth(thickness);
        GL11.glPointSize(thickness*0.9f);

        if(renderMode == RenderMode.SOLID) {
            innerRadius = 0.9f*radius;
            drawSolidCircle();

        } else {
            //glu.gluPartialDisk(qobj, 0, radius, quality, 4, 0, 360);

            if(innerRadius == 0) {
                drawCircle();
            } else {
                drawExternCircle();
            }
        }
    }


    private void drawCircle() {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        if(customColor) {
            GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor3f(innerColor.r, innerColor.g, innerColor.b);
        }
	GL11.glVertex3f(0.0f, 0.0f, 0.0f); // center

        float step = 2f*(float) Math.PI / (float) quality;
        if(customColor) {

            GL11.glColor4f(outerColor.r, outerColor.g, outerColor.b, outerColor.a);
        }
        
        for(int i = 0; i <= quality; i++) {
            GL11.glVertex3f((float) (radius * Math.cos(step*i)), (float) (radius * Math.sin(step*i)), 0f);
        }

        if(customColor) {
            
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPopAttrib();
        }

        GL11.glEnd();

    }

    private void drawExternCircle() {
        
        float step = 2f*(float) Math.PI / (float) quality;

        if(customColor) {
            GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);


        for(int i = 0; i <= quality; i++) {
            if(customColor) {
                GL11.glColor4f(innerColor.r, innerColor.g, innerColor.b, innerColor.a);
            }
            GL11.glVertex3f((float) (innerRadius * Math.cos(step*i)), (float) (innerRadius * Math.sin(step*i)), 0f);
            
            if(customColor) {
                GL11.glColor4f(outerColor.r, outerColor.g, outerColor.b, outerColor.a);
            }
            GL11.glVertex3f((float) (radius * Math.cos(step*i)), (float) (radius * Math.sin(step*i)), 0f);
        }

        GL11.glEnd();

        if(customColor) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPopAttrib();
        }

        

    }


    private void drawSolidCircle() {
         float step = 2f*(float) Math.PI / (float) quality;



        GL11.glBegin(GL11.GL_LINE_LOOP);

        for(int i = 0; i <= quality; i++) {
            GL11.glVertex3f((float) (radius * Math.cos(step*i)), (float) (radius * Math.sin(step*i)), 0f);
        }

        GL11.glEnd();


        GL11.glBegin(GL11.GL_POINTS);

        for(int i = 0; i <= quality; i++) {
            GL11.glVertex3f((float) (radius * Math.cos(step*i)), (float) (radius * Math.sin(step*i)), 0f);
        }

        GL11.glEnd();


    }

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

    

}
