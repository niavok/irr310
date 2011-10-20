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
 * Created on 29.01.2008
 * $Id$
 */
package org.fenggui.event.mouse;

/**
* @author ivicaz, last edited by $Author$, $Date$
* @version $Revision$
*/
public interface IMouseListener
{
  public void mouseDragged(MouseDraggedEvent mouseDraggedEvent);

  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent);

  public void mouseExited(MouseExitedEvent mouseExited);

  public void mouseMoved(MouseMovedEvent mouseMovedEvent);

  public void mousePressed(MousePressedEvent mousePressedEvent);

  public void mouseClicked(MouseClickedEvent mouseClickedEvent);

  public void mouseDoubleClicked(MouseDoubleClickedEvent mouseDoubleClickedEvent);

  public void mouseReleased(MouseReleasedEvent mouseReleasedEvent);

  public void mouseWheel(MouseWheelEvent mouseWheelEvent);
}
