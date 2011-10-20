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
package org.fenggui.decorator.border;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fenggui.binding.render.Graphics;
import org.fenggui.decorator.IDecorator;

/**
 * 
 * Border that contains several other borders. Note that once you defined the <code>CompositeBorder</code>
 * you cannot add or remove borders because a border a read-only <code>Spacing</code>.
 * 
 * @author Esa Tanskanen and Johannes
 *
 */
public class CompositeBorder extends Border
{
  private List<Border> borders = null;

  public CompositeBorder()
  {
    this(new ArrayList<Border>());
  }

  public CompositeBorder(Border... borders)
  {
    this(Arrays.asList(borders));
  }

  public CompositeBorder(List<Border> list)
  {
    this.borders = new ArrayList<Border>();

    for (Border b : list)
      addBorder(b);
  }

  public void addBorder(Border b)
  {
    borders.add(b);

    setSpacing(getTop() + b.getTop(), getLeft() + b.getLeft(), getRight() + b.getRight(), getBottom() + b.getBottom());
  }

  public void addBorder(Border b, int index)
  {
    borders.add(index, b);

    setSpacing(getTop() + b.getTop(), getLeft() + b.getLeft(), getRight() + b.getRight(), getBottom() + b.getBottom());
  }

  public void replaceBorder(Border b, int index)
  {
    if (index >= size())
    {
      addBorder(b);
    }
    else
    {
      Border old = borders.get(index);

      setSpacing(getTop() + b.getTop() - old.getTop(), getLeft() + b.getLeft() - old.getLeft(), getRight()
          + b.getRight() - old.getRight(), getBottom() + b.getBottom() - old.getBottom());

      borders.set(index, b);
    }
  }

  public int size()
  {
    return borders.size();
  }

  @Override
  public void paint(Graphics g, int localX, int localY, int width, int height)
  {
    for (Border b : borders)
    {
      b.paint(g, localX, localY, width, height);

      localX += b.getLeft();
      localY += b.getBottom();

      width -= b.getLeftPlusRight();
      height -= b.getBottomPlusTop();
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.border.Border#copy()
   */
  @Override
  public IDecorator copy()
  {
    CompositeBorder result = new CompositeBorder(this.borders);
    super.copy(result);
    return result;
  }

}
