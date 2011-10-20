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
import org.fenggui.ObservableWidget;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public abstract class ObservableWidgetActor implements IActor
{

  protected abstract void hook(ObservableWidget widget);

  protected abstract void unhook(ObservableWidget widget);

  /* (non-Javadoc)
   * @see org.fenggui.actor.IActor#setup(org.fenggui.IWidget)
   */
  public void hook(IWidget widget)
  {
    if (widget instanceof ObservableWidget)
    {
      hook((ObservableWidget) widget);
    }
    else
    {
      throw new IllegalArgumentException("Only ObservalbeWidget widgets are supportet by this Actor.");
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.actor.IActor#unHook(org.fenggui.IWidget)
   */
  public void unHook(IWidget widget)
  {
    if (widget instanceof ObservableWidget)
    {
      unhook((ObservableWidget) widget);
    }
    else
    {
      throw new IllegalArgumentException("Only ObservalbeWidget widgets are supportet by this Actor.");
    }
  }
}
