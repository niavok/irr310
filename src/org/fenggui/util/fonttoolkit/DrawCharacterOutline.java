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
 
 * Created on Jan 30, 2006
 * $Id: DrawCharacterOutline.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.util.fonttoolkit;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

/**
 * @todo the outliner does not work very well... #
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class DrawCharacterOutline extends RenderStage
{

  private Color color = null;

  public DrawCharacterOutline(Color c)
  {
    color = c;
  }

  public void renderChar(FontMetrics fontMetrics, BufferedImage image, char c, int safetyMargin)
  {
    Graphics2D g = (Graphics2D) image.getGraphics();
    GlyphVector v = fontMetrics.getFont().createGlyphVector(g.getFontRenderContext(), new char[] { c });
    Shape shape = v.getOutline(safetyMargin / 2, fontMetrics.getHeight() - safetyMargin / 2);
    g.setColor(color);
    g.draw(shape);
  }

}
