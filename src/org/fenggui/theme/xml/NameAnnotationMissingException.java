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
 * $Id: ComboBox.java 116 2006-12-12 22:46:21Z schabby $
 */
package org.fenggui.theme.xml;

/**
 * @author Esa Tanskanen
 *
 */
@SuppressWarnings("serial")
public class NameAnnotationMissingException extends IXMLStreamableException
{

  public NameAnnotationMissingException(String message, IParseContext parseContext)
  {
    super(message, parseContext);
  }

  public NameAnnotationMissingException(String message, IParseContext parseContext, Throwable cause)
  {
    super(message, parseContext, cause);
  }

  public NameAnnotationMissingException(Throwable cause)
  {
    super(cause);
  }
}
