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
import org.fenggui.event.DisplayResizedEvent;
import org.fenggui.event.IDisplayResizedListener;
import org.fenggui.util.Alignment;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * Aligns a widget within the widgets Display object on a resize of the display.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class KeepOnPositionActor implements IActor
{
  private Alignment               align;
  private IWidget                 widget   = null;
  private Point                   offset   = new Point(0, 0);
  private IDisplayResizedListener listener = new IDisplayResizedListener()
                                           {

                                             public void displayResized(DisplayResizedEvent displayResizedEvent)
                                             {
                                               position(displayResizedEvent.getWidth(), displayResizedEvent.getHeight());
                                             }

                                           };

  /**
   * 
   */
  public KeepOnPositionActor(Alignment align)
  {
    this.align = align;
  }

  public KeepOnPositionActor(Alignment align, Point offset)
  {
    this(align);
    this.offset = offset;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#hook(org.fenggui.IWidget)
   */
  public void hook(IWidget widget)
  {
    if (this.widget != null)
      throw new IllegalStateException("Actor already hooked to an other widget. Unhook that first.");

    widget.getDisplay().getBinding().addDisplayResizedListener(listener);
    this.widget = widget;
    // initial positioning
    position(widget.getDisplay().getWidth(), widget.getDisplay().getHeight());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#unHook(org.fenggui.IWidget)
   */
  public void unHook(IWidget widget)
  {
    if (widget != this.widget)
      throw new IllegalStateException("Actor already hooked on an other widget. Unhook that first.");

    this.widget.getDisplay().getBinding().removeDisplayResizedListener(listener);
    this.widget = null;
  }

  private void position(int width, int height)
  {
    Dimension size = new Dimension(width, height);
    Point newPosition = align.alignBox(new Point(0, 0), size, widget.getSize());
    widget.setX(newPosition.getX() + offset.getX());
    widget.setY(newPosition.getY() + offset.getY());
  }
}
