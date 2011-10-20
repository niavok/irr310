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
 * Created on 22.10.2007
 * $Id$
 */
package org.fenggui.tooltip;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.decorator.Decorator;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Span;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class LabelTooltipDecorator extends Decorator implements ITooltipData
{
  private String tooltip = "";

  public LabelTooltipDecorator(String label, boolean enabled, String tooltip)
  {
    super(label, Span.PADDING, enabled);
  }

  public LabelTooltipDecorator()
  {

  }

  public void paint(Graphics g, int localX, int localY, int width, int height)
  {
    //no painting here
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    setTooltip(stream.processAttribute("tooltip", getTooltip(), ""));
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }

  public LabelTooltipDecorator copy()
  {
    LabelTooltipDecorator result = new LabelTooltipDecorator();
    super.copy(result);
    result.setTooltip(this.getTooltip());
    return result;
  }
}
