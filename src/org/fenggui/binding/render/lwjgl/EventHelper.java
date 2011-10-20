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
 * Created on Jan 30, 2006
 * $Id: EventHelper.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.binding.render.lwjgl;

import org.fenggui.event.key.Key;
import org.fenggui.event.mouse.MouseButton;
import org.lwjgl.input.Keyboard;

/**
 * Helps to map events from LWJGL to FengGUI.
 * 
 * @todo Not all keys are correctly mapped yet!! #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class EventHelper
{

  public static MouseButton getMouseButton(int button)
  {
    switch (button)
    {
    case 0:
      return MouseButton.LEFT;
    case 2:
      return MouseButton.MIDDLE;
    case 1:
      return MouseButton.RIGHT;
    default:
      return MouseButton.LEFT;
    }
  }

  public static char mapKeyChar()
  {
    switch (Keyboard.getEventKey())
    {
    case Keyboard.KEY_SPACE:
      return ' ';
    default:
      return Keyboard.getEventCharacter();
    }
  }

  public static Key mapEventKey()
  {
    Key keyClass;

    switch (Keyboard.getEventKey())
    {
    case Keyboard.KEY_END:
      keyClass = Key.END;
      break;
    case Keyboard.KEY_HOME:
      keyClass = Key.HOME;
      break;
    case Keyboard.KEY_BACK:
      keyClass = Key.BACKSPACE;
      break;
    case Keyboard.KEY_SPACE:
      keyClass = Key.LETTER;
      break;
    case Keyboard.KEY_RETURN:
      keyClass = Key.ENTER;
      break;
    case Keyboard.KEY_ESCAPE:
      keyClass = Key.ESCAPE;
      break;
    case Keyboard.KEY_DELETE:
      keyClass = Key.DELETE;
      break;
    case Keyboard.KEY_UP:
      keyClass = Key.UP;
      break;
    case Keyboard.KEY_RIGHT:
      keyClass = Key.RIGHT;
      break;
    case Keyboard.KEY_LEFT:
      keyClass = Key.LEFT;
      break;
    case Keyboard.KEY_DOWN:
      keyClass = Key.DOWN;
      break;
    case Keyboard.KEY_SCROLL:
      keyClass = Key.SHIFT;
      break;
    case Keyboard.KEY_LMENU:
      keyClass = Key.ALT;
      break;
    case Keyboard.KEY_RMENU:
      keyClass = Key.ALT_GRAPH;
      break;
    case Keyboard.KEY_LCONTROL:
      keyClass = Key.CTRL;
      break;
    case Keyboard.KEY_RSHIFT:
      keyClass = Key.SHIFT;
      break;
    case Keyboard.KEY_LSHIFT:
      keyClass = Key.SHIFT;
      break;
    case Keyboard.KEY_RCONTROL:
      keyClass = Key.CTRL;
      break;
    case Keyboard.KEY_INSERT:
      keyClass = Key.INSERT;
      break;
    case Keyboard.KEY_TAB:
      keyClass = Key.TAB;
      break;
    case Keyboard.KEY_F12:
      keyClass = Key.F12;
      break;
    case Keyboard.KEY_F11:
      keyClass = Key.F11;
      break;
    case Keyboard.KEY_F10:
      keyClass = Key.F10;
      break;
    case Keyboard.KEY_F9:
      keyClass = Key.F9;
      break;
    case Keyboard.KEY_F8:
      keyClass = Key.F8;
      break;
    case Keyboard.KEY_F7:
      keyClass = Key.F7;
      break;
    case Keyboard.KEY_F6:
      keyClass = Key.F6;
      break;
    case Keyboard.KEY_F5:
      keyClass = Key.F5;
      break;
    case Keyboard.KEY_F4:
      keyClass = Key.F4;
      break;
    case Keyboard.KEY_LWIN:
    case Keyboard.KEY_RWIN:
      keyClass = Key.META;
      break;
    default:
      if ("1234567890".indexOf(Keyboard.getEventCharacter()) != -1)
      {
        keyClass = Key.DIGIT;
      }
      else
      {
        // @todo must not necessarily be a letter!! #
        keyClass = Key.LETTER;
      }
      break;
    }

    return keyClass;

  }

}
