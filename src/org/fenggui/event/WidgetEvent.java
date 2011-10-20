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
 * $Id: Event.java 465 2008-02-19 13:00:33Z marcmenghin $
 */
package org.fenggui.event;

import org.fenggui.IWidget;

/**
 * Base class for all widget events.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date: 2008-02-19 14:00:33 +0100 (Di, 19 Feb 2008) $
 * @version $Revision: 465 $
 */
public class WidgetEvent extends Event
{
  private IWidget source = null;

  /**
   * Creates a new <code>WidgetEvent</code> object.
   * @param source the source from which the event was emitted
   */
  public WidgetEvent(IWidget source)
  {
    this.source = source;
  }

  /**
   * Returns the widet that emitted the event.
   * @return the widget
   */
  public IWidget getSource()
  {
    return source;
  }
}
