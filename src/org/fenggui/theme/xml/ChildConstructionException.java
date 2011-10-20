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
 * $Id: ChildConstructionException.java 606 2009-03-13 14:56:05Z marcmenghin $
 */

package org.fenggui.theme.xml;

/**
 * An exception thrown when the construction of a child object fails.
 * Primarly used as a wrapper exception for the numerious different
 * exceptions thrown by the reflection API.
 * 
 * @author Esa Tanskanen
 *
 */
@SuppressWarnings("serial")
public class ChildConstructionException extends IXMLStreamableException
{
  public ChildConstructionException(String message, IParseContext parseContext)
  {
    super(message, parseContext);
  }

  public ChildConstructionException(String message, IParseContext parseContext, Throwable cause)
  {
    super(message, parseContext, cause);
  }

  public ChildConstructionException(Throwable cause)
  {
    super(cause);
  }

  public static ChildConstructionException createMultipleDefinitionsException(String name, IParseContext parsingContext)
  {
    return new ChildConstructionException("multiple definitions for the element " + name + "\n\n", parsingContext);
  }
}
