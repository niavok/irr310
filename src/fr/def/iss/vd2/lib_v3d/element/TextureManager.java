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
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * 
 * @author fberto
 */
public class TextureManager {

	static Map<BufferedImage, TextureHandler> textureCache = new HashMap<BufferedImage, TextureHandler>();

	private Texture readTexture(BufferedImage image) {
		return readPixels(image);
	}

	public TextureHandler getTexture(BufferedImage image) {
		if (textureCache.containsKey(image)) {
			return textureCache.get(image);
		} else {
			TextureHandler newTexture = loadTexture(image);
			textureCache.put(image, newTexture);
			return newTexture;
		}
	}

	public void clearCache() {
		for (BufferedImage buffer : textureCache.keySet()) {
			TextureHandler textureHandler = textureCache.get(buffer);
			textureHandler.setGlId(-1);
		}

		textureCache.clear();
	}

	private Texture readPixels(BufferedImage img) {
		return null;
		/*
		 * int[] packedPixels = new int[img.getWidth() * img.getHeight()];
		 * 
		 * PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0,
		 * img.getWidth(), img.getHeight(), packedPixels, 0, img.getWidth());
		 * try { pixelgrabber.grabPixels(); } catch (InterruptedException e) {
		 * throw new RuntimeException(); }
		 * 
		 * int bytesPerPixel = 4; ByteBuffer unpackedPixels =
		 * BufferUtil.newByteBuffer(packedPixels.length * bytesPerPixel);
		 * 
		 * for (int row = img.getHeight() - 1; row >= 0; row--) { for (int col =
		 * 0; col < img.getWidth(); col++) { int packedPixel = packedPixels[row
		 * * img.getWidth() + col]; unpackedPixels.put((byte) ((packedPixel >>
		 * 16) & 0xFF)); unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
		 * unpackedPixels.put((byte) ((packedPixel ) & 0xFF));
		 * unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
		 * 
		 * } }
		 * 
		 * unpackedPixels.flip();
		 * 
		 * 
		 * return new Texture(unpackedPixels, img.getWidth(), img.getHeight());
		 */
	}

	private TextureHandler loadTexture(BufferedImage image) {

		return null;
		/*
		 * int[] texture_ids = new int[1];
		 * 
		 * GL11.GLgglGenTextures(1, texture_ids, 0); Texture texture =
		 * readTexture(image);
		 * 
		 * GL12.glBindTexture(GL12.GL_TEXTURE_2D, texture_ids[0]);
		 * GL12.GLTexParameteri(GL12.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAG_FILTER,
		 * GL12.GL_LINEAR); GL12.GLTexParameteri(GL12.GL_TEXTURE_2D,
		 * GL12.GL_TEXTURE_MIN_FILTER, GL12.GL_LINEAR_MIPMAP_LINEAR);
		 * GL12.GLTexParameteri(GL12.GL_TEXTURE_2D,
		 * GL12.GL_GENERATE_MIPMAP_SGIS, GL12.GL_TRUE);
		 * 
		 * 
		 * 
		 * GL11.GLTexImage2D(GL11.GL_TEXTURE_2D, 0,
		 * image.getColorModel().getNumComponents(), texture.getWidth(),
		 * texture.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
		 * texture.getPixels());
		 * 
		 * 
		 * return new TextureHandler(this, image, texture_ids[0]);
		 */
	}

	void refresh(TextureHandler handler) {
		TextureHandler newTextureHandler = getTexture(handler.getBuffer());
		handler.setGlId(newTextureHandler.getGlId());
	}

	public static class Texture {

		private ByteBuffer pixels;
		private int width;
		private int height;

		public Texture(ByteBuffer pixels, int width, int height) {
			this.height = height;
			this.pixels = pixels;
			this.width = width;
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
}
