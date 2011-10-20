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
package org.fenggui.text;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Dimension;

/**
 * This stores the data a ComplexTextRenderer needs. Normally this object will hold the
 * Text, Colors and TextRenderers that are used. In most cases it will only be usable with
 * one specific complex TextRenderer.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface ITextContentManager extends IXMLStreamable
{
  /**
   * Sets the text that will be used. This text may be transformed depending on the
   * implementation.
   * 
   * @param text
   *          text to use.
   */
  public void setContent(String text, TextAppearance appearance);

  public void setContent(Object text, TextAppearance appearance);

  public String getContent();

  public void Update(TextAppearance appearance);
  
  public boolean isEmpty();

  /**
   * If false all Text after the first linebreak will be removed.
   * 
   * @return false if all text after the first linebreak should be removed.
   */
  public boolean isMultiline();

  /**
   * Sets if the text should only be used till the first linebreak. If false all text
   * after the linebreak will be removed. If this is false, word-warping still can be
   * used.
   * 
   * @param multiline
   */
  public void setMultiline(boolean multiline, TextAppearance appearance);

  /**
   * If true, text that is too long for a line will automatically be put on the next line.
   * 
   * @return true if too long text should be warped to the next line.
   */
  public boolean isWordWarping();

  /**
   * Sets that too long text should be put to the next line. This also works if multiline
   * is set to false.
   * 
   * @param warp
   *          if true all text that is too long for one line will be split so it fits into
   *          the line.
   */
  public void setWordWarping(boolean warp, TextAppearance appearance);

  /**
   * Should be called if the widget is resized or something on the appearance changes. This
   * will update the content to correctly reflect the changes.
   * 
   * @param width Max width of the content.
   * @param height Max height of the content.
   */
  public void adaptChange(int width, int height, TextAppearance appearance);

  /**
   * 
   * @param listener
   */
  public boolean addSizeChangedListener(ISizeChangedListener listener);

  /**
   * 
   * @param listener
   */
  public boolean removeSizeChangedListener(ISizeChangedListener listener);

  /**
   * Should return the size of the content (cached if possible).
   * 
   * @return size of the content
   */
  public Dimension getSize();

  /**
   * Renders the text.
   * 
   * @param x x-position to start rendering
   * @param y y-position to start rendering
   * @param w width of render area
   * @param h height of render area
   * @param g graphics object
   * @param gl opengl object
   */
  public void render(int x, int y, Graphics g, TextAppearance appearance);
}
