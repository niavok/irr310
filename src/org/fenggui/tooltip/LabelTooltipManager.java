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

import org.fenggui.IWidget;
import org.fenggui.Label;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class LabelTooltipManager extends Label implements ITooltipManager
{

  public final static String DATAKEY_LABELTOOLTIP = "DATAKEY_LabelTooltip";
  
  public LabelTooltipManager()
  {
    super();
  }

  public LabelTooltipManager(LabelTooltipManager widget)
  {
    super(widget);
  }

  public void showTooltip(IWidget widget)
  {
    ITooltipData tooltip = (ITooltipData) widget.getData(DATAKEY_LABELTOOLTIP);

    if (tooltip != null)
    {
      this.setText(tooltip.getTooltip());
      this.updateMinSize();
      this.layout();

      //set position
      int x = widget.getX() + widget.getSize().getWidth() - 10;
      int y = widget.getY() - 10;
      this.setXY(x, y);

      //show tooltip
      if (!this.isInWidgetTree())
        widget.getDisplay().addWidget(this);

    }
    else
    {
      //hide tooltip
      if (this.isInWidgetTree())
        this.removedFromWidgetTree();
    }

  }

  /* (non-Javadoc)
   * @see org.fenggui.Label#process(org.fenggui.theme.xml.InputOutputStream)
   */
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
  }

}
