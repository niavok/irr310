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
 * Created on Jan 30, 2006
 * $Id: AWTGLCanvasBinding.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.binding.render.lwjgl;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.fenggui.binding.clipboard.AWTClipboard;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.jogl.JOGLCursorFactory;
import org.lwjgl.opengl.AWTGLCanvas;

/**
 * Alternative binding for LWJGL. 
 * It uses <code>org.lwjgl.opengl.AWTGLCanvas</code> insead of
 * <code>org.lwjgl.opengl.Display</code> to bind to.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class AWTGLCanvasBinding extends Binding
{
  private AWTGLCanvas       canvas        = null;
  private JOGLCursorFactory cursorFactory = null;
  private IClipboard        clipboard     = new AWTClipboard();

  /**
   * Constructs a new <code>AWTGLCanvasBinding</code> object.
   * @param canvas the canvas
   */
  public AWTGLCanvasBinding(final AWTGLCanvas canvas)
  {
    super(new LWJGLOpenGL());

    this.canvas = canvas;

    cursorFactory = new JOGLCursorFactory(canvas);

    canvas.addComponentListener(new ComponentListener()
    {
      public void componentResized(ComponentEvent ce)
      {
        fireDisplayResizedEvent(canvas.getWidth(), canvas.getHeight());
      }

      public void componentMoved(ComponentEvent arg0)
      {
      }

      public void componentShown(ComponentEvent arg0)
      {
      }

      public void componentHidden(ComponentEvent arg0)
      {
      }
    });
  }

  @Override
  public ITexture getTexture(InputStream is) throws IOException
  {
    return LWJGLTexture.uploadTextureToVideoRAM(ImageIO.read(is));
  }

  @Override
  public ITexture getTexture(BufferedImage bi)
  {
    return LWJGLTexture.uploadTextureToVideoRAM(bi);
  }

  @Override
  public int getCanvasWidth()
  {
    return canvas.getWidth();
  }

  @Override
  public int getCanvasHeight()
  {
    return canvas.getHeight();
  }

  @Override
  public JOGLCursorFactory getCursorFactory()
  {
    return cursorFactory;
  }

  @Override
  public IClipboard getClipboard()
  {
    return clipboard;
  }

}
