/*
 * FengGUI - Java GUIs in OpenGL (http://fenggui.dev.java.net)
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
 * Created on 20th October 2005
 * $Id: PixmapBorder16.java 390 2007-11-08 10:50:01Z marcmenghin $
 */
package org.fenggui.decorator.border;

import java.io.IOException;
import java.util.List;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;

/**
 * This border type uses 16 pixmaps to draw the border. The pixmaps are arranged as
 * follows:
 * 
 * <pre>
 *      TOP_LEFT-----UPPER_TOP_LEFT_JUNC-----TOP-----UPPER_TOP_RIGHT_JUNC-----TOP_RIGHT
 *          |                                                                    |
 * LOWER_TOP_LEFT_JUNC                                                 LOWER_TOP_RIGHT_JUNC
 *          |                                                                    |
 *        LEFT                                                                 RIGHT
 *          |                                                                    |
 * UPPER_BOTTOM_LEFT_JUNC                                            UPPER_BOTTOM_RIGHT_JUNC
 *          |                                                                    |
 *    BOTTOM_LEFT--LOWER_BOTTOM_LEFT_JUNC--BOTTOM--LOWER_BOTTOM_RIGHT_JUNC--BOTTOM_RIGHT  
 * </pre>
 * 
 * Pixmaps number 1, 3, 5, 6, 9, A, C and E are scaled in the x-axis (1, 3, C, E) or in
 * the y-axis (5, 6, 9, A) in order to create arbitrary sized borders. One limitation with
 * this system is that it is very difficult to create a border around an object that is
 * smaller than 4w by 4h in size (where w,h are the width and height of the pixmap
 * components). This could be fixed by scaling the border down for small sizes.<br/>
 * <br/> Based on the implementation of PixmapBorder16.
 * 
 * @author Marc Menghin, last edited by $Author: marcmenghin $, $Date: 2007-11-08 11:50:01
 *         +0100 (Do, 08 Nov 2007) $
 * @version $Revision: 390 $
 */
public class PixmapBorder16FixedCenters extends PixmapBorder16
{
  /**
   * Creates a new PixmapBorder.
   * 
   * @param array
   *          Contains the pixmaps for this border in the proper order. According to the
   *          names of the pixmap in the above figure, the order is the following: 0, 1,
   *          2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F.
   * @throws IllegalArgumentException
   *           Thrown if the length of the array not 16.
   */
  public PixmapBorder16FixedCenters(Pixmap[] array) throws IllegalArgumentException
  {
    super(array);
  }

  /**
   * Creates a new PixmapBorder.
   * 
   * @param list
   *          Contains the pixmaps of this border in the proper order.
   * @throws IllegalArgumentException
   *           thrown if the length of the array not 16.
   */
  public PixmapBorder16FixedCenters(List<Pixmap> list) throws IllegalArgumentException
  {
    this(list.toArray(new Pixmap[list.size()]));
  }

  public PixmapBorder16FixedCenters(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  /**
   * Default constructor; used by the XML theme loading mechanism.
   * 
   */
  public PixmapBorder16FixedCenters()
  {
  }

  @Override
  public void paint(Graphics g, int localX, int localY, int width, int height)
  {
    g.setColor(getModulationColor());

    Pixmap[] tex = this.getPixmaps();

    int spaceWidth = (int) Math.ceil((width - (tex[TOP].getWidth() + tex[TOP_LEFT].getWidth() + tex[TOP_RIGHT]
        .getWidth())) / 2.0d);
    int spaceHeight = (int) Math
        .ceil((height - (tex[TOP_LEFT].getHeight() + tex[LEFT].getHeight() + tex[UPPER_BOTTOM_RIGHT_JUNC].getHeight())) / 2.0d);

    // Top bar
    g.drawScaledImage(tex[UPPER_TOP_LEFT_JUNC], localX + getLeft(), localY + height - getTop(), spaceWidth, getTop());
    g.drawImage(tex[TOP], localX + getLeft() + spaceWidth, localY + height - getTop());
    g.drawScaledImage(tex[UPPER_TOP_RIGHT_JUNC], localX + width - getRight() - spaceWidth, localY + height - getTop(),
      spaceWidth, getTop());

    // Middle bar
    g.drawScaledImage(tex[LOWER_TOP_LEFT_JUNC], localX, localY + height - getTop() - spaceHeight, getLeft(),
      spaceHeight);
    g.drawScaledImage(tex[LOWER_TOP_RIGHT_JUNC], localX + width - getRight(), localY + height - getTop() - spaceHeight,
      getRight(), spaceHeight);
    g.drawImage(tex[LEFT], localX, localY + getBottom() + spaceHeight);
    g.drawImage(tex[RIGHT], localX + width - getRight(), localY + getBottom() + spaceHeight);
    g.drawScaledImage(tex[UPPER_BOTTOM_LEFT_JUNC], localX, localY + getBottom(), getLeft(), spaceHeight);
    g.drawScaledImage(tex[UPPER_BOTTOM_RIGHT_JUNC], localX + width - getRight(), localY + getBottom(), getRight(),
      spaceHeight);

    // Bottom bar
    g.drawScaledImage(tex[LOWER_BOTTOM_LEFT_JUNC], localX + getLeft(), localY, spaceWidth, getBottom());
    g.drawImage(tex[BOTTOM], localX + getLeft() + spaceWidth, localY);
    g.drawScaledImage(tex[LOWER_BOTTOM_RIGHT_JUNC], localX - getRight() + width - spaceWidth, localY, spaceWidth,
      getBottom());

    // Corners
    g.drawImage(tex[TOP_LEFT], localX, localY + height - getTop());
    g.drawImage(tex[BOTTOM_LEFT], localX, localY);
    g.drawImage(tex[TOP_RIGHT], localX + width - getRight(), localY + height - getTop());
    g.drawImage(tex[BOTTOM_RIGHT], localX + width - getRight(), localY);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.decorator.border.Border#copy()
   */
  @Override
  public IDecorator copy()
  {
    PixmapBorder16FixedCenters result = new PixmapBorder16FixedCenters(this.getPixmaps());
    super.copy(result);
    return result;
  }
}
