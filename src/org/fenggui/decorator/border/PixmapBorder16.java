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
 * $Id: PixmapBorder16.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.decorator.border;

import java.io.IOException;
import java.util.List;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

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
 * Pixmaps number 2, 7, 8 and D are scaled in the x-axis (2, D) or in the y-axis (7,8) in
 * order to create arbitrary sized borders. One limitation with this system is that it is
 * very difficult to create a border around an object that is smaller than 4w by 4h in
 * size (where w,h are the width and height of the pixmap components). This could be fixed
 * by scaling the border down for small sizes.<br/> <br/> In an alternative mode of
 * operation, the pixmaps that make up an edge are merged. The three pixmaps that make up
 * one edge are regarded as one pixmap that is strechted accordingly. Concretely, that
 * means that the pixmaps 5, 7, 9 are drawn as one as well as 6, 8, A and 1, 2, 3 and C,
 * D, E.
 * 
 * @author Graham Briggs, last edited by $Author: marcmenghin $, $Date: 2007-11-08
 *         11:50:01 +0100 (Do, 08 Nov 2007) $
 * @version $Revision: 606 $
 */
public class PixmapBorder16 extends Border
{
  public static final int TOP_LEFT                = 0;
  public static final int UPPER_TOP_LEFT_JUNC     = 1;
  public static final int TOP                     = 2;
  public static final int UPPER_TOP_RIGHT_JUNC    = 3;
  public static final int TOP_RIGHT               = 4;
  public static final int LOWER_TOP_LEFT_JUNC     = 5;
  public static final int LOWER_TOP_RIGHT_JUNC    = 6;
  public static final int LEFT                    = 7;
  public static final int RIGHT                   = 8;
  public static final int UPPER_BOTTOM_LEFT_JUNC  = 9;
  public static final int UPPER_BOTTOM_RIGHT_JUNC = 10;
  public static final int BOTTOM_LEFT             = 11;
  public static final int LOWER_BOTTOM_LEFT_JUNC  = 12;
  public static final int BOTTOM                  = 13;
  public static final int LOWER_BOTTOM_RIGHT_JUNC = 14;
  public static final int BOTTOM_RIGHT            = 15;

  /**
   * The additional border Pixmaps.
   */
  private Pixmap[]        tex                     = null;

  /**
   * The color that is set just before the pixmaps are drawn. This color is used to
   * modulate the color of the pixmaps
   */
  private Color           modulationColor         = Color.WHITE;

  /**
   * Creates a new PixmapBorder.
   * 
   * @param array
   *          Contains the pixmaps for this border in the proper order. According to the
   *          names of the pixmap in the above figure, the order is the following: 0, 1,
   *          2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F. In the alternative mode of
   *          operation the order of the pixmaps in the are is 0, 2, 4, 7, 8, B, D, F.
   * @throws IllegalArgumentException
   *           Thrown if the length of the array is not 16.
   */
  public PixmapBorder16(Pixmap[] array) throws IllegalArgumentException
  {

    // check mode of operation
    if (array.length == 16)
    {
      setSpacing(array[2].getHeight(), array[7].getWidth(), array[8].getWidth(), array[13].getHeight());
    }
    else
      throw new IllegalArgumentException("Wrong numbers of Pixmaps! 16 Pixmaps have to be specified, not "
          + array.length);

    tex = array;

  }

  /**
   * Creates a new PixmapBorder.
   * 
   * @param list
   *          Contains the pixmaps of this border in the proper order.
   * @throws IllegalArgumentException
   *           thrown if the length of the list is neither 8 nor 16
   */
  public PixmapBorder16(List<Pixmap> list) throws IllegalArgumentException
  {
    this(list.toArray(new Pixmap[list.size()]));
  }

  /**
   * Default constructor; used by the XML theme loading mechanism.
   * 
   */
  public PixmapBorder16()
  {

  }

  public PixmapBorder16(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  @Override
  public void paint(Graphics g, int localX, int localY, int width, int height)
  {
    g.setColor(modulationColor);

    g.drawImage(tex[UPPER_TOP_LEFT_JUNC], localX + getLeft(), localY + height - getTop());

    g.drawScaledImage(tex[TOP], localX + getLeft() + tex[UPPER_TOP_LEFT_JUNC].getWidth(), localY + height - getTop(),
      width - getLeft() - getRight() - tex[UPPER_TOP_LEFT_JUNC].getWidth() - tex[LOWER_TOP_RIGHT_JUNC].getWidth(),
      getTop());

    g.drawImage(tex[UPPER_TOP_RIGHT_JUNC], localX + width - getRight() - tex[UPPER_TOP_RIGHT_JUNC].getWidth(), localY
        + height - getTop());

    g.drawImage(tex[LOWER_TOP_LEFT_JUNC], localX, localY + height - getTop() - tex[LOWER_TOP_LEFT_JUNC].getHeight());

    g.drawImage(tex[LOWER_TOP_RIGHT_JUNC], localX + width - getRight(), localY + height - getTop()
        - tex[LOWER_TOP_RIGHT_JUNC].getHeight());

    g.drawScaledImage(tex[LEFT], localX, localY + getBottom() + tex[UPPER_BOTTOM_LEFT_JUNC].getHeight(), getLeft(),
      height - getBottom() - getTop() - tex[LOWER_TOP_LEFT_JUNC].getHeight() - tex[UPPER_BOTTOM_LEFT_JUNC].getHeight());

    g.drawScaledImage(tex[RIGHT], localX + width - getRight(), localY + getBottom()
        + tex[UPPER_BOTTOM_RIGHT_JUNC].getHeight(), getRight(), height - getBottom() - getTop()
        - tex[UPPER_BOTTOM_RIGHT_JUNC].getHeight() - tex[LOWER_TOP_RIGHT_JUNC].getHeight());

    g.drawImage(tex[UPPER_BOTTOM_LEFT_JUNC], localX, localY + getBottom());

    g.drawImage(tex[UPPER_BOTTOM_RIGHT_JUNC], localX + width - getRight(), localY + getBottom());

    // bottom left corner

    g.drawImage(tex[LOWER_BOTTOM_LEFT_JUNC], localX + getLeft(), localY);

    g.drawScaledImage(tex[BOTTOM], localX + getLeft() + tex[LOWER_BOTTOM_LEFT_JUNC].getWidth(), localY, width
        - getLeft() - getRight() - tex[LOWER_BOTTOM_LEFT_JUNC].getWidth() - tex[LOWER_BOTTOM_RIGHT_JUNC].getWidth(),
      getBottom());

    g.drawImage(tex[LOWER_BOTTOM_RIGHT_JUNC], localX - getRight() + width - tex[LOWER_BOTTOM_RIGHT_JUNC].getWidth(),
      localY);

    g.drawImage(tex[TOP_LEFT], localX, localY + height - getTop());
    g.drawImage(tex[BOTTOM_LEFT], localX, localY);
    g.drawImage(tex[TOP_RIGHT], localX + width - getRight(), localY + height - getTop());
    g.drawImage(tex[BOTTOM_RIGHT], localX + width - getRight(), localY);
  }

  /**
   * XML streaming not supported yet for PixmapBorde16
   */
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
    if (tex == null)
      tex = new Pixmap[16];
    tex[TOP_LEFT] = stream.processChild("TopLeftPixmap", tex[TOP_LEFT], Pixmap.class);
    tex[UPPER_TOP_LEFT_JUNC] = stream.processChild("UpperTopLeftJuncPixmap", tex[UPPER_TOP_LEFT_JUNC], Pixmap.class);
    tex[TOP] = stream.processChild("TopPixmap", tex[TOP], Pixmap.class);
    tex[UPPER_TOP_RIGHT_JUNC] = stream.processChild("UpperTopRightJuncPixmap", tex[UPPER_TOP_RIGHT_JUNC], Pixmap.class);
    tex[TOP_RIGHT] = stream.processChild("TopRightPixmap", tex[TOP_RIGHT], Pixmap.class);
    tex[LOWER_TOP_LEFT_JUNC] = stream.processChild("LowerTopLeftJuncPixmap", tex[LOWER_TOP_LEFT_JUNC], Pixmap.class);
    tex[LOWER_TOP_RIGHT_JUNC] = stream.processChild("LowerTopRightJuncPixmap", tex[LOWER_TOP_RIGHT_JUNC], Pixmap.class);
    tex[LEFT] = stream.processChild("LeftPixmap", tex[LEFT], Pixmap.class);
    tex[RIGHT] = stream.processChild("RightPixmap", tex[RIGHT], Pixmap.class);
    tex[UPPER_BOTTOM_LEFT_JUNC] = stream.processChild("UpperBottomLeftJuncPixmap", tex[UPPER_BOTTOM_LEFT_JUNC],
      Pixmap.class);
    tex[UPPER_BOTTOM_RIGHT_JUNC] = stream.processChild("UpperBottomRightJuncPixmap", tex[UPPER_BOTTOM_RIGHT_JUNC],
      Pixmap.class);
    tex[BOTTOM_LEFT] = stream.processChild("BottomLeftPixmap", tex[BOTTOM_LEFT], Pixmap.class);
    tex[LOWER_BOTTOM_LEFT_JUNC] = stream.processChild("LowerBottomLeftJuncPixmap", tex[LOWER_BOTTOM_LEFT_JUNC],
      Pixmap.class);
    tex[BOTTOM] = stream.processChild("BottomPixmap", tex[BOTTOM], Pixmap.class);
    tex[LOWER_BOTTOM_RIGHT_JUNC] = stream.processChild("LowerBottomRightJuncPixmap", tex[LOWER_BOTTOM_RIGHT_JUNC],
      Pixmap.class);
    tex[BOTTOM_RIGHT] = stream.processChild("BottomRightPixmap", tex[BOTTOM_RIGHT], Pixmap.class);

    setSpacing(tex[2].getHeight(), tex[7].getWidth(), tex[8].getWidth(), tex[13].getHeight());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.decorator.border.Border#copy()
   */
  @Override
  public IDecorator copy()
  {
    PixmapBorder16 result = new PixmapBorder16(tex);
    super.copy(result);
    return result;
  }

  protected void copy(PixmapBorder16 result)
  {
    super.copy(result);
    result.setModulationColor(modulationColor);
  }

  protected Pixmap[] getPixmaps()
  {
    return tex;
  }

  public Color getModulationColor()
  {
    return modulationColor;
  }

  public void setModulationColor(Color modulationColor)
  {
    this.modulationColor = modulationColor;
  }
}
