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
 * Created on 26.11.2007
 * $Id$
 */
package org.fenggui.text.content;

import org.fenggui.appearance.TextAppearance;

/**
 * A Interface which defines methods for a selection
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IContentSelection
{

  /**
   * Returns true if this object has a selected part, false otherwise.
   * 
   * @return true if a selected part is within this object, false otherwise.
   */
  public abstract boolean hasSelection();

  /**
   * Returns the selection start within this object.
   * @return start position in atoms.
   */
  public abstract int getSelectionStart();

  /**
   * Returns the selection end within this object.
   * 
   * @return end position in atoms.
   */
  public abstract int getSelectionEnd();

  /**
   * Removes all selections from this object. No content is removed.
   */
  public abstract void clearSelection(TextAppearance appearance);

  /**
   * Removes everything that is selected including the content of this object.
   */
  public abstract void removeSelection(IContentFactory factory, TextAppearance appearance);

  /**
   * Selected the parts between the to atom positions.
   * 
   * @param start start position in atoms
   * @param end end position in atoms
   */
  public abstract void setSelection(int start, int end, TextAppearance appearance);

}