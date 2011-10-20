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
 * Created on Apr 29, 2005
 * $Id: IPaintListener.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.event;

import org.fenggui.binding.render.Graphics;

/**
 * 
 * Abstraction of classes that draw something in the Graphics
 * context.
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 * @version $Revision: 606 $
 *
 */
public interface IPaintListener
{

  public void paint(final Graphics g);
}
