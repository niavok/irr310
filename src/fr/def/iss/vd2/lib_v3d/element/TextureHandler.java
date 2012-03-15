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
import java.io.IOException;
import java.nio.IntBuffer;

import org.fenggui.binding.render.ITexture;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.element.TextureManager.Texture;

public class TextureHandler implements ITexture {

    private TextureManager textureManager;
    private BufferedImage buffer;
    private Texture texture;

    
    public TextureHandler(TextureManager textureManager, BufferedImage buffer) {
        this.textureManager = textureManager;
        this.buffer = buffer;
        this.texture = textureManager.getTexture(buffer);
    }

    BufferedImage getBuffer() {
        return buffer;
    }

    @Override
    public void process(InputOutputStream stream) throws IOException, IXMLStreamableException {
    }

    @Override
    public String getUniqueName() {
        return null;
    }

    @Override
    public void dispose() {
        GL11.glDeleteTextures(IntBuffer.wrap(new int[] { texture.getGlId() }));
        texture.setValid(false);
    }

    @Override
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlId());
    }

    @Override
    public int getTextureWidth() {
        return texture.getWidth();
    }

    @Override
    public int getTextureHeight() {
        return texture.getHeight();
    }

    @Override
    public int getImageWidth() {
        return buffer.getWidth();
    }

    @Override
    public int getImageHeight() {
        return buffer.getHeight();
    }

    @Override
    public boolean hasAlpha() {
        return true;
    }

    @Override
    public int getID() {
        if(texture == null) {
            textureManager.refresh(this);
        }
        return texture.getGlId();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }


}
