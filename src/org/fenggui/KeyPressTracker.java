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
 * Created on 29.01.2008
 * $Id$
 */
package org.fenggui;

import java.util.HashSet;
import java.util.Set;

import org.fenggui.event.key.Key;

/**
 * Instance of this class tracks states of modifier keys by listening keyup/keydown
 * listeners. Its purpose is to provide (stupid) mechanism which will at any time know if
 * some key is pressed or not.
 * 
 * @author ivicaz, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class KeyPressTracker
{
  private Set<Key> modifiers;

  public KeyPressTracker()
  {
    modifiers = new HashSet<Key>(10);
  }

  public boolean setModifierPressed(Key modifierPressed)
  {
    return modifiers.add(modifierPressed);
  }

  public void setModifierReleased(Key modifierPressed)
  {
    modifiers.remove(modifierPressed);
  }

  public boolean isPressed(Key key)
  {
    return modifiers.contains(key);
  }

  public Set<Key> getModifiers()
  {
    // TODO Expensive. Some other way to prevent accessors from modifying original set?
    // return new HashSet<Key>(modifiers);
    //Maybe this is not even a bad thing. I would say that if he wants to change it its bad that he can't. 
    return modifiers;
  }
}
