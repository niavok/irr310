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
 * $Id: IListModel.java 602 2009-03-13 11:26:38Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.appearance.EntryAppearance;

/**
 * @author Marc Menghin, last edited by $Author: marcmenghin $, $LastChangedDate: 2009-03-13 12:26:38 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 602 $
 */
public interface IListModel extends IModel
{
  public int getSize();

  public Item getItem(int index, EntryAppearance appearance);
}
