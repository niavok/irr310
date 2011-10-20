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
 * Created on Dec 7, 2006
 * $Id: EntryAppearance.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui.appearance;

import java.io.IOException;

import org.fenggui.DecoratorLayer;
import org.fenggui.List;
import org.fenggui.StandardWidget;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

/**
 * Appearance definition for the <code>List</code> widget.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-28 14:13:57 +0100 (Sa, 28 Mär 2009) $
 * @version $Revision: 614 $
 * @see List
 */
public class EntryAppearance extends LabelAppearance
{
  private int            rowHeight          = 10;
  private DecoratorLayer selectionUnderlay  = new DecoratorLayer();
  private DecoratorLayer mouseHoverUnderlay = new DecoratorLayer();
  private Color          hoverColor         = Color.BLACK;
  private Color          selectionColor     = Color.BLACK;
  private Color          color              = Color.BLACK;

  public EntryAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super(w, stream);
  }

  public EntryAppearance(StandardWidget w)
  {
    super(w);
  }

  /**
   * copy constructor
   * @param w
   * @param appearance
   */
  public EntryAppearance(StandardWidget w, EntryAppearance appearance)
  {
    super(w, appearance);
    this.hoverColor = new Color(appearance.hoverColor);
    this.selectionColor = new Color(appearance.selectionColor);
    this.color = new Color(appearance.color);
    this.rowHeight = appearance.getRowHeight();
    this.selectionUnderlay = new DecoratorLayer(appearance.getSelectionUnderlay());
    this.mouseHoverUnderlay = new DecoratorLayer(appearance.getHoverUnderlay());
  }

  public DecoratorLayer getSelectionUnderlay()
  {
    return selectionUnderlay;
  }

  public int getRowHeight()
  {
    return rowHeight;
  }

  public void setRowHeight(int rowHeight)
  {
    this.rowHeight = rowHeight;
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    selectionColor = stream.processChild("SelectionColor", selectionColor, Color.BLACK, Color.class);
    hoverColor = stream.processChild("HoverColor", hoverColor, Color.BLACK, Color.class);
    color = stream.processChild("Color", color, Color.BLACK, Color.class);

    selectionUnderlay = stream.processChild("SelectionUnderlay", selectionUnderlay, new DecoratorLayer(),
      DecoratorLayer.class);
    mouseHoverUnderlay = stream.processChild("HoverUnderlay", mouseHoverUnderlay, new DecoratorLayer(),
      DecoratorLayer.class);

    rowHeight = stream.processAttribute("rowHeight", rowHeight, 10);
  }

  public DecoratorLayer getHoverUnderlay()
  {
    return mouseHoverUnderlay;
  }

  public void setSelectionUnderlay(DecoratorLayer selectionUnderlay)
  {
    this.selectionUnderlay = selectionUnderlay;
  }

  public void setHoverUnderlay(DecoratorLayer mouseHoverUnderlay)
  {
    this.mouseHoverUnderlay = mouseHoverUnderlay;
  }

  public Color getHoverColor()
  {
    return hoverColor;
  }

  public void setHoverColor(Color hoverColor)
  {
    this.hoverColor = hoverColor;
  }

  public Color getSelectionColor()
  {
    return selectionColor;
  }

  public void setSelectionColor(Color selectionColor)
  {
    this.selectionColor = selectionColor;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  /* (non-Javadoc)
   * @see org.fenggui.appearance.LabelAppearance#clone(org.fenggui.StandardWidget)
   */
  @Override
  public LabelAppearance clone(StandardWidget widget)
  {
    EntryAppearance result = (EntryAppearance) super.clone(widget);
    
    result.hoverColor = this.hoverColor.clone();
    result.selectionColor = this.selectionColor.clone();
    result.color = this.color.clone();
    result.selectionUnderlay = this.getSelectionUnderlay().clone();
    result.mouseHoverUnderlay = this.getHoverUnderlay().clone();
    
    return result;
  }
}
