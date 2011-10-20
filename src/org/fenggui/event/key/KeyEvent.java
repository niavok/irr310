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
 * Created on Oct 8, 2006
 * $Id: KeyEvent.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.event.key;

import java.util.Set;

import org.fenggui.IWidget;
import org.fenggui.event.WidgetEvent;

public class KeyEvent extends WidgetEvent
{
  private char     key;
  private Key      keyClass;
  private Set<Key> modifiers;

  public KeyEvent(IWidget source, char key, Key keyClass, Set<Key> modifiers)
  {
    super(source);
    this.key = key;
    this.keyClass = keyClass;
    this.modifiers = modifiers;
  }

  public char getKey()
  {
    return key;
  }

  public Key getKeyClass()
  {
    return keyClass;
  }

  public boolean isPressed(Key key)
  {
    return modifiers.contains(key);
  }

  public Set<Key> getModifiers()
  {
    return modifiers;
  }
}
