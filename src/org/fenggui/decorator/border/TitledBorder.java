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
 * $Id: TitledBorder.java 633 2009-04-25 09:54:13Z marcmenghin $
 */
package org.fenggui.decorator.border;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.util.Color;

/**
 * Border that displays a line of text at the top.
 *  
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-04-25 11:54:13 +0200 (Sa, 25 Apr 2009) $
 * @version $Revision: 633 $
 */
public class TitledBorder extends Border
{

  private String    title = "";
  private Color     color = Color.GRAY, textColor = Color.BLACK;
  private ImageFont font  = null;

  /**
   * Constructs a new <code>TitledBorder</code> without text.
   *
   */
  public TitledBorder()
  {
    this(ImageFont.getDefaultFont(), "");
  }

  public TitledBorder(String title)
  {
    this(ImageFont.getDefaultFont(), title, Color.BLACK);
  }

  public TitledBorder(ImageFont font, String title)
  {
    this(font, title, Color.BLACK);
  }

  public TitledBorder(ImageFont font, String title, Color textColor)
  {
    super(font.getHeight(), 1, 1, 1);
    this.font = font;
    setTitle(title);
    this.textColor = textColor;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public ImageFont getFont()
  {
    return font;
  }

  public Color getTextColor()
  {
    return textColor;
  }

  public void setTextColor(Color textColor)
  {
    this.textColor = textColor;
  }

  @Override
  public void paint(Graphics g, int localX, int localY, int width, int height)
  {
    g.setColor(textColor);
    g.setFont(font);
    g.drawString(title, localX + 10, localY + height - font.getHeight());

    g.setColor(color);

    final int OFFSET = 2;
    int halfFontSize = font.getHeight() / 2;
    int topPosY = localY + height - halfFontSize - OFFSET;

    // left
    g.setLineWidth(2); //so everyone should be able to see a line
    
    g.drawLine(localX, localY, localX, topPosY);

    // right
    g.drawLine(localX + width - getRight(), localY, localX + width - getRight(), topPosY);

    // top
    g.drawLine(localX, topPosY, localX + 5, topPosY);

    g.drawLine(localX + font.getWidth(title) + 15, topPosY, localX + getLeft() + width, topPosY);

    // bottom
    g.drawLine(localX, localY, getLeft() + width + getRight(), localY);

    // set line width back to default
    g.setLineWidth(1);
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.border.Border#copy()
   */
  @Override
  public TitledBorder copy()
  {
    TitledBorder border = new TitledBorder(this.font, "", this.textColor);
    border.color = this.color;
    super.copy(border);
    return border;
  }

}
