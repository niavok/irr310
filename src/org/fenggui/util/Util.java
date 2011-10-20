/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005-2008 FengGUI Project
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
 * Created on 20.04.2008
 * $Id$
 */
package org.fenggui.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class with helpful methods.
 * 
 * @author marc menghin
 * 
 */
public final class Util
{

  private Util()
  {
  }

  /**
   * Tries to create a instance of the given class over an empty constructor. Returns null
   * if it didn't find such a constructor of if it couldn't create a instance over it.
   * 
   * @param <T>
   * @param objClass
   * @return
   */
  public static <T> T createInstanceOfClass(Class<T> objClass)
  {
    T obj = null;
    
    try
    {
      Constructor<T> con = objClass.getConstructor();
      obj = con.newInstance();
    }
    catch (SecurityException e)
    {
      Log.error("", e);
    }
    catch (NoSuchMethodException e)
    {
      Log.error("Couldn't find empty Constructor for Object.", e);
    }
    catch (IllegalArgumentException e)
    {
      Log.error("", e);
    }
    catch (InstantiationException e)
    {
      Log.error("", e);
    }
    catch (IllegalAccessException e)
    {
      Log.error("", e);
    }
    catch (InvocationTargetException e)
    {
      Log.error("", e);
    }
    
    return obj;
  }
}
