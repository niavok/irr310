/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (C) 2005-2009 FengGUI Project
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
 * Created on Nov 9, 2006
 * $Id: IContainer.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui;

/**
 * A Container is a set of Widgets. The layout manager that is
 * assigned to the Container is responsible for
 * the size and position of its content. In terms of the tree
 * data structure, a container is a node with an arbitrary number
 * of child nodes. 
 */
public interface IContainer extends IBasicContainer
{

  /**
   * Add a widget at a specific position into the container.
   * 
   * @param w
   * @param position
   */
  public void addWidget(IWidget w, int position);

  /**
   * Add one or more widgets to the end of the container.
   * 
   * @param w
   */
  public void addWidget(IWidget... w);

  /**
   * Removes one or more widgets from the container.
   * @param c
   */
  public void removeWidget(IWidget... w);

  /**
   * Returns the children of this container.
   * @return the children Widgets
   */
  public java.util.List<IWidget> getContent();

  /**
   * Returns the amount of child widgets
   * 
   * @return
   */
  public int getChildWidgetCount();

  /**
   * Returns true if this container contains child widgets.
   * 
   * @return
   */
  public boolean hasChildWidgets();
}
