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
 * $Id: WindowClosedEvent.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.event;

import org.fenggui.composite.Window;

/**
 * Class that represents the event of closing a window.
 * 
 * @author Schabby, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class WindowClosedEvent extends WidgetEvent
{

  private Window window = null;

  public WindowClosedEvent(Window w)
  {
    super(w);
    window = w;
  }

  public Window getWindow()
  {
    return window;
  }

}
