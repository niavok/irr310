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
package org.fenggui.binding.render.dummy;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.fenggui.binding.render.ITexture;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;

public class DummyTexture implements ITexture
{

  public void bind()
  {
    // does nothing! It's a dummy implementation

  }

  public void dispose()
  {
    // does nothing! It's a dummy implementation

  }

  public int getTextureWidth()
  {
    // does nothing! It's a dummy implementation
    return 200;
  }

  public int getTextureHeight()
  {
    // does nothing! It's a dummy implementation
    return 100;
  }

  public int getImageWidth()
  {
    // does nothing! It's a dummy implementation
    return 82;
  }

  public int getImageHeight()
  {
    // does nothing! It's a dummy implementation
    return 50;
  }

  public void setAlpha(boolean alpha)
  {
    // does nothing! It's a dummy implementation

  }

  public boolean hasAlpha()
  {
    // does nothing! It's a dummy implementation
    return false;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
  }

  public String getUniqueName()
  {
    return null;
  }

  public void texSubImage2D(int xOffset, int yOffset, int width, int height, ByteBuffer buffer)
  {
  }

  public int getID()
  {
    return 0;
  }

}
