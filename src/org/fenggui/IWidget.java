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
 * Created on Nov 12, 2006
 * $Id: IWidget.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.binding.render.Graphics;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.IPositionChangedListener;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.PositionChangedEvent;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.event.mouse.MouseDraggedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.event.mouse.MouseReleasedEvent;
import org.fenggui.event.mouse.MouseWheelEvent;
import org.fenggui.layout.ILayoutData;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * A IWidget interface provides a definition for objects that represent a Widget.
 * A Widget is the most basic entity of a GUI system. It is defined as a entity
 * with a rectangular area.
 *  
 * The minimum size of a widget is maintained by the widget itself and is
 * always kept up to date, while the actual size of the widget is usually set
 * by the layout manager. Alternatively, one can set the actual size of a widget
 * manually via the {@link IWidget#setSize(Dimension)}.
 */
public interface IWidget extends IXMLStreamable
{

  /**
   * Returns the layout data associated with this widget. LayoutManagers will use
   * this data to correctly layout the widget within the container.
   * 
   * @return Data the LayoutManager of the parent Container will use or <code>null</code> if none.
   */
  public ILayoutData getLayoutData();

  /**
   * Returns the parent container if there is one. Else it will return <code>null</code>. On the Display
   * class this will always return <code>null</code>.
   * 
   * @return parent container or <code>null</code>.
   */
  public IBasicContainer getParent();

  /**
   * The <code>clone</code> method creates a shallow copy of the Widget. The returned Widget will be
   * independent of the current cloned Widget. Usually only the Widget will be cloned not its content.
   *   
   * @return a clone of this instance.
   */
  public IWidget clone();

  /**
   * Called on the hovered widget when the mouse enters the Widget
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param mouseEnteredEvent event type
   */
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent);

  /**
   * Called on the hovered widget when the mouse exits the Widget
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param mouseExitedEvent event type
   */
  public void mouseExited(MouseExitedEvent mouseExitedEvent);

  /**
   * Called on the hovered widget when a mouse button is pressed.
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param mp event type
   */
  public void mousePressed(MousePressedEvent mp);

  /**
   * Called on the hovered widget when the mouse pointer moves over the widget.
   * Do not make computationally expensive things here because this
   * method is constantly called by Display
   * 
   * @todo evaluate the idea to introduce a flag for Containers that
   * indicate whether the Container holds mouse-over sensitive Widgets
   * to avoid fining the underlying Widget on every mouse move event. #
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param displayX the x coordinate of the mouse cursor in display
   * coordinates
   * @param displayY the y coordinate of the mouse cursor in display
   * coordinates
   */
  public void mouseMoved(int displayX, int displayY);

  /**
   * Called on the hovered widget when the mouse is dragged (moved while pressing a
   * mouse button down) over the Widget.
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @todo mouseDragged is actually only a special case of
   * mouseMoved. Consider to merge both events #
   * @param mp event type
   */
  public void mouseDragged(MouseDraggedEvent mp);

  /**
   * Called on the hovered widget when a previously pressed mouse button is released
   * on the Widget. It can not be expected that the mouse button was
   * pressed on this widget.
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param mr event type
   */
  public void mouseReleased(MouseReleasedEvent mr);

  /**
   * Called on the hovered widget when the user clicks one something. This event is raised
   * after a mousePressed and before a mouseReleased event occurs.
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param event event type
   */
  public void mouseClicked(MouseClickedEvent event);

  /**
   * Called on the hovered widget when the mouse button is pressed twice in a short time.
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param event event type
   */
  public void mouseDoubleClicked(MouseDoubleClickedEvent event);

  /**
   * Called on the hovered widget when there is a mouse wheel event.
   * 
   * <p>Note: This event will travel from the hovered widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param mouseWheelEvent event type
   */
  public void mouseWheel(MouseWheelEvent mouseWheelEvent);

  /**
   * Called on the focused widget when a key is pressed on the keyboard.
   * 
   * <p>Note: This event will travel from the focused widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param keyPressedEvent event type
   */
  public void keyPressed(KeyPressedEvent keyPressedEvent);

  /**
   * Called on the focused widget when a previously pressed key on the keyboard
   * is released.
   * 
   * <p>Note: This event will travel from the focused widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param keyReleasedEvent event type
   */
  public void keyReleased(KeyReleasedEvent keyReleasedEvent);

  /**
   * Called on the focused widget when a key is Typed. This event is raised after a
   * keyPressed event and before a keyReleased event.
   *  
   * <p>Note: This event will travel from the focused widget to the root of the visual
   * tree (usually the Display object).</p>
   * 
   * @param keyTypedEvent event type
   */
  public void keyTyped(KeyTypedEvent keyTypedEvent);

  /**
   * Called if the size changed.
   * 
   * @param event
   */
  public void sizeChanged(SizeChangedEvent event);

  /**
   * Called of the position changed.
   * 
   * @param event
   */
  public void positionChanged(PositionChangedEvent event);

  /**
   * Adds a listener to the sizeChanged event.
   * 
   * @param l
   */
  public void addSizeChangedListener(ISizeChangedListener l);

  /**
   * Removes a listener form the sizeChanged event.
   * 
   * @param l
   */
  public void removeSizeChangedListener(ISizeChangedListener l);

  /**
   * Adds a minSizeChanged listener.
   *  
   * @param l
   */
  public void addMinSizeChangedListener(ISizeChangedListener l);

  /**
   * Removes a minSizeChanged listener.
   * 
   * @param l
   */
  public void removeMinSizeChangedListener(ISizeChangedListener l);

  /**
   * Adds a listener to the positionChanged event.
   * 
   * @param l
   */
  public void addPositionChangedListener(IPositionChangedListener l);

  /**
   * Removes a listener from the positionChanged listener.
   * 
   * @param l
   */
  public void removePositionChangedListener(IPositionChangedListener l);

  /**
   * Returns the absolute position of this widget within the Display.
   *  
   * @return
   */
  public int getDisplayX();

  /**
   * Returns the absolute position of this widget within the Display.
   * 
   * @return
   */
  public int getDisplayY();

  /**
   * Returns the Display the Widget is in or <code>null</code> if its not added
   * to any Display.
   * 
   * @return
   */
  public Display getDisplay();

  /**
   * Returns the widget that is on the given location or <code>null</code> if none is found.
   * 
   * @param x local x value
   * @param y local y value
   * @return the widget that is on this location of <code>null</code> if none.
   */
  public IWidget getWidget(int x, int y);

  /**
   * Updates the minSize of the widget. Usually this should be handled by the Widgets themself.
   */
  public void updateMinSize();

  /**
   * Called when a widget gets or looses the focus.
   */
  public void focusChanged(FocusEvent focusEvent);

  /**
   * Returns the current size of the widget.
   * 
   * @return
   */
  public Dimension getSize();

  /**
   * Returns the current minSize of the widget.
   * 
   * @return
   */
  public Dimension getMinSize();

  /**
   * Returns the current X position of the widget.
   * 
   * @return
   */
  public int getX();

  /**
   * Returns the current Y position of the widget.
   * 
   * @return
   */
  public int getY();

  /**
   * Returns the position of the widget.
   * 
   * @return
   */
  public Point getPosition();

  /**
   * Sets the position of the widget.
   * 
   * @param p
   */
  public void setPosition(Point p);

  /**
   * Sets the X position of the widget.
   * 
   * @param x
   */
  public void setX(int x);

  /**
   * Sets the Y position of the widget.
   * 
   * @param y
   */
  public void setY(int y);

  /**
   * Returns true if the widget can be traversed to. 
   * 
   * @return
   */
  public boolean isTraversable();

  /**
   * This is called if the widget is removed from the widget Tree. 
   */
  public void removedFromWidgetTree();

  /**
   * Sets the parent of a widget.
   * 
   * @param object
   */
  public void setParent(IBasicContainer object);

  /**
   * This is called if the widget is added to the widget Tree. This indicated that calls
   * to getDisplay() should from now on not return null but the Display object.
   */
  public void addedToWidgetTree();

  /**
   * Returns whether this widget is registered in a widget tree.
   * 
   * @return true if registered, else otherwise
   */
  public boolean isInWidgetTree();

  /**
   * Layouts the widgets content.
   */
  public void layout();

  /**
   * Sets the size of the widget.
   * 
   * @param d
   */
  public void setSize(Dimension d);

  /**
   * Checks if the widget is set to be visible or not. Doesn't check if the
   * widget is actually drawn on screen or not.
   * 
   * @return true if the widget should be visible.
   */
  public boolean isVisible();

  /**
   * Sets the visibility state of the widget. If set to false the widget will
   * not be drawn and will not receive any events but takes its space (remains
   * in the widget-tree). Also all child elements will not be drawn or receive
   * any events.
   * 
   * @param visible True if the widget should be drawn, false otherwise.
   */
  public void setVisible(boolean visible);

  /**
   * Returns true if the widget is expandable (default), false otherwise.
   * LayoutManagers should respect this setting.
   * 
   * @return
   */
  public boolean isExpandable();

  /**
   * Returns true if the widget is shrinkable (default), false otherwise.
   * LayoutManagers should respect this setting.
   * 
   * @return
   */
  public boolean isShrinkable();

  /**
   * Draws the widget.
   * 
   * @param g
   */
  public void paint(Graphics g);

  /**
   * Set a custom data object to the widget. This is a method to associate arbitrary data
   * with a specific widget. 
   * 
   * @param data the data object
   * @see org.fenggui.util.Util
   */
  public void setData(String key, Object data);

  /**
   * Returns the data object associated with this widget.
   * 
   * @return a data object.
   * @see org.fenggui.util.Util
   */
  public Object getData(String key);
}