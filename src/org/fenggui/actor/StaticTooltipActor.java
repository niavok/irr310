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
 * Created on 20.04.2008
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.IContainer;
import org.fenggui.IWidget;
import org.fenggui.event.Event;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.IEventListener;
import org.fenggui.event.IPositionChangedListener;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.PositionChangedEvent;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.util.Dimension;
import org.fenggui.util.Util;

/**
 * Positions a tooltip at a position relative to the widget. The tooltip
 * data is received from the widgets custom data storage. It will only be
 * displayed on widgets that have this data. The tooltip will be displayed
 * as soon as the widget gets the focus. When the focus is lost the
 * tooltip will be removed again.<br />
 * <br />
 * Implementing classes can easily customize the tooltip appearance and
 * position.
 * 
 * @author marc menghin
 * 
 */
public abstract class StaticTooltipActor implements IActor
{
  public static final String       TOOLTIPOBJECTIDENT = "<TooltipObject>";

  private Container                tooltip            = null;
  private Display                  display            = null;
  private IEventListener           listener           = null;
  private ISizeChangedListener     sizeListener;
  private IPositionChangedListener positionListener;
  private IWidget                  lastWidget         = null;

  /**
   * Constructs a new StaticTooltipActor
   */
  public StaticTooltipActor()
  {
    sizeListener = new ISizeChangedListener()
    {
      public void sizeChanged(SizeChangedEvent event)
      {
        if (isTooltipReady())
          positionTooltipOnWidget(lastWidget, tooltip, display.getSize());
      }
    };

    positionListener = new IPositionChangedListener()
    {
      public void positionChanged(PositionChangedEvent event)
      {
        if (isTooltipReady())
          positionTooltipOnWidget(lastWidget, tooltip, display.getSize());
      }
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#hook(org.fenggui.IWidget)
   */
  public void hook(IWidget widget)
  {
    display = widget.getDisplay();

    if (listener != null)
    {
      unHook(widget);
    }

    if (listener == null)
    {
      listener = new IEventListener()
      {

        public void processEvent(Event event)
        {
          if (event instanceof FocusEvent)
          {
            FocusEvent fevent = (FocusEvent) event;

            if (fevent.isFocusLost())
            {
              hideTooltip();
              lastWidget.removePositionChangedListener(positionListener);
              lastWidget.removeSizeChangedListener(sizeListener);
              lastWidget = null;
            }

            if (fevent.isFocusGained())
            {
              if (tooltip != null && tooltip.isInWidgetTree())
                hideTooltip();
              lastWidget = fevent.getSource();
              lastWidget.addPositionChangedListener(positionListener);
              lastWidget.addSizeChangedListener(sizeListener);
              showTooltip(lastWidget);
            }
          }
        }

      };
    }

    display.addGlobalEventListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#unHook(org.fenggui.IWidget)
   */
  public void unHook(IWidget widget)
  {
    if (listener != null)
    {
      widget.getDisplay().removeGlobalEventListener(listener);
      listener = null;
    }
  }

  /**
   * Creates the tooltip object.
   * 
   * @return the used tooltip widget.
   */
  protected abstract Container createTooltip();

  /**
   * Checks if the tooltip is ready to be shown/moved.
   * 
   * @return
   */
  protected abstract boolean isTooltipReady();

  /**
   * Sets the data to the tooltip widget.
   * 
   * @param data
   */
  protected abstract void setTooltip(Object data);

  /**
   * Positions the tooltip on this widget.
   * 
   * @param widget
   * @param tooltip
   * @param space
   */
  protected abstract void positionTooltipOnWidget(IWidget widget, IWidget tooltip, Dimension space);

  private void hideTooltip()
  {
    if (tooltip != null && tooltip.isInWidgetTree())
    {
      IWidget parent = tooltip.getParent();
      if (parent instanceof IContainer)
      {
        ((IContainer) parent).removeWidget(tooltip);
      }
    }
  }

  private void showTooltip(IWidget widget)
  {
    if (tooltip == null)
      tooltip = createTooltip();

    Object data = getTooltipDataFromWidget(widget);
    setTooltip(data);
    if (data != null)
    {
      displayTooltip(widget);
    }
  }

  private void displayTooltip(IWidget widget)
  {
    positionTooltipOnWidget(widget, tooltip, display.getSize());
    display.addWidget(tooltip);
    //popup has to be on top not this tooltip
    if (display.getPopupWidget() != null && display.getPopupWidget().isInWidgetTree())
    {
      display.bringToFront(display.getPopupWidget());
    }
  }

  private Object getTooltipDataFromWidget(IWidget widget)
  {
    return widget.getData(TOOLTIPOBJECTIDENT);
  }

  /**
   * Should be used to add the tooltip data to the widget. This uses the custom
   * data storage (with HashMap) from the widget.
   * 
   * @see Util
   * @param widget widget to add the data.
   * @param data data used by the tooltip.
   * @return the previous tooltip data associated with the widget, or null if
   *  there was no data. (A null return can also indicate that the tooltip data
   *  previously associated with the widget was null.)
   */
  public static void setTooltipDataToWidget(IWidget widget, Object data)
  {
    widget.setData(TOOLTIPOBJECTIDENT, data);
  }
}
