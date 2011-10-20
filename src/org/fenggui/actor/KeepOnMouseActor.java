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
 * Created on 30.01.2008
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.event.Event;
import org.fenggui.event.IEventListener;
import org.fenggui.event.mouse.MouseMovedEvent;
import org.fenggui.util.Point;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class KeepOnMouseActor implements IActor
{
  private IEventListener mouseListener = null;
  private Point          offset;

  public KeepOnMouseActor(Point offset)
  {
    this.offset = offset;
  }

  /* (non-Javadoc)
   * @see org.fenggui.actor.IActor#hook(org.fenggui.IWidget)
   */
  public void hook(final IWidget widget)
  {
    Display display = widget.getDisplay();
    if (display != null)
    {
      mouseListener = new IEventListener()
      {

        public void processEvent(Event event)
        {
          if (event instanceof MouseMovedEvent)
          {
            MouseMovedEvent mouseMovedEvent = (MouseMovedEvent) event;
            widget.setX(mouseMovedEvent.getDisplayX() + offset.getX());
            widget.setY(mouseMovedEvent.getDisplayY() + offset.getY());

          }
        }

      };
      display.addGlobalEventListener(mouseListener);
    }
    else
    {
      throw new IllegalArgumentException("Widget not in widget tree.");
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.actor.IActor#unHook(org.fenggui.IWidget)
   */
  public void unHook(IWidget widget)
  {
    if (mouseListener != null)
    {
      Display display = widget.getDisplay();
      if (display != null)
      {
        display.removeGlobalEventListener(mouseListener);
        mouseListener = null;
      }
    }
  }

}
