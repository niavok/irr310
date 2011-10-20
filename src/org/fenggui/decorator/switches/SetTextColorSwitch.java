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
 * Created on Dec 12, 2006
 * $Id: SetTextColorSwitch.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.decorator.switches;

import java.io.IOException;

import org.fenggui.IWidget;
import org.fenggui.StandardWidget;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

/**
 * 
 * @author marcmenghin, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class SetTextColorSwitch extends Switch
{
  private Color c = null;

  public SetTextColorSwitch(String label, Color colorToSet)
  {
    super(label);
    c = colorToSet;
  }

  @Override
  public void setup(IWidget w)
  {
    // kinda ugly casting, I know. But I think this should work in 99% of all cases.
    ((TextAppearance) ((StandardWidget) w).getAppearance()).getStyle(TextStyle.DEFAULTSTYLEKEY).getTextStyleEntry(
      TextStyleEntry.DEFAULTSTYLESTATEKEY).setColor(c);
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    c = (Color) stream.processChild(c, XMLTheme.TYPE_REGISTRY);
  }

  @Override
  public SetTextColorSwitch copy()
  {
    return new SetTextColorSwitch(this.getLabel(), c);
  }

}
