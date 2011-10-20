/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005-2009 FengGUI Project
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details:
 * http://www.gnu.org/copyleft/lesser.html#TOC3
 *
 */
package org.fenggui.binding.render.lwjgl;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.fenggui.binding.render.Cursor;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

public class LWJGLCursor extends Cursor
{
  org.lwjgl.input.Cursor cursor = null;

  public LWJGLCursor(org.lwjgl.input.Cursor c)
  {
    cursor = c;
  }

  public LWJGLCursor(int xHotspot, int yHotspot, BufferedImage image)
  {
    BufferedImage bi = null;

    int width = image.getWidth();
    int height = image.getHeight();

    if (image.getType() != BufferedImage.TYPE_INT_ARGB)
    {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bi.getGraphics().drawImage(image, 0, 0, null);
    }
    else
      bi = image;

    ByteBuffer bb = ByteBuffer.allocateDirect(bi.getWidth() * bi.getHeight() * 4);
    bb.order(ByteOrder.nativeOrder());
    IntBuffer ib = bb.asIntBuffer();

    for (int y = bi.getHeight() - 1; y >= 0; y--)
    {
      for (int x = 0; x < bi.getWidth(); x++)
      {
        ib.put(bi.getRGB(x, y));
      }
    }

    ib.flip();

    try
    {
      cursor = new org.lwjgl.input.Cursor(width, height, xHotspot, yHotspot, 1, ib, null);
    }
    catch (LWJGLException e)
    {

      e.printStackTrace();
    }
  }

  @Override
  public void show()
  {
    try
    {
      Mouse.setNativeCursor(cursor);
    }
    catch (LWJGLException e)
    {
      e.printStackTrace();
    }
  }

}
