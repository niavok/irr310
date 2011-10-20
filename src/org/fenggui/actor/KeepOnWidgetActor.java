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
 * Created on 08.04.2008
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.IWidget;
import org.fenggui.event.IPositionChangedListener;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.PositionChangedEvent;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.util.Alignment;
import org.fenggui.util.Point;

/**
 * @author Marc Menghin
 * 
 */
public class KeepOnWidgetActor implements IActor
{

  private Point                    offset;
  private IWidget                  target;
  private IWidget                  widget           = null;
  private IPositionChangedListener positionListener = null;
  private ISizeChangedListener     sizeListener     = null;

  private Alignment                align            = Alignment.MIDDLE;

  /**
   * 
   */
  public KeepOnWidgetActor(IWidget target, Point offset, Alignment align)
  {
    this.offset = offset;
    this.target = target;
    this.align = align;
    this.sizeListener = new ISizeChangedListener()
    {
      public void sizeChanged(SizeChangedEvent event)
      {
        positionWidget(KeepOnWidgetActor.this.widget);
      }
    };
    this.positionListener = new IPositionChangedListener()
    {
      public void positionChanged(PositionChangedEvent event)
      {
        positionWidget(KeepOnWidgetActor.this.widget);
      }
    };
    this.target.addSizeChangedListener(sizeListener);
    this.target.addPositionChangedListener(positionListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#hook(org.fenggui.IWidget)
   */
  public void hook(IWidget widget)
  {
    if (this.widget != null)
      throw new IllegalStateException("Actor already hooked to a widget.");

    this.widget = widget;

    positionWidget(widget);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#unHook(org.fenggui.IWidget)
   */
  public void unHook(IWidget widget)
  {
    if (this.widget != null)
    {
      if (widget == this.widget || widget == null)
      {
        this.widget = null;
      }
      else
      {
        throw new IllegalArgumentException("Different widget then used to hook this actor");
      }
    }
  }

  private void positionWidget(IWidget widget)
  {
    if (widget != null)
    {
      Point pos = align.alignBox(new Point(target.getDisplayX(), target.getDisplayY()), target.getSize(), widget
          .getSize());
      pos.translate(offset);
      widget.setX(pos.getX());
      widget.setY(pos.getY());
    }
  }

  public void removeFromTarget()
  {
    if (target != null)
    {
      target.removeSizeChangedListener(sizeListener);
      target.removePositionChangedListener(positionListener);
      target = null;
    }
  }
}
