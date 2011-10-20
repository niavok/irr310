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
 * Created on Apr 29, 2005
 * $Id: Border.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.decorator.border;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Spacing;
import org.fenggui.util.Span;

/**
 * Base class for all borders.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public abstract class Border extends Spacing implements IDecorator
{

  private String  label   = "default";
  private boolean enabled = true;
  private Span    span    = Span.BORDER;

  /**
   * Every Background class must register itself to the type register
   * if it should be possible to be loaded using InputOutputStream
   */
  //public static final TypeRegister<Border> TYPE_REGISTER =
  //	new TypeRegister<Border>();

  public Border()
  {

  }

  public Span getSpan()
  {
    return span;
  }

  public void setSpan(Span span)
  {
    this.span = span;
  }

  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public String getLabel()
  {
    return label;
  }

  public Border(int top, int left, int right, int bottom)
  {
    super(top, left, right, bottom);
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    label = stream.processAttribute("label", label, "default");
    enabled = stream.processAttribute("enabled", enabled, true);
    span = (Span) stream.processEnum("span", span, Span.BORDER, Span.class, Span.STORAGE_FORMAT);
  }

  public abstract void paint(Graphics g, int localX, int localY, int width, int height);

  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.IDecorator#copy()
   */
  public IDecorator copy()
  {
    return null;
  }

  protected void copy(Border result)
  {
    result.setEnabled(this.isEnabled());
    result.setLabel(this.getLabel());
    result.setSpan(this.getSpan());
  }
}
