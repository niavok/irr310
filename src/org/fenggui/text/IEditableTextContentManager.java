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
package org.fenggui.text;

import java.util.Set;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.key.Key;
import org.fenggui.text.content.ContentManager;

/**
 * Data object interface for use with the IAdvancedTextRenderer.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IEditableTextContentManager extends ITextContentManager
{
  public enum MoveDirection
  {
    Up, Left, Right, Down
  }

  public void clickedOn(int x, int y, Set<Key> modifiers, TextAppearance appearance);

  public void dragedTo(int x, int y, Set<Key> modifiers, TextAppearance appearance);

  public int getMaxLines();

  public void setMaxLines(int lines);

  public void addContentAtEnd(Object content, TextAppearance appearance);

  public void addContentAtBeginning(Object content, TextAppearance appearance);

  public String getSelectedContent();

  public int getContentLineCount();

  public void removeContentLineFromEnd();

  public void removeContentLineFromBeginning();

  public boolean isEditMode();

  public void setEditMode(boolean editMode);

  public boolean addMinSizeChangedListener(ISizeChangedListener listener);

  public boolean removeMinSizeChangedListener(ISizeChangedListener listener);

  public boolean handleKeyPresses(Key key, Set<Key> modifiers, TextAppearance appearance);

  public void setReadonly(boolean readonly);

  public boolean isReadonly();

  public ContentManager getManager();

  public int getActivePositionIndex();

  public void setActivePositionIndex(int index);

  public boolean hasSelection();

  public int getSelectionStartIndex();

  public void setSelectionIndex(int index1, int index2, TextAppearance appearance);

  public int getSelectionEndIndex();

  /**
   * Checks if this is a valid character. A valid character is one where a representation
   * of that character can be drawn.
   * 
   * @param data
   * @param c
   * @return
   */
  public boolean isValidChar(char c, TextAppearance appearance);

  public boolean handleTextInput(char character, TextAppearance appearance);

  /**
   * @param listener
   * @return
   * @see org.fenggui.text.content.ContentManager#addSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public boolean addSizeChangedListener(ISizeChangedListener listener);

  /**
   * @param listener
   * @return
   * @see org.fenggui.text.content.ContentManager#removeSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public boolean removeSizeChangedListener(ISizeChangedListener listener);
}
