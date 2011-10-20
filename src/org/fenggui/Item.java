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
 * $Id: Item.java 611 2009-03-22 15:58:20Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.appearance.EntryAppearance;
import org.fenggui.appearance.LabelAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.text.ITextContentManager;
import org.fenggui.text.TextContentManager;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * 
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-22 16:58:20 +0100 (So, 22 Mär 2009) $
 * @version $Revision: 611 $
 */
public class Item implements IXMLStreamable, IToggable<Item>
{

  protected ITextContentManager textData      = null;
  private Pixmap                pixmap        = null;
  private Object                userData      = null;
  private boolean               isSelected    = false;
  private Dimension             preferredSize = new Dimension(0, 0);

  protected Item()
  {

  }

  public Item(String text, Pixmap pixmap, LabelAppearance appearance)
  {
    textData = new TextContentManager();
    textData.setContent(text, appearance);
    this.pixmap = pixmap;
    updatePreferredSize(appearance);
  }

  public Item(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  public Item(String text, LabelAppearance appearance)
  {
    this(text, null, appearance);
  }

  public String getText()
  {
    return textData.getContent();
  }

  public void setText(String text, LabelAppearance appearance)
  {
    this.textData.setContent(text, appearance);
    updatePreferredSize(appearance);
  }

  public Pixmap getPixmap()
  {
    return pixmap;
  }

  public void setPixmap(Pixmap pixmap, LabelAppearance appearance)
  {
    this.pixmap = pixmap;
    updatePreferredSize(appearance);
  }

  public void setData(Object value)
  {
    this.userData = value;
  }

  public Object getData()
  {
    return userData;
  }

  public boolean isSelected()
  {
    return isSelected;
  }

  public Item setSelected(boolean b)
  {
    this.isSelected = b;
    return this;
  }

  public void adaptChange(int width, int height, LabelAppearance appearance)
  {
    textData.adaptChange(width, height, appearance);
    updatePreferredSize(appearance);
  }

  public Dimension getPreferredSize()
  {
    return preferredSize;
  }

  public void updatePreferredSize(LabelAppearance appearance)
  {
    preferredSize = calculatePreferredSize(appearance);
  }

  protected Dimension calculatePreferredSize(LabelAppearance appearance)
  {
    int width;
    int height;

    width = textData.getSize().getWidth();
    height = textData.getSize().getHeight();

    if (pixmap != null)
    {
      width += pixmap.getWidth();
      if (!textData.isEmpty())
        width += appearance.getGap();
      height = Math.max(pixmap.getHeight(), height);
    }

    if (textData.isWordWarping())
    {
      width = 5;
      height = 5;
    }

    return new Dimension(width, height);
  }

  public void render(int x, int y, int width, int height, Item hoveredItem, EntryAppearance appearance, Graphics g)
  {
    if (hoveredItem == this)
    {
      g.setColor(appearance.getHoverColor());
      appearance.getHoverUnderlay().paint(g, x, y, width, height);
      g.setColor(appearance.getColor());

      //      g.setColor(Color.RED);
      //      g.drawFilledRectangle(x,y,width, height);
      //      g.setColor(appearance.getColor());
    }

    if (this.isSelected())
    {
      appearance.getSelectionUnderlay().paint(g, x, y, width, height);
    }

    renderContent(x, y, width, height, hoveredItem, appearance, g);
  }

  protected void renderContent(int x, int y, int width, int height, Item hoveredItem, EntryAppearance appearance,
      Graphics g)
  {
    int itemWidth = 0;
    int itemHeight = 0;

    if (pixmap != null)
    {
      itemWidth = pixmap.getWidth();
      itemHeight = pixmap.getHeight();
      if (!textData.isEmpty())
        itemWidth += appearance.getGap();
    }
    else if (textData.isEmpty())
      return;

    if (!textData.isEmpty())
    {
      itemWidth += textData.getSize().getWidth();
      itemHeight = Math.max(itemHeight, textData.getSize().getHeight());
    }

    int localX = x + appearance.getAlignment().alignX(width, itemWidth);

    if (pixmap != null)
    {
      g.setColor(Color.WHITE);
      int localY = y + appearance.getAlignment().alignY(itemHeight, pixmap.getHeight());
      g.drawImage(pixmap, localX, localY);
      localX += pixmap.getWidth() + appearance.getGap();
    }

    if (!textData.isEmpty())
    {
      int localY = y + appearance.getAlignment().alignY(itemHeight, textData.getSize().getHeight())
          + textData.getSize().getHeight();
      textData.render(localX, localY, g, appearance);
    }
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {

  }

  /* (non-Javadoc)
   * @see org.fenggui.io.IOStreamSaveable#getUniqueName()
   */
  public String getUniqueName()
  {
    return GENERATE_NAME;
  }
}
