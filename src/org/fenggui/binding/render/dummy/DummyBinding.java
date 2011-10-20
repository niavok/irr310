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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.fenggui.binding.clipboard.DummyClipboard;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.CursorFactory;
import org.fenggui.binding.render.ITexture;
import org.fenggui.theme.XMLTheme;

public class DummyBinding extends Binding
{
  private DummyClipboard clipboard = new DummyClipboard();

  public DummyBinding()
  {
    super(new DummyOpenGL());
    XMLTheme.TYPE_REGISTRY.register("Texture", DummyTexture.class);
  }

  @Override
  public ITexture getTexture(InputStream is) throws IOException
  {
    return new DummyTexture();
  }

  @Override
  public ITexture getTexture(BufferedImage bi)
  {
    return new DummyTexture();
  }

  @Override
  public int getCanvasWidth()
  {
    return 1024;
  }

  @Override
  public int getCanvasHeight()
  {
    return 768;
  }

  @Override
  public CursorFactory getCursorFactory()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IClipboard getClipboard()
  {
    return clipboard;
  }

}
