package com.irr310.i3d.view.drawable;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;
import com.irr310.i3d.Texture;
import com.irr310.i3d.TextureManager;

public class BitmapDrawable extends Drawable {

    private final BufferedImage bufferedImage;
    private Texture texture = null;
    
    public BitmapDrawable(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void setBounds(float left, float top, float right, float bottom) {
        super.setBounds(left, top, right, bottom);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public int getIntrinsicWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return bufferedImage.getHeight();
    }
    
    @Override
    public void draw() {
        if(texture ==null) {
            texture = TextureManager.getTexture(bufferedImage);
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (texture != null) {
            if (texture.hasAlpha()) {
                GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
            }
    
            texture.bind();
        }
        
        begin(GL11.GL_QUADS);

        
        final float startX = 0;
        final float startY = 0;
        final float endY = 1;
        final float endX = 1;

        GL11.glTexCoord2f(startX, startY);
        vertex(left, top);

        GL11.glTexCoord2f(startX, endY);
        vertex(left, bottom);

        GL11.glTexCoord2f(endX, endY);
        vertex(right, bottom);

        GL11.glTexCoord2f(endX, startY);
        vertex(right, top);
        end();
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
    
}
