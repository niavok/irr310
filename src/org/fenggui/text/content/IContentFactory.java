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
 * Created on 22.11.2007
 * $Id$
 */
package org.fenggui.text.content;

import java.util.List;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.text.ITextCursorRenderer;
import org.fenggui.text.content.part.AbstractContentPart;

/**
 * Factory which is responsible to map from the content to a displayable structure
 * using ContentParts. It is used by the AdvancedTextRenderer system. 
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IContentFactory
{
  /**
   * Returns an empty Line.
   * 
   * @return
   */
  public AbstractContentPart getEmptyContentPart(TextAppearance appearance);

  /**
   * Transforms a given content into its ContentParts.
   * 
   * @param content
   * @return
   */
  public List<AbstractContentPart> getContentParts(Object content, TextAppearance appearance);

  /**
   * Splits the given content into its lines. The resulting list container for each line
   * the object representing this line. No auto word-warping applied.
   * 
   * @param content
   * @return
   */
  public Object[] getContentLines(Object content);

  /**
   * This method is used to transform special objects like images into the representing
   * content format.
   * 
   * @param obj
   * @return
   */
  public Object getContentObject(Object obj);

  /**
   * Returns the TextCursor to use.
   * 
   * @return
   */
  public ITextCursorRenderer getTextCursorRenderer();

  public void addLineStart(StringBuilder result);

  public void addLineEnd(StringBuilder result);

  public void finalContent(StringBuilder result);
}
