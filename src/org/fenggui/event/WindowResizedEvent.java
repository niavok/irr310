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
package org.fenggui.event;

import org.fenggui.IWidget;

public class WindowResizedEvent extends WidgetEvent
{

  private int oldWidth;
  private int oldHeight;

  public WindowResizedEvent(IWidget source, int oldWidth, int oldHeight)
  {
    super(source);
    this.oldHeight = oldHeight;
    this.oldWidth = oldWidth;
  }

  /**
   * Get the previous width of the window. This is not guaranteed to be different from the current width.
   * @return
   */
  public int getOldWidth()
  {
    return oldWidth;
  }

  /**
   * Get the previous height of the window. This is not guaranteed to be different from the current height.
   * @return
   */
  public int getOldHeight()
  {
    return oldHeight;
  }

}
