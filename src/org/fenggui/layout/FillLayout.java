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
 * Created on 2005-3-26
 * $Id: BorderLayout.java 434 2008-01-10 10:04:46Z marcmenghin $
 */
package org.fenggui.layout;

import java.io.IOException;
import java.util.List;

import org.fenggui.Container;
import org.fenggui.IWidget;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * This layout fills each child widget over the whole area.
 * 
 * @author Marc Menghin
 * 
 */
public class FillLayout extends LayoutManager
{

  /**
   * 
   */
  public FillLayout()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.layout.LayoutManager#computeMinSize(org.fenggui.Container,
   *      java.util.List)
   */
  @Override
  public Dimension computeMinSize(List<IWidget> content)
  {
    int height = 0;
    int width = 0;

    for (IWidget c : content)
    {
      int cHeight = c.getMinSize().getHeight();
      int cWidth = c.getMinSize().getWidth();
      if (cHeight > height)
        height = cHeight;
      if (cWidth > width)
        width = cWidth;
    }
    return new Dimension(width, height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.layout.LayoutManager#doLayout(org.fenggui.Container, java.util.List)
   */
  @Override
  public void doLayout(Container container, List<IWidget> content)
  {
    Dimension contentSize = new Dimension(container.getAppearance().getContentWidth(), container.getAppearance()
        .getContentHeight());
    for (IWidget c : content)
    {
      c.setSize(new Dimension(contentSize.getWidth(), contentSize.getHeight()));
      c.setPosition(new Point(0, 0));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
   */
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
  }

}
