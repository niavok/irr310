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
 * $Id: ProgressBar.java 611 2009-03-22 15:58:20Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.text.ITextContentManager;
import org.fenggui.text.TextContentManager;
import org.fenggui.util.Dimension;

/**
 * Horizontal progress bar Widget.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-22 16:58:20 +0100 (So, 22 Mär 2009) $
 * @version $Revision: 611 $
 */
public class ProgressBar extends StandardWidget
{

  private double                 value               = 0.5;
  private boolean                horizontal          = true;

  private EntryAppearance        appearance          = null;
  private ITextContentManager    textData            = null;

  protected ISizeChangedListener sizeChangedListener = new ISizeChangedListener()
                                                     {

                                                       public void sizeChanged(SizeChangedEvent event)
                                                       {
                                                         updateMinSize();
                                                       }

                                                     };

  public ProgressBar(String text)
  {
    this();
    setText(text);
  }

  public ProgressBar()
  {
    appearance = new EntryAppearance(this);
    textData = new TextContentManager();
    textData.addSizeChangedListener(sizeChangedListener);
    updateMinSize();
  }

  public double getValue()
  {
    return value;
  }

  public void setValue(double value)
  {
    if (value > 1)
      value = 1;
    if (value < 0)
      value = 0;
    this.value = value;
  }

  public void setHorizontal(boolean horizontal)
  {
    this.horizontal = horizontal;
  }

  public boolean isHorizontal()
  {
    return horizontal;
  }

  public void setText(String text)
  {
    textData.setContent(text, appearance);
    updateMinSize();
  }

  public EntryAppearance getAppearance()
  {
    return appearance;
  }

  @Override
  public Dimension getMinContentSize()
  {
    if (textData.isEmpty())
      return new Dimension(0, 0);
    else
      return new Dimension(textData.getSize());
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    if (horizontal)
    {
      appearance.getSelectionUnderlay().paint(g, 0, 0, (int) (appearance.getContentWidth() * this.getValue()),
        appearance.getContentHeight());
    }
    else
    {
      appearance.getSelectionUnderlay().paint(g, 0, 0, (int) (appearance.getContentWidth()),
        (int) (appearance.getContentHeight() * this.getValue()));
    }

    if (!textData.isEmpty())
    {
      Dimension size = textData.getSize();
      int x = org.fenggui.util.Alignment.MIDDLE.alignX(appearance.getContentWidth(), size.getWidth());
      int y = org.fenggui.util.Alignment.MIDDLE.alignY(appearance.getContentHeight(), size.getHeight())
          + size.getHeight();
      textData.render(x, y, g, appearance);
    }

  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  @Override
  public void sizeChanged(SizeChangedEvent event)
  {
    super.sizeChanged(event);
    textData.adaptChange(appearance.getContentWidth(), appearance.getContentHeight(), appearance);
  }
}
