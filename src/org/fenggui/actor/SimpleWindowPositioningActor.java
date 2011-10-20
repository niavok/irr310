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
 * Created on 15.11.2007
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.composite.Window;
import org.fenggui.event.DisplayResizedEvent;
import org.fenggui.event.IDisplayResizedListener;
import org.fenggui.event.IWidgetListChangedListener;
import org.fenggui.event.WidgetListChangedEvent;
import org.fenggui.layout.StaticLayout;
import org.fenggui.util.Dimension;
import org.fenggui.util.Log;
import org.fenggui.util.Point;
import org.fenggui.util.Spacing;

/**
 * Positions Windows when they are added to a display. It starts by centering the first
 * window. All other windows will be placed with the given "movement". This "movement" can
 * only be positive and negative in the vertical direction. This is similar to what most
 * operating systems do. As soon as the border is reached it starts at the other side.
 * 
 * @author Marc Menghin
 * 
 */
public class SimpleWindowPositioningActor implements IActor
{
  private Display                    display        = null;
  private IWidgetListChangedListener listener       = null;
  private Point                      lastPosition   = null;
  private Point                      movementDelta  = null;
  private Spacing                    padding        = null;
  private IActorSpacingReader        reader         = null;
  private Window                     lastWindow     = null;
  private IDisplayResizedListener    resizeListener = null;

  public SimpleWindowPositioningActor(Point movement, IActorSpacingReader reader)
  {
    this.movementDelta = movement;
    this.reader = reader;
    this.listener = new IWidgetListChangedListener()
    {

      public void widgetAdded(WidgetListChangedEvent widgetAddedEvent)
      {
        IWidget[] widgets = widgetAddedEvent.getAddedWidget();
        if (widgets.length >= 1)
        {
          IWidget widget = widgets[0];
          if (widget instanceof Window)
          {
            position((Window) widget);
          }
        }
      }

      public void widgetRemoved(WidgetListChangedEvent widgetAddedEvent)
      {
        // DO nothing
      }

    };

    this.resizeListener = new IDisplayResizedListener()
    {

      public void displayResized(DisplayResizedEvent displayResizedEvent)
      {
        lastPosition = null;
      }

    };
  }

  public SimpleWindowPositioningActor(Point movement, Spacing padding)
  {
    this(movement, (IActorSpacingReader) null);
    this.padding = padding;
  }

  public void hook(IWidget widget)
  {
    if (widget instanceof Display)
    {
      setup((Display) widget);
    }
  }

  private void setup(Display widget)
  {
    display = widget;
    display.addWidgetListChangedListener(listener);
    display.getBinding().addDisplayResizedListener(resizeListener);
  }

  public void unHook(IWidget widget)
  {
    if (display != null)
    {
      display.removeWidgetListChangedListener(listener);
      display.getBinding().removeDisplayResizedListener(resizeListener);
    }

    display = null;
  }

  private boolean hasWindows()
  {
    int windows = 0;
    if (display != null)
    {
      for (IWidget widget : display.getWidgets())
      {
        if (widget instanceof Window)
          windows++;
        if (windows > 1)
          return true;
      }
    }
    return false;
  }

  private void position(Window window)
  {
    if (display == null || window == null || lastWindow == window)
      return;

    // update padding
    if (reader != null)
      padding = reader.getSpacing();
    else if (padding == null)
      Log.error("No padding defined in SimpleWindowPositioningActor!");

    // if no window is present position in center
    if (!hasWindows())
      lastPosition = null;

    Dimension area = display.getSize();
    Dimension size = window.getSize();
    Dimension validSpace = new Dimension(area.getWidth() - (size.getWidth() + padding.getRight()), area.getHeight()
        - (padding.getTop()));
    Point position = null;

    if (lastPosition == null)
    {
      StaticLayout.center(window, display);
      position = window.getPosition();
      window.setPosition(new Point(position.getX() + (padding.getLeft() - padding.getRight()) / 2, position.getY()
          + (padding.getBottom() - padding.getTop()) / 2));
      position = window.getPosition();
      if (position.getY() + size.getHeight() > area.getHeight())
      {
        position.setY(area.getHeight() - size.getHeight());
        window.setPosition(position);
      }
    }
    else
    {
      lastPosition.translate(movementDelta.getX(), movementDelta.getY());
      if (lastPosition.getX() < padding.getLeft() || lastPosition.getX() > validSpace.getWidth())
      {
        lastPosition.setX(padding.getLeft());
      }
      if (lastPosition.getY() - size.getHeight() < padding.getBottom() || lastPosition.getY() > validSpace.getHeight())
      {
        lastPosition.setY(validSpace.getHeight());
      }
      position = new Point(lastPosition.getX(), lastPosition.getY() - size.getHeight());
      window.setPosition(position);
    }
    lastPosition = new Point(position.getX(), position.getY() + size.getHeight());
    lastWindow = window;
  }

  /**
   * @return the padding
   */
  public Spacing getPadding()
  {
    return padding;
  }

  /**
   * @param padding
   *          the padding to set
   */
  public void setPadding(Spacing padding)
  {
    this.padding = padding;
  }

  public abstract interface IActorSpacingReader
  {
    public abstract Spacing getSpacing();
  }
}
