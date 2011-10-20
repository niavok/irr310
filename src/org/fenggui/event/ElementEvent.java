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
 * Created on 21.03.2008
 * $Id$
 */
package org.fenggui.event;

import org.fenggui.event.key.KeyEvent;
import org.fenggui.event.mouse.MouseEvent;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ElementEvent<T> extends Event
{
  private T          element;
  private MouseEvent mouseEvent = null;
  private KeyEvent   keyEvent   = null;

  /**
   * 
   */
  public ElementEvent(T element, KeyEvent keyEvent)
  {
    this.element = element;
    this.keyEvent = keyEvent;
  }

  public ElementEvent(T element, MouseEvent mouseEvent)
  {
    this.element = element;
    this.mouseEvent = mouseEvent;
  }

  public T getElement()
  {
    return element;
  }

  public boolean isMouseEvent()
  {
    return mouseEvent != null;
  }

  public boolean isKeyEvent()
  {
    return keyEvent != null;
  }

  public KeyEvent getKeyEvent()
  {
    return keyEvent;
  }

  public MouseEvent getMouseEvent()
  {
    return mouseEvent;
  }
}
