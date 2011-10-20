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
 * Created on Jan 18, 2007
 * $Id: DecoratorLayer.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.binding.render.Graphics;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Log;

/**
 * Class to make decorators usable for overlay effects such as selections bars in menus und
 * tables.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-28 14:13:57 +0100 (Sa, 28 Mär 2009) $
 * @version $Revision: 614 $
 */
public class DecoratorLayer implements IXMLStreamable, Cloneable
{
  private java.util.List<IDecorator> decorators = new ArrayList<IDecorator>();

  public DecoratorLayer(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  public DecoratorLayer(IDecorator d)
  {
    decorators.add(d);
  }

  public DecoratorLayer(IDecorator... array)
  {
    for (IDecorator d : array)
      decorators.add(d);
  }

  public DecoratorLayer(java.util.List<IDecorator> list)
  {
    decorators.addAll(list);
  }

  /**
   * copy constructor
   * 
   * @param layer
   */
  public DecoratorLayer(DecoratorLayer layer)
  {
    for (IDecorator deco : layer.decorators)
    {
      decorators.add(deco);
    }
  }

  public void paint(Graphics g, int x, int y, int width, int height)
  {
    for (IDecorator deco : decorators)
    {
      if (!deco.isEnabled())
        continue;

      deco.paint(g, x, y, width, height);
    }
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    stream.processChildren(decorators, XMLTheme.TYPE_REGISTRY);
  }

  public void add(IDecorator... array)
  {
    for (IDecorator d : array)
      decorators.add(d);
  }

  public void clear()
  {
    decorators.clear();
  }

  /* (non-Javadoc)
   * @see org.fenggui.io.IOStreamSaveable#getUniqueName()
   */
  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  public DecoratorLayer clone()
  {
    DecoratorLayer result;
    try
    {
      result = (DecoratorLayer) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      //can not happen but write out anyway
      Log.error("Couldn't clone DecoratorLayer", e);
      return null;
    }

    //Decorators can't be changed so just add all to this layer
    result.decorators = new ArrayList<IDecorator>(decorators.size());
    result.decorators.addAll(decorators);
    return result;
  }
}
