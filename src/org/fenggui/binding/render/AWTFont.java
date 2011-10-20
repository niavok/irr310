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
 */
package org.fenggui.binding.render;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.IOException;

import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

public class AWTFont extends org.fenggui.binding.render.Font implements IFont, IAWTFont
{
  private Font        font;
  private FontMetrics metric               = null;
  private boolean     antialiased          = false;
  private boolean     useFractionalMetrics = false;
  private boolean     underlined           = false;

  public AWTFont(Font font, boolean antialiased, boolean useFractionalMetrics, boolean underlined)
  {
    this.font = font;
    this.antialiased = antialiased;
    this.useFractionalMetrics = useFractionalMetrics;
    this.underlined = underlined;
  }

  /**
   * Loads a Font from an InputOnlyStream
   * @throws IXMLStreamableException 
   * @throws IOException 
   */
  public AWTFont(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  public Dimension calculateSize(String text)
  {
    return null;
  }

  private FontMetrics getMetric()
  {
    if (this.metric == null)
      this.metric = Toolkit.getDefaultToolkit().getFontMetrics(font);

    return metric;
  }

  public int getLineHeight()
  {

    return getMetric().getHeight();
  }

  public int getWidth(String text)
  {
    return getMetric().stringWidth(text);
  }

  public boolean isCharacterMapped(char c)
  {
    return font.canDisplay(c);
  }

  public Font getFont()
  {
    return font;
  }

  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    String name = stream.processAttribute("fontName", "not-set");
    String typeStr = stream.processAttribute("type", "not-set", "plain");
    int type = java.awt.Font.PLAIN;

    if (typeStr.equalsIgnoreCase("plain"))
      type = java.awt.Font.PLAIN;
    else if (typeStr.equalsIgnoreCase("bold"))
      type = java.awt.Font.BOLD;
    else if (typeStr.equalsIgnoreCase("italic"))
      type = java.awt.Font.ITALIC;
    else
      throw new IllegalArgumentException("Unknwown font type '" + typeStr + "'");

    int size = stream.processAttribute("size", 16);
    font = new java.awt.Font(name, type, size);
    antialiased = stream.processAttribute("antialiasing", true, false);
    useFractionalMetrics = stream.processAttribute("fractionalMetrics", true, false);
  }

  /**
   * @return Returns the antialiased.
   */
  public boolean isAntialiased()
  {
    return antialiased;
  }

  /**
   * @return Returns the useFractionalMetrics.
   */
  public boolean isUseFractionalMetrics()
  {
    return useFractionalMetrics;
  }

  public boolean isUnderlined()
  {
    return underlined;
  }

  public void setUnderlined(boolean underlined)
  {
    this.underlined = underlined;
  }
}
