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
 * Created on Oct 8, 2006
 * $Id: MouseExitedEvent.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.event.mouse;

import java.util.Set;

import org.fenggui.IWidget;
import org.fenggui.event.key.Key;

public class MouseExitedEvent extends MouseEvent
{
  private IWidget     entered = null;
  private IWidget     exited  = null;
  private int         displayX;
  private int         displayY;
  private int         clickCount;
  private MouseButton button;

  public MouseExitedEvent(IWidget entered, IWidget exited, int displayX, int displayY, MouseButton button, int clickCount, Set<Key> modifiers)
  {
    super(exited, modifiers);
    this.entered = entered;
    this.exited = exited;
    this.displayX = displayX;
    this.displayY = displayY;
    this.button = button;
    this.clickCount = clickCount;
  }

  public IWidget getEntered()
  {
    return entered;
  }

  public IWidget getExited()
  {
    return exited;
  }

  public int getDisplayX()
  {
    return displayX;
  }

  public int getDisplayY()
  {
    return displayY;
  }

  public MouseButton getButton()
  {
    return button;
  }

  public int getClickCount()
  {
    return clickCount;
  }
}
