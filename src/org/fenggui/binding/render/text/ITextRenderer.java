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
 * Created on Feb 23, 2007
 * $Id: ITextRenderer.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.binding.render.text;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IFont;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.ICopyable;

/**
 * Interface that abstracts different kinds of text renderers. Text renderes get
 * a string and a font and render the text in <code>render()</code>.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public interface ITextRenderer extends IXMLStreamable, ICopyable<ITextRenderer>
{
  public final static String        DEFAULTTEXTRENDERERKEY = "default";

  public final static ITextRenderer DEFAULTRENDERER        = new DirectTextRenderer();

  public void render(int x, int y, String[] text, Color color, Graphics g);

  public void render(int x, int y, String text, Color color, Graphics g);

  public Dimension calculateSize(String[] text);

  public int getLineHeight();

  public int getLineSpacing();

  public int getWidth(String text);

  public int getWidth(char text);

  public boolean isValidChar(char c);

  public void setFont(IFont font);

  public IFont getFont();
}
