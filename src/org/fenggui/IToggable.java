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
 * $Id: IToggable.java 606 2009-03-13 14:56:05Z marcmenghin $
 */

package org.fenggui;

/**
 * Widget that can be used to toggle 
 * between a selected- and an unselected state.
 * This interface is used
 * to standarize the naming of methods in those Widgets in
 * question.<br/>
 * <br/>
 * Toggable Widgets usually allow to select out of a finite
 * set of choices where each Widget represents one choice.
 * The type of the set of choices can be used to specialise
 * this generic Widget in this type.
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public interface IToggable<E>
{

  /**
   * Returns whether the Widget is selected.
   * @return true if selected, false otherwise
   */
  public boolean isSelected();

  /**
   * Sets the selection of this Widget.
   * @param b true if Widget shall be selected, false otherwise
   */
  public E setSelected(boolean b);
}
