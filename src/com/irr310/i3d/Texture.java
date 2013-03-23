package com.irr310.i3d;

import org.lwjgl.opengl.GL11;

public class Texture {

    /** The GL texture ID */
    private int textureID = -1;

    /** The height of the image */
    private int imgHeight;
    /** The width of the image */
    private int imgWidth;

    /** The width of the texture */
    private int texWidth;
    /** The height of the texture */
    private int texHeight;

    /**
     * Create a new texture
     *
     * @param target The GL target 
     * @param textureID The GL texture ID
     */
    public Texture(int textureID, int imageWidth, int imageHeight, int textureWidth, int textureHeight)
    {
      this.textureID = textureID;
      this.texWidth = textureWidth;
      this.texHeight = textureHeight;
      this.imgWidth = imageWidth;
      this.imgHeight = imageHeight;
    }
    
    /**
     * Get the height of the original image
     *
     * @return The height of the original image
     */
    public int getImageHeight()
    {
      return imgHeight;
    }

    /** 
     * Get the width of the original image
     *
     * @return The width of the original image
     */
    public int getImageWidth()
    {
      return imgWidth;
    }

    /* (non-Javadoc)
     * @see joglui.binding.Texture#getTextureWidth()
     */
    public int getTextureWidth()
    {
      return texWidth;
    }

    /* (non-Javadoc)
     * @see joglui.binding.Texture#getTextureHeight()
     */
    public int getTextureHeight()
    {
      return texHeight;
    }
    
    /**
     * Bind the specified GL context to a texture
     */
    public void bind()
    {
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }
    
    
    public boolean hasAlpha()
    {
      return true;
    }

    public void free() {
        GL11.glDeleteTextures(textureID);
        textureID = -1;
    }
}
