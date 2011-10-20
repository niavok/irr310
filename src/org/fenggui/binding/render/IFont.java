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
 * Created on 24.10.2007
 * $Id$
 */
package org.fenggui.binding.render;

import org.fenggui.util.Dimension;

/**
 * A FengGUI Font can be a Image Font (using Pixmaps) or a normal Java AWT Font.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IFont
{
  /**
   * Calculates the size that this text uses. Linebreaks will be considered.
   * 
   * @param text Text to calculate size from.
   * @return returns the size that the given text will use.
   */
  public Dimension calculateSize(String text);

  /**
   * Returns the size of one line.
   * 
   * @return Height of one line.
   */
  public int getLineHeight();

  /**
   * Calculates the size of a text. This method does not consider linebreaks.
   * 
   * @param text Text to calculate width from.
   * @return returns the width of the given text.
   */
  public int getWidth(String text);

  /**
   * Is this really used? I think this should be removed. I don't now any reason for this method.
   * @param c
   * @return
   */
  public boolean isCharacterMapped(char c);
}
