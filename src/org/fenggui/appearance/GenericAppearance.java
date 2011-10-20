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
 * Created on Mar 3, 2009
 * $Id$
 */
package org.fenggui.appearance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.fenggui.StandardWidget;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Log;
import org.fenggui.util.Spacing;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public abstract class GenericAppearance extends DecoratorAppearance
{

  private Map<String, Pixmap>       pixmaps    = null;
  private Map<String, Color>        colors     = null;
  private Map<String, IntegerStore> integers   = null;
  private Map<String, Spacing>      spacings   = null;
  private Map<String, IDecorator>   decorators = null;
  private Map<String, Alignment>    alignments = null;

  /**
   * Internal Class to store integer values as XML elements
   * 
   * @author Marc Menghin, last edited by $Author$, $Date$
   * @version $Revision$
   */
  class IntegerStore implements IXMLStreamable
  {
    Integer value;

    public IntegerStore(Integer value)
    {
      this.value = value;
    }

    /* (non-Javadoc)
     * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
     */
    public String getUniqueName()
    {
      return IXMLStreamable.GENERATE_NAME;
    }

    /* (non-Javadoc)
     * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
     */
    public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
    {
      stream.processAttribute("Value", value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public IntegerStore clone()
    {
      IntegerStore result;
      try
      {
        result = (IntegerStore) super.clone();
      }
      catch (CloneNotSupportedException e)
      {
        //can not happen but write out anyway
        Log.error("Couldn't clone IntegerStore", e);
        return null;
      }
      result.value = new Integer(this.value.intValue());
      return result;
    }
  }

  /**
   * @param w
   */
  public GenericAppearance(StandardWidget w)
  {
    super(w);
  }

  /**
   * @param w
   * @param stream
   * @throws IOException
   * @throws IXMLStreamableException
   */
  public GenericAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super(w, stream);
  }

  /**
   * @param w
   * @param appearance
   */
  public GenericAppearance(StandardWidget w, DecoratorAppearance appearance)
  {
    super(w, appearance);
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    //create empty maps
    pixmaps = new HashMap<String, Pixmap>();
    colors = new HashMap<String, Color>();
    integers = new HashMap<String, IntegerStore>();
    spacings = new HashMap<String, Spacing>();
    decorators = new HashMap<String, IDecorator>();
    alignments = new HashMap<String, Alignment>();

    stream.processChildren("Pixmap", pixmaps, Pixmap.class);
    stream.processChildren("Color", colors, Color.class);
    stream.processChildren("Integer", integers, IntegerStore.class);
    stream.processChildren("Spacing", spacings, Spacing.class);
    stream.processChildren("Decorator", decorators, IDecorator.class);
    stream.processEnumChildren("Alignment", alignments, Alignment.class);
  }

  public Pixmap getPixmap(String key)
  {
    if (pixmaps == null)
      return null;
    else
      return pixmaps.get(key);
  }

  public void addPixmap(String key, Pixmap pixmap)
  {
    if (pixmaps == null)
      pixmaps = new HashMap<String, Pixmap>();
    pixmaps.remove(key);
    pixmaps.put(key, pixmap);
  }

  public Color getColor(String key)
  {
    if (colors == null)
      return null;
    else
      return colors.get(key);
  }

  public void addColor(String key, Color color)
  {
    if (colors == null)
      colors = new HashMap<String, Color>();
    colors.remove(key);
    colors.put(key, color);
  }

  public Integer getInteger(String key)
  {
    if (integers == null)
      return null;
    else
    {
      IntegerStore store = integers.get(key);
      if (store != null)
        return integers.get(key).value;
      else
        return null;
    }
  }

  public void addInteger(String key, Integer integer)
  {
    if (integers == null)
      integers = new HashMap<String, IntegerStore>();

    integers.remove(key);
    integers.put(key, new IntegerStore(integer));
  }

  public Spacing getSpacing(String key)
  {
    if (spacings == null)
      return null;
    else
      return spacings.get(key);
  }

  public void addSpacing(String key, Spacing spacing)
  {
    if (spacings == null)
      spacings = new HashMap<String, Spacing>();

    spacings.remove(key);
    spacings.put(key, spacing);
  }

  public IDecorator getDecorator(String key)
  {
    if (decorators != null)
      return decorators.get(key);
    else
      return null;
  }

  public void addDecorator(String key, IDecorator decorator)
  {
    if (decorators == null)
      decorators = new HashMap<String, IDecorator>();

    decorators.remove(key);
    decorators.put(key, decorator);
  }

  public Alignment getAlignment(String key)
  {
    if (alignments != null)
      return alignments.get(key);
    else
      return null;
  }

  public void addAlignment(String key, Alignment decorator)
  {
    if (alignments == null)
      alignments = new HashMap<String, Alignment>();

    alignments.remove(key);
    alignments.put(key, decorator);
  }

  /* (non-Javadoc)
   * @see org.fenggui.appearance.DecoratorAppearance#clone(org.fenggui.StandardWidget)
   */
  @Override
  protected DecoratorAppearance clone(StandardWidget widget)
  {
    GenericAppearance result = (GenericAppearance) super.clone(widget);

    if (this.alignments != null)
    {
      result.alignments = new HashMap<String, Alignment>(this.alignments.size());
      for (Entry<String, Alignment> align : this.alignments.entrySet())
        result.alignments.put(align.getKey(), align.getValue());
    }

    if (this.decorators != null)
    {
      result.decorators = new HashMap<String, IDecorator>(this.decorators.size());
      for (Entry<String, IDecorator> align : this.decorators.entrySet())
        result.decorators.put(align.getKey(), align.getValue().copy());
    }

    if (this.spacings != null)
    {
      result.spacings = new HashMap<String, Spacing>(this.spacings.size());
      for (Entry<String, Spacing> align : this.spacings.entrySet())
        result.spacings.put(align.getKey(), align.getValue().clone());
    }

    if (this.colors != null)
    {
      result.colors = new HashMap<String, Color>(this.colors.size());
      for (Entry<String, Color> align : this.colors.entrySet())
        result.colors.put(align.getKey(), align.getValue().clone());
    }

    if (this.integers != null)
    {
      result.integers = new HashMap<String, IntegerStore>(this.integers.size());
      for (Entry<String, IntegerStore> align : this.integers.entrySet())
        result.integers.put(align.getKey(), align.getValue().clone());
    }

    if (this.pixmaps != null)
    {
      result.pixmaps = new HashMap<String, Pixmap>(this.pixmaps.size());
      for (Entry<String, Pixmap> align : this.pixmaps.entrySet())
        result.pixmaps.put(align.getKey(), align.getValue());
    }

    return result;
  }

}