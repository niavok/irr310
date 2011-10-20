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
 * $Id: LabelAppearance.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui.appearance;

import java.io.IOException;

import org.fenggui.StandardWidget;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-28 14:13:57 +0100 (Sa, 28 Mär 2009) $
 * @version $Revision: 614 $
 * @dedication Frank Sinatra - Bad Leroy Brown
 */
public class LabelAppearance extends TextAppearance
{
  private int gap;

  public LabelAppearance(StandardWidget w)
  {
    super(w);
    gap = 5;
  }

  public LabelAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super(w, stream);
  }

  public LabelAppearance(StandardWidget w, LabelAppearance appearance)
  {
    super(w, appearance);

    this.gap = appearance.getGap();
  }

  public int getGap()
  {
    return gap;
  }

  public void setGap(int gap)
  {
    this.gap = gap;
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    gap = stream.processAttribute("gap", gap, 5);

  }

  /* (non-Javadoc)
   * @see org.fenggui.appearance.TextAppearance#clone(org.fenggui.StandardWidget)
   */
  @Override
  public LabelAppearance clone(StandardWidget widget)
  {
    return (LabelAppearance) super.clone(widget);
  }

}
