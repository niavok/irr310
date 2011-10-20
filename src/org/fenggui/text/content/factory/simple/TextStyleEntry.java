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
 */
package org.fenggui.text.content.factory.simple;

import java.io.IOException;

import org.fenggui.DecoratorLayer;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.CursorFactory.CursorType;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Log;

/**
 * @author Marc Menghin
 * 
 */
public class TextStyleEntry implements IXMLStreamable, Cloneable
{
  public final static String DEFAULTSTYLESTATEKEY  = "default";
  public final static String HOVEREDSTYLESTATEKEY  = "hovered";
  public final static String SELECTEDSTYLESTATEKEY = "selected";
  public final static String DISABLEDSTYLESTATEKEY = "disabled";

  private String             fontStyle;
  private Color              color;
  private Color              selectionColor;
  private DecoratorLayer     background;
  private DecoratorLayer     selectionBackground;
  private CursorType         mouseCursor;

  public TextStyleEntry()
  {
    initDefaults();
  }

  public TextStyleEntry(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    this.process(stream);
  }

  private void initDefaults()
  {
    fontStyle = ITextRenderer.DEFAULTTEXTRENDERERKEY;
    color = Color.BLACK;
    selectionColor = Color.WHITE;
    background = new DecoratorLayer();
    selectionBackground = new DecoratorLayer();
    selectionBackground.add(new PlainBackground(Color.BLUE));
    mouseCursor = CursorType.TEXT;
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
   */
  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
   */
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    fontStyle = stream.processAttribute("fontstyle", fontStyle, ITextRenderer.DEFAULTTEXTRENDERERKEY);
    background = stream.processChild("Background", background, new DecoratorLayer(), DecoratorLayer.class);
    selectionBackground = stream.processChild("SelectionBackground", selectionBackground, new DecoratorLayer(),
      DecoratorLayer.class);
    color = stream.processChild("Color", color, Color.BLACK, Color.class);
    selectionColor = stream.processChild("SelectionColor", selectionColor, Color.BLACK, Color.class);
  }

  public String getFontStyle()
  {
    return fontStyle;
  }

  public void setFontStyle(String fontStyle)
  {
    this.fontStyle = fontStyle;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public Color getSelectionColor()
  {
    return selectionColor;
  }

  public void setSelectionColor(Color selectionColor)
  {
    this.selectionColor = selectionColor;
  }

  public DecoratorLayer getBackground()
  {
    return background;
  }

  public DecoratorLayer getSelectionBackground()
  {
    return selectionBackground;
  }

  public CursorType getMouseCursor()
  {
    return mouseCursor;
  }

  public void setMouseCursor(CursorType mouseCursor)
  {
    this.mouseCursor = mouseCursor;
  }

  public ITextRenderer resolveRenderer(TextAppearance appearance)
  {
    return appearance.getRenderer(this.fontStyle);
  }

  @Override
  public TextStyleEntry clone()
  {
    TextStyleEntry result;
    try
    {
      result = (TextStyleEntry) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      //can not happen but write out anyway
      Log.error("Couldn't clone TextStyleEntry", e);
      return null;
    }

    result.color = this.color.clone();
    result.selectionColor = this.selectionColor.clone();
    result.background = this.background.clone();
    result.selectionBackground = this.selectionBackground.clone();

    return result;
  }
}
