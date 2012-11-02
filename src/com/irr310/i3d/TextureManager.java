package com.irr310.i3d;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.fenggui.util.ImageConverter;
import org.lwjgl.opengl.GL11;

public class TextureManager {

    /**
     * Creates an integer buffer to hold specified ints
     * - strictly a utility method
     *
     * @param size how many int to contain
     * @return created IntBuffer
     */
    public static IntBuffer createIntBuffer(int size)
    {
      ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
      temp.order(ByteOrder.nativeOrder());

      return temp.asIntBuffer();
    }

    
    private static int createTextureID()
    {
      IntBuffer tmp = createIntBuffer(1);
      GL11.glGenTextures(tmp);
      return tmp.get(0);
    }
    
    
    
    private static Texture uploadTextureToVideoRAM(BufferedImage awtImage)
    {
      int textureID = createTextureID();
      GL11.glEnable(GL11.GL_TEXTURE_2D);

      GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

      // convert that image into a byte buffer of texture data
      ByteBuffer textureBuffer = ImageConverter.convertPowerOf2(awtImage);
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

      int texWidth = ImageConverter.powerOf2(awtImage.getWidth());
      int texHeight = ImageConverter.powerOf2(awtImage.getHeight());

      GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texWidth, texHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        textureBuffer);

      GL11.glDisable(GL11.GL_TEXTURE_2D);
      return new Texture(textureID, awtImage.getWidth(), awtImage.getHeight(), texWidth, texHeight);
    }


    public Texture getTexture(BufferedImage image) {
        return uploadTextureToVideoRAM(image);
    }
    
}
