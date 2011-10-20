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
 * Created on Apr 18, 2005
 * $Id: LWJGLBinding.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.binding.render.lwjgl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.fenggui.binding.clipboard.AWTClipboard;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.ITexture;
import org.fenggui.theme.XMLTheme;
import org.lwjgl.opengl.Display;

/**
 * Binds FengGUI to LWJGL.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class LWJGLBinding extends Binding
{
  private LWJGLCursorFactory cursorFactory = null;
  private IClipboard         clipboard     = new AWTClipboard();

  public LWJGLBinding()
  {
    this(new LWJGLOpenGL());
  }

  public LWJGLBinding(IOpenGL gl)
  {
    super(gl);

    cursorFactory = new LWJGLCursorFactory();

    XMLTheme.TYPE_REGISTRY.register("Texture", LWJGLTexture.class);
  }

  @Override
  public ITexture getTexture(InputStream is) throws IOException
  {
    if (!this.getOpenGL().isOpenGLThread())
      throw new RuntimeException("getTexture called not in OpenGL Thread!");
    return LWJGLTexture.uploadTextureToVideoRAM(ImageIO.read(is));
  }

  @Override
  public ITexture getTexture(BufferedImage bi)
  {
    if (!this.getOpenGL().isOpenGLThread())
      throw new RuntimeException("getTexture called not in OpenGL Thread!");

    return LWJGLTexture.uploadTextureToVideoRAM(bi);
  }

  /* (non-Javadoc)
   * @see joglui.binding.Binding#getCanvasWidth()
   */
  public int getCanvasWidth()
  {
    return Display.getDisplayMode().getWidth();
  }

  /* (non-Javadoc)
   * @see joglui.binding.Binding#getCanvasHeight()
   */
  public int getCanvasHeight()
  {
    return Display.getDisplayMode().getHeight();
  }

  @Override
  public LWJGLCursorFactory getCursorFactory()
  {
    return cursorFactory;
  }

  @Override
  public IClipboard getClipboard()
  {
    return clipboard;
  }

}
