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
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SGISGenerateMipmap;

/**
 * @author fberto
 */
/*public class TextureManager {

    static Map<BufferedImage, Texture> textureCache = new HashMap<BufferedImage, Texture>();

    private Texture readTexture(BufferedImage image) {
        return readPixels(image);
    }

    public Texture getTexture(BufferedImage image) {
        if (textureCache.containsKey(image)) {
            return textureCache.get(image);
        } else {
            Texture newTexture = loadTexture(image);
            textureCache.put(image, newTexture);
            return newTexture;
        }
    }

    public void clearCache() {
        for (Texture texture : textureCache.values()) {
            texture.setValid(false);
        }

        textureCache.clear();
    }

    private Texture readPixels(BufferedImage img) {

        int[] packedPixels = new int[img.getWidth() * img.getHeight()];
        PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), packedPixels, 0, img.getWidth());
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        int bytesPerPixel = 4;
        ByteBuffer unpackedPixels = BufferUtils.createByteBuffer(packedPixels.length * bytesPerPixel);
        for (int row = img.getHeight() - 1; row >= 0; row--) {
            for (int col = 0; col < img.getWidth(); col++) {
                int packedPixel = packedPixels[row * img.getWidth() + col];
                unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
            }
        }
        unpackedPixels.flip();
        return new Texture(unpackedPixels, img.getWidth(), img.getHeight());

    }

    public static IntBuffer createIntBuffer(int size) {
        ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());

        return temp.asIntBuffer();
    }

    private Texture loadTexture(BufferedImage image) {

        IntBuffer texture_ids = createIntBuffer(1);
        GL11.glGenTextures(texture_ids);
        Texture texture = readTexture(image);
        texture.setId(texture_ids.get(0));

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_ids.get(0));
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, SGISGenerateMipmap.GL_GENERATE_MIPMAP_SGIS, GL11.GL_TRUE);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
                          0,
                          image.getColorModel().getNumComponents(),
                          texture.getWidth(),
                          texture.getHeight(),
                          0,
                          GL11.GL_RGBA,
                          GL11.GL_UNSIGNED_BYTE,
                          texture.getPixels());

        return texture;

    }

    void refresh(TextureHandler handler) {
        handler.setTexture(getTexture(handler.getBuffer()));
    }

    public static class Texture {

        private ByteBuffer pixels;
        private int width;
        private int height;
        private int id;
        private boolean valid;

        public Texture(ByteBuffer pixels, int width, int height) {
            this.height = height;
            this.pixels = pixels;
            this.width = width;
            this.valid = true;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGlId() {
            if(!valid) {
                throw new IllegalStateException("GL Texture id perimed");
            }
            return id;
        }

        public int getHeight() {
            return height;
        }

        public ByteBuffer getPixels() {
            return pixels;
        }

        public int getWidth() {
            return width;
        }
    }

    public static BufferedImage LoadImage(String string) throws IOException {
        return ImageIO.read(new File(string));
    }
}*/
