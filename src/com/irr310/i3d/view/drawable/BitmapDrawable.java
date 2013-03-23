package com.irr310.i3d.view.drawable;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;
import com.irr310.i3d.Texture;
import com.irr310.i3d.TextureManager;

public class BitmapDrawable extends Drawable {

    private final BufferedImage bufferedImage;
    private Texture texture = null;
    private int clipRight = 0;
    private int clipBottom = 0;
    private int clipLeft = 0;
    private int clipTop = 0;

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
        return bufferedImage.getWidth() - clipLeft - clipRight;
    }

    @Override
    public int getIntrinsicHeight() {
        return bufferedImage.getHeight() - clipTop - clipBottom;
    }

    @Override
    public void draw() {
        if (texture == null) {
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

        final float startX = (float) clipLeft / (float) bufferedImage.getWidth();
        final float startY = (float) clipTop / (float) bufferedImage.getHeight();
        final float endX = startX + (float) getIntrinsicWidth() / (float) bufferedImage.getWidth();
        final float endY = startY + (float) getIntrinsicHeight() / (float) bufferedImage.getHeight();

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

    public BufferedImage getImage() {
        return bufferedImage;
    }

    public void setClipTop(int clipTop) {
        this.clipTop = clipTop;

    }

    public void setClipLeft(int clipLeft) {
        this.clipLeft = clipLeft;
    }

    public void setClipBottom(int clipBottom) {
        this.clipBottom = clipBottom;
    }

    public void setClipRight(int clipRight) {
        this.clipRight = clipRight;
    }
}
