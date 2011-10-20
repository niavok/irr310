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
 * Created on Nov 10, 2006
 * $Id: IBasicContainer.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.theme.xml.IXMLStreamable;

/**
 * Meant for widgets that contain a fixed number of other widgets and layout
 * them according to a fixed rule.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 * @dedication Frank Sinatra - Fly Me To The Moon
 */
public interface IBasicContainer extends IWidget, IXMLStreamable
{
  public IWidget getNextWidget(IWidget start);

  public IWidget getPreviousWidget(IWidget start);

  public IWidget getNextTraversableWidget(IWidget start);

  public IWidget getPreviousTraversableWidget(IWidget start);

  public boolean isKeyTraversalRoot();

  public void layout();

}
