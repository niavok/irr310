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
 * $Id: CharacterPixmap.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package com.irr310.i3d.fonts;

import com.irr310.i3d.Texture;

/**
 * A character pixmap is especially designed for describing
 * characters on textures.<br/> 
 *  
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class CharacterPixmap extends Pixmap
{
  private static final int    DEFAULT_CHAR_WIDTH = 10;

  private static final String ATTR_CHARACTER     = "char";
  private static final String ATTR_CHAR_WIDTH    = "char-width";

  private char                character;
  private int                 charWidth          = DEFAULT_CHAR_WIDTH;

  public CharacterPixmap(Texture texture, int x, int y, int width, int height, char c, int charWidth)
  {
    super(texture, x, y, width, height);
    this.charWidth = charWidth;
    character = c;
  }

  public char getCharacter()
  {
    return character;
  }

  public int getCharWidth()
  {
    return charWidth;
  }
  
}
