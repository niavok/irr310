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

/**
 * A flow layout arranges components in a directional flow, much like lines of
 * text in a paragraph. 
 * Flow layouts are typically used to arrange buttons in a panel. It arranges
 * buttons horizontally until no more buttons fit on the same line. The line
 * alignment is determined by the <code>align</code> property. The possible
 * values are:
 * <ul>
 * <li>{@link #LEFT LEFT}
 * <li>{@link #RIGHT RIGHT}
 * <li>{@link #CENTER CENTER}
 * <li>{@link #LEADING LEADING}
 * <li>{@link #TRAILING TRAILING}
 * </ul>
 * <p>
 * A flow layout lets each component assume its natural (preferred) size.
 * </p>
 * 
 * @author raft
 */
public class FlowLayout extends LayoutManager
{

  /**
   * This value indicates that each row of components should be
   * left-justified.
   */
  public static final int LEFT     = 0;

  /**
   * This value indicates that each row of components should be centered.
   */
  public static final int CENTER   = 1;

  /**
   * This value indicates that each row of components should be
   * right-justified.
   */
  public static final int RIGHT    = 2;

  /**
   * This value indicates that each row of components should be justified to
   * the leading edge of the container's orientation, for example, to the left
   * in left-to-right orientations.
   */
  public static final int LEADING  = 3;

  /**
   * This value indicates that each row of components should be justified to
   * the trailing edge of the container's orientation, for example, to the
   * right in left-to-right orientations.
   */
  public static final int TRAILING = 4;

  /**
   * <code>newAlign</code> is the property that determines how each row
   * distributes empty space for the Java 2 platform, v1.2 and greater. It can
   * be one of the following three values:
   * <ul>
   * <code>LEFT</code>
   * <code>RIGHT</code>
   * <code>CENTER</code>
   * <code>LEADING</code>
   * <code>TRAILING</code>
   * </ul>
   * 
   * @serial
   * @see #getAlignment
   * @see #setAlignment
   */
  private int             align;

  /**
   * The flow layout manager allows a seperation of components with gaps. The
   * horizontal gap will specify the space between components and between the
   * components and the borders of the <code>Container</code>.
   * 
   * @serial
   * @see #getHgap()
   * @see #setHgap(int)
   */
  private int             hgap;

  /**
   * The flow layout manager allows a seperation of components with gaps. The
   * vertical gap will specify the space between rows and between the the rows
   * and the borders of the <code>Container</code>.
   * 
   * @serial
   * @see #getHgap()
   * @see #setHgap(int)
   */
  private int             vgap;

  /**
   * Constructs a new <code>FlowLayout</code> with a centered alignment and
   * a default 5-unit horizontal and vertical gap.
   */
  public FlowLayout()
  {
    this(CENTER, 5, 5);
  }

  /**
   * Constructs a new <code>FlowLayout</code> with the specified alignment
   * and a default 5-unit horizontal and vertical gap. The value of the
   * alignment argument must be one of <code>FlowLayout.LEFT</code>,
   * <code>FlowLayout.RIGHT</code>, <code>FlowLayout.CENTER</code>,
   * <code>FlowLayout.LEADING</code>, or <code>FlowLayout.TRAILING</code>.
   * 
   * @param align
   *            the alignment value
   */
  public FlowLayout(int align)
  {
    this(align, 5, 5);
  }

  /**
   * Creates a new flow layout manager with the indicated alignment and the
   * indicated horizontal and vertical gaps.
   * <p>
   * The value of the alignment argument must be one of
   * <code>FlowLayout.LEFT</code>, <code>FlowLayout.RIGHT</code>,
   * <code>FlowLayout.CENTER</code>, <code>FlowLayout.LEADING</code>, or
   * <code>FlowLayout.TRAILING</code>.
   * 
   * @param align
   *            the alignment value
   * @param hgap
   *            the horizontal gap between components and between the
   *            components and the borders of the <code>Container</code>
   * @param vgap
   *            the vertical gap between components and between the components
   *            and the borders of the <code>Container</code>
   */
  public FlowLayout(int align, int hgap, int vgap)
  {
    this.hgap = hgap;
    this.vgap = vgap;
    setAlignment(align);
  }

  /**
   * Gets the alignment for this layout. Possible values are
   * <code>FlowLayout.LEFT</code>, <code>FlowLayout.RIGHT</code>,
   * <code>FlowLayout.CENTER</code>, <code>FlowLayout.LEADING</code>, or
   * <code>FlowLayout.TRAILING</code>.
   * 
   * @return the alignment value for this layout
   * @see #setAlignment
   */
  public int getAlignment()
  {
    return align;
  }

  /**
   * Sets the alignment for this layout. Possible values are
   * <ul>
   * <li><code>FlowLayout.LEFT</code>
   * <li><code>FlowLayout.RIGHT</code>
   * <li><code>FlowLayout.CENTER</code>
   * <li><code>FlowLayout.LEADING</code>
   * <li><code>FlowLayout.TRAILING</code>
   * </ul>
   * 
   * @param align
   *            one of the alignment values shown above
   * @see #getAlignment()
   */
  public void setAlignment(int align)
  {
    this.align = align;
  }

  /**
   * Gets the horizontal gap between components and between the components and
   * the borders of the <code>Container</code>
   * 
   * @return the horizontal gap between components and between the components
   *         and the borders of the <code>Container</code>
   * @see #setHgap
   */
  public int getHgap()
  {
    return hgap;
  }

  /**
   * Sets the horizontal gap between components and between the components and
   * the borders of the <code>Container</code>.
   * 
   * @param hgap
   *            the horizontal gap between components and between the
   *            components and the borders of the <code>Container</code>
   * @see #getHgap
   */
  public void setHgap(int hgap)
  {
    this.hgap = hgap;
  }

  /**
   * Gets the vertical gap between components and between the components and
   * the borders of the <code>Container</code>.
   * 
   * @return the vertical gap between components and between the components
   *         and the borders of the <code>Container</code>
   * @see #setVgap
   */
  public int getVgap()
  {
    return vgap;
  }

  /**
   * Sets the vertical gap between components and between the components and
   * the borders of the <code>Container</code>.
   * 
   * @param vgap
   *            the vertical gap between components and between the components
   *            and the borders of the <code>Container</code>
   * @see #getVgap
   */
  public void setVgap(int vgap)
  {
    this.vgap = vgap;
  }

  /**
   * Centers the elements in the specified row, if there is any slack.
   * 
   * @param target
   *            the component which needs to be moved
   * @param x
   *            the x coordinate
   * @param y
   *            the y coordinate
   * @param width
   *            the width dimensions
   * @param height
   *            the height dimensions
   * @param rowStart
   *            the beginning of the row
   * @param rowEnd
   *            the the ending of the row
   */
  private void moveComponents(Container target, List<IWidget> content, int x, int y, int width, int height,
      int rowStart, int rowEnd, boolean ltr)
  {
    switch (align)
    {
    case LEFT:
      x += ltr ? 0 : width;
      break;
    case CENTER:
      x += width / 2;
      break;
    case RIGHT:
      x += ltr ? width : 0;
      break;
    case LEADING:
      break;
    case TRAILING:
      x += width;
      break;
    }
    for (int i = rowStart; i < rowEnd; i++)
    {
      IWidget m = content.get(i);
      if (m.isVisible())
      {
        Dimension d = m.getSize();
        if (ltr)
        {
          m.setX(x);
          m.setY(y + (height - d.getHeight()) / 2);
        }
        else
        {
          m.setX(target.getWidth() - x - d.getWidth());
          m.setY(y + (height - d.getHeight()) / 2);
        }
        x += d.getWidth() + hgap;
      }
    }
  }

  /**
   * Returns a string representation of this <code>FlowLayout</code> object
   * and its values.
   * 
   * @return a string representation of this layout
   */
  public String toString()
  {
    String str = "";
    switch (align)
    {
    case LEFT:
      str = ",align=left";
      break;
    case CENTER:
      str = ",align=center";
      break;
    case RIGHT:
      str = ",align=right";
      break;
    case LEADING:
      str = ",align=leading";
      break;
    case TRAILING:
      str = ",align=trailing";
      break;
    }
    return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + str + "]";
  }

  /**
   * Returns the minimum dimensions needed to layout the <i>visible</i>
   * components contained in the specified target container.
   * 
   * @param container
   *            the container that needs to be laid out
   * @return the minimum dimensions to lay out the subcomponents of the
   *         specified container
   */
  @Override
  public Dimension computeMinSize(List<IWidget> content)
  {
    int width = 0;
    int height = 0;

    //		for (int i = 0; i < content.size(); i++)
    //		{
    //			IWidget m = content.get(i);
    //			Dimension d = m.getMinSize();
    //			height = Math.max(height, d.getHeight());
    //			if (i > 0)
    //			{
    //				width += hgap;
    //			}
    //			width += d.getWidth();
    //		}
    //		// Insets insets = container.getInsets();
    //		// dim.width += insets.left + insets.right + hgap * 2;
    //		// dim.height += insets.top + insets.bottom + vgap * 2;
    //		width += hgap * 2;
    //		height += vgap * 2;
    Dimension dim = new Dimension(width, height);
    return dim;
  }

  @Override
  public void doLayout(Container container, List<IWidget> content)
  {
    // Insets insets = container.getInsets();
    // int maxwidth = container.width - (insets.left + insets.right + hgap *
    // 2);
    int maxwidth = container.getWidth() - (hgap * 2);
    int nmembers = content.size();
    // int x = 0, y = insets.top + vgap;
    int x = 0, y = vgap;
    int rowh = 0, start = 0;

    boolean ltr = true; // container.getComponentOrientation().isLeftToRight();

    for (int i = 0; i < nmembers; i++)
    {
      IWidget m = content.get(i);
      if (m.isVisible())
      {
        Dimension d = m.getMinSize();
        m.setSize(d);

        if ((x == 0) || ((x + d.getWidth()) <= maxwidth))
        {
          if (x > 0)
          {
            x += hgap;
          }
          x += d.getWidth();
          rowh = Math.max(rowh, d.getHeight());
        }
        else
        {
          // moveComponents(container, insets.left + hgap, y, maxwidth
          // - x, rowh, start, i, ltr);
          moveComponents(container, content, hgap, y, maxwidth - x, rowh, start, i, ltr);
          x = d.getWidth();
          y += vgap + rowh;
          rowh = d.getHeight();
          start = i;
        }
      }
    }
    // moveComponents(container, insets.left + hgap, y, maxwidth - x, rowh,
    // start, nmembers, ltr);
    moveComponents(container, content, hgap, y, maxwidth - x, rowh, start, nmembers, ltr);
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    hgap = stream.processAttribute("hgap", hgap);
    vgap = stream.processAttribute("vgap", vgap);
    align = stream.processAttribute("align", align);
  }

}