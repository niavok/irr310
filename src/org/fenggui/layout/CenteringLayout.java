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
package org.fenggui.layout;

import java.io.IOException;
import java.util.List;

import org.fenggui.Container;
import org.fenggui.IWidget;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * Takes a single widget and centers it, horizontally and/or vertically, at its current size, within the
 * available space.
 * @author Sam Bayless
 *
 */
public class CenteringLayout extends LayoutManager
{

  public static enum CenterStyle
  {
    HORIZONTAL, VERTICAL, BOTH;
  }

  /**
   * The type of centering to provide - horizontal, vertical, or both.
   */
  private CenterStyle centerStyle;

  /**
   * The amount to shift the center of the widget by.
   * Can be positive or negative.
   * Only applied if centering horizontally (or both).
   */
  private int         xOffset = 0;

  /**
   * The amount to shift the center of the widget by.
   * Can be positive or negative.
   * Only applied if centering vertically (or both).
   */
  private int         yOffset = 0;

  public CenteringLayout()
  {
    this(CenterStyle.BOTH);
  }

  /**
   * A layout manager that just centers each widget.
   * @param centerStyle The type of centering to provide - horizontal, vertical, or both.
   */
  public CenteringLayout(CenterStyle centerStyle)
  {
    super();

    this.centerStyle = centerStyle;
    if (centerStyle == null)
      this.centerStyle = CenterStyle.BOTH;
  }

  @Override
  public Dimension computeMinSize(List<IWidget> content)
  {
    if (content.isEmpty())
      return new Dimension(0, 0);
    return (content.get(0).getMinSize());

  }

  @Override
  public void doLayout(Container container, List<IWidget> content)
  {
    if (content.isEmpty())
      return;

    int contentWidth = container.getAppearance().getContentWidth();
    int contentHeight = container.getAppearance().getContentHeight();

    int left = container.getAppearance().getLeftMargins();
    int bottom = container.getAppearance().getBottomMargins();

    for (IWidget widget : content)
    {

      int xPosition = 0;

      int yPosition = 0;

      if (centerStyle == CenterStyle.BOTH || centerStyle == CenterStyle.HORIZONTAL)
        xPosition = left + contentWidth / 2 - widget.getSize().getWidth() / 2 + getXOffset();

      if (centerStyle == CenterStyle.BOTH || centerStyle == CenterStyle.VERTICAL)
        yPosition = bottom + contentHeight / 2 - widget.getSize().getHeight() / 2 + getYOffset();

      widget.getPosition().setXY(xPosition, yPosition);
    }
  }

  /**
   * The amount to shift the center of the widget by.
   * Can be positive or negative.
   * Only applied if centering vertically (or both).
   */
  public int getYOffset()
  {
    return yOffset;
  }

  /**
   * The amount to shift the center of the widget by.
   * Can be positive or negative.
   * Only applied if centering vertically (or both).
   * @param offset the amount to offset the center by. Can be positive or negative.
   */
  public void setYOffset(int offset)
  {
    yOffset = offset;
  }

  /**
   * The amount to shift the center of the widget by.
   * Can be positive or negative.
   * Only applied if centering horizontally (or both).
   */
  public int getXOffset()
  {
    return xOffset;
  }

  /**
   * The amount to shift the center of the widget by.
   * Can be positive or negative.
   * Only applied if centering horizontally (or both).
   * @param offset the amount to offset the center by. Can be positive or negative.
   */
  public void setXOffset(int offset)
  {
    xOffset = offset;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    xOffset = stream.processAttribute("xOffset", xOffset);
    yOffset = stream.processAttribute("yOffset", yOffset);
  }

}