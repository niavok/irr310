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
package org.fenggui.text.content;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.util.Dimension;

public interface IContent<T>
{
  /**
   * Adds a character at the selection start index. If a selection is present it will be
   * removed.
   * 
   * @param c
   * @return true if the character could be added.
   */
  public abstract boolean addChar(char c, TextAppearance appearance);

  /**
   * Adds content to the active position in this line.
   * 
   * @param content Content to add
   * @param factory the factory to use
   * @return true if content could be added, false otherwise.
   */
  public abstract void addContent(Object content, int width, boolean wordwarp, IContentFactory factory,
      TextAppearance appearance);

  public abstract boolean addContentAtEnd(Object content, int width, boolean wordwarp, IContentFactory factory,
      TextAppearance appearance);

  public abstract boolean addContentAtBegining(Object content, int width, boolean wordwarp, IContentFactory factory,
      TextAppearance appearance);

  public abstract void getContent(StringBuilder result);

  public abstract void getSelectedContent(StringBuilder result);

  /**
   * The amount of atomic elements in this object.
   * 
   * @return Returns the atomCount.
   */
  public abstract int getAtomCount();

  public abstract Dimension getSize();

  /**
   * Merges the given ContentLine into this ContentLine. The new content
   * will be added at the end of this line.
   * @param line
   * 
   * will merge the given userline with this userline. The given userline will be added
   * at the end of this userline.
   */
  public abstract void mergeContent(T line);

  /**
   * Optimizes the containing content parts by trying to merge parts together and
   * removing empty parts. This should not change anything on the parts itself.
   * 
   * This method optimizes the parts of this ContentUserLine. After the optimization the
   * ContentUserLine is one line height (so word-warping is removed) and contains the
   * minimal amount of content parts possible.
   */
  public abstract void optimizeContent(TextAppearance appearance);

  /**
   * Removes all content from this Content object.
   */
  public abstract void removeAll(IContentFactory factory, TextAppearance appearance);
}