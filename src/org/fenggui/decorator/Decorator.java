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
 * Created on 05.11.2007
 * $Id$
 */
package org.fenggui.decorator;

import java.io.IOException;

import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Span;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public abstract class Decorator implements IDecorator
{
  private String  label   = "default";
  private boolean enabled = true;
  private Span    span    = Span.BORDER;

  public Decorator()
  {

  }

  public Decorator(String label, Span span, boolean enabled)
  {
    this.label = label;
    this.span = span;
    this.enabled = enabled;
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.IDecorator#copy()
   */
  public IDecorator copy()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.IDecorator#getLabel()
   */
  public String getLabel()
  {
    return label;
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.IDecorator#getSpan()
   */
  public Span getSpan()
  {
    // TODO Auto-generated method stub
    return span;
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.IDecorator#isEnabled()
   */
  public boolean isEnabled()
  {
    // TODO Auto-generated method stub
    return enabled;
  }

  /* (non-Javadoc)
   * @see org.fenggui.decorator.IDecorator#setEnabled(boolean)
   */
  public void setEnabled(boolean enable)
  {
    this.enabled = enable;
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
    span = (Span) stream.processEnum("span", span, span, Span.class, Span.STORAGE_FORMAT);
    label = stream.processAttribute("label", label, "default");
    enabled = stream.processAttribute("enabled", enabled, true);
  }

  public void setSpan(Span span)
  {
    this.span = span;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  protected void copy(Decorator result)
  {
    result.setEnabled(this.isEnabled());
    result.setLabel(this.getLabel());
    result.setSpan(this.getSpan());
  }
}
