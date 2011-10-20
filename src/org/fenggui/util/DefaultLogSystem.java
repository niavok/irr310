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
 * Created on Mar 22, 2009
 * $Id$
 */
package org.fenggui.util;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public final class DefaultLogSystem implements ILogSystem
{

  /* (non-Javadoc)
   * @see org.fenggui.util.ILogSystem#debug(java.lang.String, java.lang.Object[])
   */
  public void debug(String debug, Object... vars)
  {
    System.out.println("Debug: " + String.format(debug, vars));
  }

  /* (non-Javadoc)
   * @see org.fenggui.util.ILogSystem#error(java.lang.String, java.lang.Object[])
   */
  public void error(String error, Object... vars)
  {
    System.err.println("Error: " + String.format(error, vars));

  }

  /* (non-Javadoc)
   * @see org.fenggui.util.ILogSystem#error(java.lang.String, java.lang.Throwable, java.lang.Object[])
   */
  public void error(String error, Throwable ex, Object... vars)
  {
    System.err.println("Error: " + String.format(error, vars));
    ex.printStackTrace();
  }

  /* (non-Javadoc)
   * @see org.fenggui.util.ILogSystem#warn(java.lang.String, java.lang.Object[])
   */
  public void warn(String warn, Object... vars)
  {
    System.out.println("Warn: " + String.format(warn, vars));
  }

}
