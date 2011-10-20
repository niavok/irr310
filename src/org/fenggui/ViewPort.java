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
 * Created on 2005-4-13
 * $Id: ViewPort.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.IViewPortPaintListener;

/**
 * Widget to run custom OpenGL commands in. Set the 
 * <code>PaintListener</code> im Appearance Adapter to 
 * run you own code. You can modify the projection matrix and
 * model matrix as much as you want.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class ViewPort extends Widget
{
  private IViewPortPaintListener viewPortPaintListener = null;
  private Display                display               = null;

  @Override
  public void addedToWidgetTree()
  {
    display = getDisplay();
  }

  public ViewPort()
  {
    updateMinSize();
  }

  public void paint(Graphics g)
  {
    if (viewPortPaintListener == null)
      return;

    IOpenGL opengl = g.getOpenGL();
    opengl.pushAllAttribs();
    opengl.setModelMatrixMode();
    opengl.pushMatrix();
    opengl.setProjectionMatrixMode();
    opengl.pushMatrix();
    opengl.setModelMatrixMode();

    int viewPortWidth = getWidth();
    int viewPortHeight = getHeight();

    opengl.setViewPort(g.getTranslation().getX(), g.getTranslation().getY(), viewPortWidth, viewPortHeight);

    viewPortPaintListener.paint(g, viewPortWidth, viewPortHeight);

    opengl.setViewPort(0, 0, display.getWidth(), display.getHeight());

    opengl.setProjectionMatrixMode();
    opengl.popMatrix();
    opengl.setModelMatrixMode();
    opengl.popMatrix();
    opengl.popAllAttribs();
  }

  public IViewPortPaintListener getViewPortPaintListener()
  {
    return viewPortPaintListener;
  }

  public void setViewPortPaintListener(IViewPortPaintListener viewPortPaintListener)
  {
    this.viewPortPaintListener = viewPortPaintListener;
  }

}
