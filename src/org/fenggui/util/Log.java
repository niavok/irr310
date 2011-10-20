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
public final class Log
{
  private static ILogSystem logger = new DefaultLogSystem();

  private Log()
  {
  }

  public static void error(String error, Object... vars)
  {
    logger.error(error, vars);
  }

  public static void error(String error, Throwable ex, Object... vars)
  {
    logger.error(error, ex, vars);

  }

  public static void warn(String warn, Object... vars)
  {
    logger.warn(warn, vars);
  }

  public static void debug(String debug, Object... vars)
  {
    logger.debug(debug, vars);
  }
  
  public static void setLogSystem(ILogSystem system)
  {
    logger = system;
  }
}
