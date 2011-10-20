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
 * Created on 20.11.2007
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.event.Event;
import org.fenggui.event.IEventListener;
import org.fenggui.event.TickEvent;
import org.fenggui.util.Point;

/**
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class LinearWidgetMovementActor implements IActor
{

  private Point          endPosition   = null;
  private Point          movementSpeed = null;
  private Display        display       = null;
  private IEventListener listener;
  private IWidget        widget        = null;

  /**
   * 
   * @param endPosition
   * @param speed
   */
  public LinearWidgetMovementActor(Point endPosition, Point speed)
  {
    this.endPosition = endPosition;
    this.listener = new IEventListener()
    {

      public void processEvent(Event event)
      {
        if (event instanceof TickEvent)
        {
          TickEvent te = (TickEvent) event;
          moveWidget(te.getTimeDelta());
        }

      }

    };
  }

  public void hook(IWidget widget)
  {
    Display display = widget.getDisplay();

    if (this.display != null && this.display != display)
    {
      throw new IllegalArgumentException("Widget needs to be on same display");
    }
    else
    {
      if (display == null)
      {
        throw new IllegalArgumentException("Widget needs to be in the widgettree.");
      }

      this.display = display;
      this.widget = widget;
      display.addGlobalEventListener(listener);
    }
  }

  public void unHook(IWidget widget)
  {
    if (display != null)
    {
      if (widget == this.widget)
      {
        this.display.removeGlobalEventListener(listener);
        this.display = null;
        this.widget = null;
      }
      else
      {
        throw new IllegalArgumentException("needs to be the hooked widget.");
      }
    }
    else
    {
      throw new IllegalStateException("first you need to hook onto a widget.");
    }
  }

  private void moveWidget(int timeDelta)
  {
    Point currentPosition = new Point(widget.getPosition());

    if (currentPosition.equals(endPosition))
    {
      //end reached remove actor.
      unHook(widget);
    }
    else
    {
      //move widget
      //todo: make framerate independent
      //use time as base to calc movement speed. So someone will tell that it
      //should take 1sec from A to B. More space between A and B means faster movement.

      translate(currentPosition, this.endPosition, this.movementSpeed);
      widget.setPosition(currentPosition);
    }

  }

  private void translate(Point current, Point destination, Point speed)
  {
    if (current.getX() > destination.getX())
    {
      current.setX(current.getX() - speed.getX());
      if (current.getX() < destination.getX())
        current.setX(destination.getX());
    }
    else
    {
      current.setX(current.getX() + speed.getX());
      if (current.getX() > destination.getX())
        current.setX(destination.getX());
    }

    if (current.getY() > destination.getY())
    {
      current.setX(current.getY() - speed.getY());
      if (current.getY() < destination.getY())
        current.setX(destination.getY());
    }
    else
    {
      current.setX(current.getY() + speed.getY());
      if (current.getY() > destination.getY())
        current.setY(destination.getY());
    }

  }
}
