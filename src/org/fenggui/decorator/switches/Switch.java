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
 * Created on Dec 8, 2006
 * $Id: Switch.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.decorator.switches;

import java.io.IOException;

import org.fenggui.IWidget;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * 
 * @author marcmenghin, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public abstract class Switch implements IXMLStreamable
{
  private String  label             = "default";
  private boolean reactingOnEnabled = true;

  public Switch(String label)
  {
    this.label = label;
  }

  public Switch(Switch s)
  {
    this.label = s.label;
    this.reactingOnEnabled = s.reactingOnEnabled;
  }

  public String getLabel()
  {
    return label;
  }

  public boolean isReactingOnEnabled()
  {
    return reactingOnEnabled;
  }

  public abstract void setup(IWidget widget);

  /**
   * Copies the switch. Return null if this switch should not be copied in an appearance object.
   * @return Copy of this switch or null.
   */
  public abstract Switch copy();

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    label = stream.processAttribute("label", label, label);
    reactingOnEnabled = stream.processAttribute("reactingOnEnabled", reactingOnEnabled, reactingOnEnabled);
  }

  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  public void setReactingOnEnabled(boolean reactingOnEnabled)
  {
    this.reactingOnEnabled = reactingOnEnabled;
  }

}
