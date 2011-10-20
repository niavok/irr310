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
package org.fenggui.binding.render.jogl;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.fenggui.binding.render.Cursor;
import org.fenggui.binding.render.CursorFactory;

public class JOGLCursorFactory extends CursorFactory
{
  private Component component = null;

  public JOGLCursorFactory(Component parent)
  {
    component = parent;

    setDefaultCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR), parent));
    setMoveCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR), parent));
    setTextCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR), parent));
    setVerticalResizeCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR), parent));
    setHorizontalResizeCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR),
        parent));
    setNWResizeCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR), parent));
    setSWResizeCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR), parent));
    setHandCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR), parent));
    setForbiddenCursor(new JOGLCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR), parent));
  }

  @Override
  public Cursor createCursor(int xHotspot, int yHotspot, BufferedImage image)
  {
    Toolkit tk = Toolkit.getDefaultToolkit();
    return new JOGLCursor(tk.createCustomCursor(image, new Point(xHotspot, yHotspot), null), component);
  }

}
