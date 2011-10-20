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
 * Created on 2005-3-2
 * $Id: Widget.java 616 2009-04-07 12:02:56Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;
import org.fenggui.util.Log;
import org.fenggui.util.Point;

/**
 * Implementation of a widget. A widget is the most basic unit of
 * a GUI system. 
 * <br/>
 * The background spans over the content, padding and border, but
 * not over the margin.<br/>
 * <br/>
 * The minimum size of a widget is maintained by the widget itself and is
 * always kept up to date, while the actual size of the widget is usually set by the layout manager.
 * Alternatively, one can set the actual size of a widget manually via the <code>setSize<method>.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-04-07 14:02:56 +0200 (Di, 07 Apr 2009) $
 * @version $Revision: 616 $
 * @dedication Billie Holiday - They Can't Take That Away from Me
 * @see org.fenggui.background.Background
 * @see org.fenggui.border.Border
  */
public class Widget implements IWidget
{

  private Dimension                           size;
  private Dimension                           minSize;

  private boolean                             shrinkable;
  private boolean                             expandable;
  private boolean                             visible;
  private Map<String, Object>                 data       = null;

  private ArrayList<ISizeChangedListener>     sizeHook;
  private ArrayList<ISizeChangedListener>     minSizeHook;
  private ArrayList<IPositionChangedListener> positionHook;

  /**
   * The parent Container in which the Widget lays.
   */
  private IBasicContainer                     parent     = null;

  /**
   * Position of the Widget in the coordinate system of the parent
   * Container.
   */
  private Point                               position;

  /**
   * Layout Data that is associated with this Widget. Layout data is
   * required by the layout managers to know how to layout a Widget.
   */
  private ILayoutData                         layoutData = null;

  /**
   * Creates a new widget.
   *
   */
  public Widget()
  {
    position = new Point(0, 0);
    size = new Dimension(10, 10);
    minSize = new Dimension(10, 10);
    shrinkable = true;
    expandable = true;
    visible = true;
    data = null;
    parent = null;
    layoutData = null;

    this.initHooks();
  }

  /**
   * Copy constructor. Should be overwritten by all implementing subclasses.
   * 
   * @param widget widget to copy.
   */
  public Widget(Widget widget)
  {
    this();
    if (widget != null)
    {
      position = new Point(widget.position);
      size = new Dimension(widget.size);
      minSize = new Dimension(widget.minSize);
      shrinkable = widget.shrinkable;
      expandable = widget.expandable;
      visible = widget.visible;
      //			TODO: copy this object aswell
      //			this.layoutData = widget.layoutData

      data = null;
      parent = null;
      layoutData = null;

      this.initHooks();
    }
  }

  private void initHooks()
  {
    sizeHook = new ArrayList<ISizeChangedListener>(0);
    minSizeHook = new ArrayList<ISizeChangedListener>(0);
    positionHook = new ArrayList<IPositionChangedListener>(0);
  }

  /**
   * Sets the LayoutData of this Widget. Note that the parameter is not
   * type safe. If you pass a wrong type, the LayoutManager may crash
   * with a ClassCashException.
   * 
   * @param layoutData layout data
   */
  public void setLayoutData(ILayoutData layoutData)
  {
    this.layoutData = layoutData;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getLayoutData()
   */
  public ILayoutData getLayoutData()
  {
    return layoutData;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getParent()
   */
  public IBasicContainer getParent()
  {
    return parent;
  }

  /**
   * Returns the minimum size of the widgets content. For this widget it's 0. 
   * 
   * @return
   */
  public Dimension getMinContentSize()
  {
    return new Dimension(0, 0);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
   */
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
  {
    if (!mouseEnteredEvent.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseEntered(mouseEnteredEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
   */
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    if (!mouseExitedEvent.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseExited(mouseExitedEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
   */
  public void mousePressed(MousePressedEvent mp)
  {
    if (!mp.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mousePressed(mp);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseMoved(int, int)
   */
  public void mouseMoved(int displayX, int displayY)
  {
    if (this.getParent() != null)
    {
      this.getParent().mouseMoved(displayX, displayY);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseDragged(org.fenggui.event.mouse.MouseDraggedEvent)
   */
  public void mouseDragged(MouseDraggedEvent mp)
  {
    if (!mp.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseDragged(mp);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseReleased(org.fenggui.event.mouse.MouseReleasedEvent)
   */
  public void mouseReleased(MouseReleasedEvent mr)
  {
    if (!mr.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseReleased(mr);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
   */
  public void mouseDoubleClicked(MouseDoubleClickedEvent event)
  {
    if (!event.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseDoubleClicked(event);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  public void mouseClicked(MouseClickedEvent event)
  {
    if (!event.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseClicked(event);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#mouseWheel(org.fenggui.event.mouse.MouseWheelEvent)
   */
  public void mouseWheel(MouseWheelEvent mouseWheelEvent)
  {
    if (!mouseWheelEvent.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().mouseWheel(mouseWheelEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#keyPressed(org.fenggui.event.KeyPressedEvent)
   */
  public void keyPressed(KeyPressedEvent keyPressedEvent)
  {
    if (!keyPressedEvent.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().keyPressed(keyPressedEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#keyReleased(org.fenggui.event.KeyReleasedEvent)
   */
  public void keyReleased(KeyReleasedEvent keyReleasedEvent)
  {
    if (!keyReleasedEvent.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().keyReleased(keyReleasedEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#keyTyped(org.fenggui.event.KeyTypedEvent)
   */
  public void keyTyped(KeyTypedEvent keyTypedEvent)
  {
    if (!keyTypedEvent.isAlreadyUsed() && (this.getParent() != null))
    {
      this.getParent().keyTyped(keyTypedEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#positionChanged(org.fenggui.event.PositionChangedEvent)
   */
  public void positionChanged(PositionChangedEvent event)
  {
    if (!this.isVisible() || !this.isInWidgetTree())
    {
      return;
    }

    for (IPositionChangedListener listener : positionHook)
    {
      listener.positionChanged(event);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#sizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  public void sizeChanged(SizeChangedEvent event)
  {
    if (!this.isVisible() || !this.isInWidgetTree())
    {
      return;
    }

    for (ISizeChangedListener listener : sizeHook)
    {
      listener.sizeChanged(event);
    }

  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#addSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public void addSizeChangedListener(ISizeChangedListener l)
  {
    sizeHook.add(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#removeSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public void removeSizeChangedListener(ISizeChangedListener l)
  {
    sizeHook.remove(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#addPositionChangedListener(org.fenggui.event.IPositionChangedListener)
   */
  public void addPositionChangedListener(IPositionChangedListener l)
  {
    positionHook.add(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#removePositionChangedListener(org.fenggui.event.IPositionChangedListener)
   */
  public void removePositionChangedListener(IPositionChangedListener l)
  {
    positionHook.remove(l);
  }

  /**
   * Returns whether this widget is registered in a widget tree.
   * @return true if registered, else otherwise
   */
  public boolean isInWidgetTree()
  {
    if (this.getDisplay() == null)
    {
      return false;
    }
    return true;
  }

  /**
   * Called when this widget is removed from the widget tree. This means
   * <ol>
   * <li>it does not receive event messages anymore,</li>
   * <li>it is not rendered anymore,</li>
   * <li>it is not layouted anymore by its parent container</li>
   * <ol>
   */
  public void removedFromWidgetTree()
  {
    // does nothing. Supposed to be overridden
  }

  /**
   * Called when this widget is added to the widget tree.
   *
   */
  public void addedToWidgetTree()
  {
    // does nothing. Supposed to be overridden
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#layout()
   */
  public void layout()
  {
    // does nothing. Supposed to be overridden
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getDisplayX()
   */
  public int getDisplayX()
  {
    // FIXME Is it the right thing to do, if parent == null ?
    IBasicContainer parent = this.getParent();
    if (parent != null)
    {
      return parent.getDisplayX() + this.getX();
    }
    return 0;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getDisplayY()
   */
  public int getDisplayY()
  {
    // FIXME Is it the right thing to do, if parent == null ?
    IBasicContainer parent = this.getParent();
    if (parent != null)
    {
      return parent.getDisplayY() + this.getY();
    }
    return 0;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getDisplay()
   */
  public Display getDisplay()
  {
    if (parent == null)
    {
      return null;
    }
    return this.getParent().getDisplay();
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getWidget(int, int)
   */
  public IWidget getWidget(int x, int y)
  {
    if (this.isVisible() && (x > 0) && (y > 0) && (x < this.getWidth()) && (y < this.getHeight()))
    {
      return this;
    }

    return null;
  }

  /**
   * Sets the widgets size to the minSize of the widget.
   */
  public void setSizeToMinSize()
  {
    this.setSize(new Dimension(this.getMinSize()));
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#setSize(org.fenggui.util.Dimension)
   */
  public void setSize(Dimension s)
  {
    Dimension oldSize = size;
    size = s;
    this.sizeChanged(new SizeChangedEvent(this, oldSize, size));
  }

  /**
   * Moves the Widget by adding x and y to the current position.
   * Note that usually the layout managers are responsible for positioning
   * Widgets, so hands off!
   * 
   * @param x pixel to the right 
   * @param y pixel to the top
   */
  public void move(int x, int y)
  {
    this.setPosition(new Point(this.getX() + x, this.getY() + y));
  }

  /**
   * Convenience method to set the x and y coordinates of this
   * Widget
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void setXY(int x, int y)
  {
    this.setPosition(new Point(x, y));
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#updateMinSize()
   */
  public void updateMinSize()
  {
    // does nothing. Supposed to be overridden
  }

  /**
   * Sets the parent of this widget.
   * @param parent parent
   */
  public final void setParent(IBasicContainer parent)
  {
    this.parent = parent;
  }

  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("--- ");
    sb.append(this.getClass().getSimpleName());
    sb.append(" ---\n");
    sb.append("size    : ");
    sb.append(size);
    sb.append('\n');
    sb.append("position: ");
    sb.append(position);
    sb.append('\n');
    sb.append("minSize : ");
    sb.append(minSize);

    return sb.toString();
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#focusChanged(org.fenggui.event.FocusEvent)
   */
  public void focusChanged(FocusEvent focusEvent)
  {
    // does nothing. Supposed to be overridden
  }

  /**
   * Sets the local <code>y</code> coordinate of this widget. Note that widget coordinates are measured
   * in the coordinate system of its parent container.
   * @param y <code>y</code> coordinate
   */
  public void setY(int y)
  {
    this.setPosition(new Point(this.getX(), y));
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getY()
   */
  public int getY()
  {
    return position.getY();
  }

  /**
   * Returns true if this widget has the input focus.
   * 
   * @return
   */
  public boolean hasFocus()
  {
    Display d = this.getDisplay();

    if (d == null)
    {
      return false;
    }

    IWidget w = d.getFocusedWidget();

    if (w == null)
    {
      return false;
    }

    return w.equals(this);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#isTraversable()
   */
  public boolean isTraversable()
  {
    return false;
  }

  /**
   * @return width of the widget
   */
  public int getWidth()
  {
    return size.getWidth();
  }

  /**
   * @return height of the widget
   */
  public int getHeight()
  {
    return size.getHeight();
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#isExpandable()
   */
  public boolean isExpandable()
  {
    return expandable;
  }

  /**
   * Sets if this widget is expandable from its currently set size.
   * 
   * @param expandable
   */
  public void setExpandable(boolean expandable)
  {
    this.expandable = expandable;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#isShrinkable()
   */
  public boolean isShrinkable()
  {
    return shrinkable;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getSize()
   */
  public Dimension getSize()
  {
    return size;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getMinSize()
   */
  public Dimension getMinSize()
  {
    return minSize;
  }

  /**
   * Sets if this widget is shrinkable from its current size.
   * 
   * @param shrinkable
   */
  public void setShrinkable(boolean shrinkable)
  {
    this.shrinkable = shrinkable;
  }

  /**
   * Sets the minimum size of this widget. This is usually set by the widget itself. 
   * 
   * @param dim
   */
  public void setMinSize(Dimension dim)
  {
    Dimension oldSize = minSize;
    minSize = dim;
    this.minSizeChanged(new SizeChangedEvent(this, oldSize, minSize));
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#paint(org.fenggui.binding.render.Graphics)
   */
  public void paint(Graphics g)
  {
    // does nothing. Supposed to be overridden
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getPosition()
   */
  public Point getPosition()
  {
    return position;
  }

  /**
   * @return returns the minimum width the widget wants.
   */
  public int getMinWidth()
  {
    return this.getMinSize().getWidth();
  }

  /**
   * @return returns the minimum height the widget wants.
   */
  public int getMinHeight()
  {
    return this.getMinSize().getHeight();
  }

  /**
   * Sets the minimum size of this widget. This is usually set by the widget itself. 
   * 
   * @param minWidth
   * @param minHeight
   */
  public void setMinSize(int minWidth, int minHeight)
  {
    this.setMinSize(new Dimension(minWidth, minHeight));
  }

  /**
   * Sets the size of this widget. 
   * 
   * @param width
   * @param height
   */
  public void setSize(int width, int height)
  {
    this.setSize(new Dimension(width, height));
  }

  /**
   * Sets the height of this widget.
   * 
   * @param height
   */
  public void setHeight(int height)
  {
    this.setSize(new Dimension(this.getWidth(), height));
  }

  /**
   * Sets the width of this widget
   * 
   * @param width
   */
  public void setWidth(int width)
  {
    this.setSize(new Dimension(width, this.getHeight()));
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#setX(int)
   */
  public void setX(int x)
  {
    this.setPosition(new Point(x, this.getY()));
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getX()
   */
  public int getX()
  {
    return position.getX();
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#setPosition(org.fenggui.util.Point)
   */
  public void setPosition(Point p)
  {
    Point oldPosition = position;
    position = p;
    PositionChangedEvent event = new PositionChangedEvent(this, oldPosition, position);
    this.positionChanged(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#isVisible()
   */
  public boolean isVisible()
  {
    if (this.getParent() != null)
    {
      return visible && this.getParent().isVisible();
    }
    else
    {
      return visible;
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#setVisible(boolean)
   */
  public void setVisible(boolean visible)
  {
    this.visible = visible;
    if (this.getParent() != null)
    {
      this.getParent().layout();
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#getData(java.lang.String)
   */
  public Object getData(String key)
  {
    if (data == null)
    {
      return null;
    }
    else
    {
      return data.get(key);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#setData(java.lang.String, java.lang.Object)
   */
  public void setData(String key, Object data)
  {
    if (this.data == null)
    {
      this.data = new HashMap<String, Object>();
    }

    this.data.put(key, data);
  }

  /**
   * called if the minSize of the widget changed.
   * 
   * @param event
   */
  public void minSizeChanged(SizeChangedEvent event)
  {
    if (!this.isVisible() || !this.isInWidgetTree())
    {
      return;
    }

    for (ISizeChangedListener listener : minSizeHook)
    {
      listener.sizeChanged(event);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#addMinSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public void addMinSizeChangedListener(ISizeChangedListener l)
  {
    minSizeHook.add(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IWidget#removeMinSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public void removeMinSizeChangedListener(ISizeChangedListener l)
  {
    minSizeHook.remove(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
   */
  public String getUniqueName()
  {
    return IXMLStreamable.GENERATE_NAME;
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
   */
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    // does nothing. Supposed to be overridden
  }

  /**
   * This will create a copy of the widget. This copy will be deep. The only restriction
   * is that it will not copy any content Items or child Widgets of this widget.
   */
  public Widget clone()
  {
    Widget result;
    try
    {
      result = (Widget) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      //can not happen but write out anyway
      Log.error("Couldn't clone Widget", e);
      return null;
    }

    result.position = position.clone();
    result.size = size.clone();
    result.minSize = minSize.clone();

    result.data = null;
    result.parent = null;
    result.layoutData = null;

    result.initHooks();

    return result;
  }
}
