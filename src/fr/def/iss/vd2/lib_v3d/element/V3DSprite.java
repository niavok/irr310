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

import java.awt.image.BufferedImage;

import javax.naming.Context;

import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

/**
 *
 * @author fberto
 */
public class V3DSprite extends V3DElement {

    private  TextureHandler texture = null;

    private BufferedImage image = null;
    private V3DVect3 size = new V3DVect3(1, 1, 0);
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private boolean disableRotation = false;
    private boolean enableColor = true;
    private float opacity = 1f;

    public V3DSprite(V3DContext context, BufferedImage image) {
        super(context);
        this.image = image;
        boundingBox.setFlat(true);

        boundingBox.setSize(image.getWidth(), image.getHeight(), 1);
    }

    public boolean isDisableRotation() {
        return disableRotation;
    }

    public void setDisableRotation(boolean disableRotation) {
        this.disableRotation = disableRotation;
    }

    



    public void setSize(float x, float y) {
        
        size.x = x;
        size.y = y;
        size.z = 0;
        
    }

    @Override
    protected void doInit() {
        texture = new TextureHandler(getContext().getTextureManager(), image);
       
    }
    
    @Override
    protected void doDisplay( V3DCamera camera) {

        if(!enableColor) {
            GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
            GL11.glColor4f(1, 1, 1, opacity);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTexEnvf( GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE );

        GL11.glAlphaFunc ( GL11.GL_GREATER, 0.1f ) ;
        GL11.glEnable ( GL11.GL_ALPHA_TEST ) ;

        if(disableRotation) {
            camera.disableRotation();
        }


        float w = 0.5f * size.x * image.getWidth();
        float h = 0.5f * size.y * image.getHeight();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f,  0.0f);
        GL11.glVertex3f(-w, -h,  0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(w, -h, 0.0f);
        GL11.glTexCoord2f(1.0f,  1.0f);
        GL11.glVertex3f(w, h, 0.0f);
        GL11.glTexCoord2f(0.0f,  1.0f);
        GL11.glVertex3f(-w, h, 0.0f);
        GL11.glEnd();  

        if(disableRotation) {
            camera.enableRotation();
        }
        

        GL11.glDisable( GL11.GL_ALPHA_TEST );
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        if(!enableColor) {
            GL11.glPopAttrib();
        }
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setEnableColor(boolean b) {
        this.enableColor = b;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    

}
