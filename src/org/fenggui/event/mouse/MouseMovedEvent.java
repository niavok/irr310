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
 * $Id: MouseMovedEvent.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.event.mouse;

import java.util.Set;

import org.fenggui.IWidget;
import org.fenggui.event.key.Key;

public class MouseMovedEvent extends MouseEvent
{
  int displayX, displayY;

  public MouseMovedEvent(IWidget source, int displayX, int displayY, Set<Key> modifiers)
  {
    super(source, modifiers);
    this.displayX = displayX;
    this.displayY = displayY;
  }

  @Override
  public int getDisplayX()
  {
    return displayX;
  }

  @Override
  public int getDisplayY()
  {
    return displayY;
  }

}
