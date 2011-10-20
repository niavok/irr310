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
 */
package org.fenggui.theme.xml;

import org.fenggui.util.jdom.Element;

/**
 * An implementation of {@link IParseContext} which builds its string
 * representation lazily. This allows us to avoid wasting time on an
 * expensive operation unless it is actually needed.
 */
public class LazyParseContext implements IParseContext
{
  private final Element elt;
  private final int     eltStartLine;
  private final int     pointerLine;

  public LazyParseContext(Element elt, int eltStartLine, int pointerLine)
  {
    this.elt = elt;
    this.eltStartLine = eltStartLine;
    this.pointerLine = pointerLine;
  }

  public String parseContextString()
  {
    StringBuilder eltStr = new StringBuilder();
    elt.toXML("", eltStr);
    return XMLProcessPointer.getParsingContext(eltStr.toString(), eltStartLine, pointerLine);
  }
}
