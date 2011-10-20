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
 * Created on 20.11.2007
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.IWidget;

/**
 * An actor is able to add behavior to a widget. It does this by using the
 * events a widget provides. 
 *  
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IActor
{

  /**
   * Assigns the actor to a widget. Normally an actor will hook into the events
   * of an widget and perform some action as they fire.
   * 
   * <p>
   * NOTE: An actors setup method should only be called with one widget.
   * Calling it again with an other widget will lead to unexpected behavior.
   * </p>
   * @param widget the widget the actor should be assigned to.
   */
  public void hook(IWidget widget);

  /**
   * Remove the actor from the assigned widget.
   */
  public void unHook(IWidget widget);
}
