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
 */
package org.fenggui.text.content.factory.simple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Log;

/**
 * @author Marc Menghin
 * 
 */
public class TextStyle implements IXMLStreamable, Cloneable
{
  public final static String          DEFAULTSTYLEKEY = "default";

  private Map<String, TextStyleEntry> styles          = new HashMap<String, TextStyleEntry>();

  /**
   * 
   */
  public TextStyle()
  {
  }

  public TextStyle(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    this.process(stream);
  }

  public TextStyleEntry getTextStyleEntry(String key)
  {
    TextStyleEntry style = getTextStyle(key);

    if (style == null && TextStyleEntry.DEFAULTSTYLESTATEKEY.equals(key))
    {
      style = getTextStyle(TextStyleEntry.DEFAULTSTYLESTATEKEY);
    }

    if (style == null)
    {
      style = new TextStyleEntry();
      styles.put(TextStyleEntry.DEFAULTSTYLESTATEKEY, style);
    }

    return style;
  }

  private TextStyleEntry getTextStyle(String key)
  {
    return styles.get(key);
  }

  public void addStyle(String key, TextStyleEntry style)
  {
    styles.put(key, style);
  }

  public void removeStyle(String key)
  {
    styles.remove(key);
  }

  public void removeAllStyles()
  {
    styles.clear();
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
   */
  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
   */
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    stream.processChildren("Style", styles, TextStyleEntry.class);
  }

  @Override
  public TextStyle clone()
  {
    TextStyle result;
    try
    {
      result = (TextStyle) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      //can not happen but write out anyway
      Log.error("Couldn't clone TextStyle", e);
      return null;
    }

    for (Entry<String, TextStyleEntry> entry : this.styles.entrySet())
    {
      result.styles.put(entry.getKey(), entry.getValue().clone());
    }

    return result;
  }
}
