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
 * Created on Dec 6, 2006
 * $Id: IAppearance.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.appearance;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.util.Dimension;

/**
 * Base interface for appearance definitons of widgets.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public interface IAppearance
{
  /**
   * Called on render-time to draw the background of the widget.
   * @param g graphics object
   * @param gl opengl interface
   */
  public void paint(Graphics g, IOpenGL gl);

  /**
   * Computes the minimum size required by the widget according to its
   * appearance definition.
   * @return the min. size
   */
  public Dimension getMinSizeHint();

}
