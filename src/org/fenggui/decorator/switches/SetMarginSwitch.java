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
 * Created on 26.09.2007
 * $Id$
 */
package org.fenggui.decorator.switches;

import org.fenggui.IWidget;
import org.fenggui.StandardWidget;
import org.fenggui.appearance.IAppearance;
import org.fenggui.appearance.SpacingAppearance;
import org.fenggui.util.Spacing;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class SetMarginSwitch extends Switch
{

  private int     upDown         = 0;
  private int     leftRight      = 0;
  private Spacing defaultSpacing = new Spacing(2, 2, 2, 2);

  /**
   * Creates a new SetMarginSwitch instance.
   * 
   * @param label label of switch.
   * @param defaultSpace The default spacing to use for Margin.
   * @param upDown the value to change the margin on top and bottom.
   * @param leftRight the value to change the margin on left and right.
   */
  public SetMarginSwitch(String label, Spacing defaultSpace, int upDown, int leftRight)
  {
    super(label);
    this.upDown = upDown;
    this.leftRight = leftRight;
    this.defaultSpacing = defaultSpace;
  }

  @Override
  public void setup(IWidget widget)
  {
    if (widget instanceof StandardWidget)
    {
      StandardWidget stdWid = (StandardWidget) widget;

      IAppearance appearance = stdWid.getAppearance();
      if (appearance instanceof SpacingAppearance)
      {
        SpacingAppearance spaceApp = (SpacingAppearance) appearance;
        spaceApp.setMargin(new Spacing(defaultSpacing.getTop() + upDown, defaultSpacing.getLeft() + leftRight,
            defaultSpacing.getRight() - leftRight, defaultSpacing.getBottom() - upDown));
      }
    }
  }

  @Override
  public SetMarginSwitch copy()
  {
    return new SetMarginSwitch(this.getLabel(), defaultSpacing, upDown, leftRight);
  }

}
