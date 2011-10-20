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
 * Created on 2005-3-2
 * $Id: Container.java 630 2009-04-22 20:34:09Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.IWidgetListChangedListener;
import org.fenggui.event.PositionChangedEvent;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.event.WidgetListChangedEvent;
import org.fenggui.layout.LayoutManager;
import org.fenggui.layout.RowLayout;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.theme.xml.MissingElementException;
import org.fenggui.util.Dimension;
import org.fenggui.util.Log;

/**
 * A Container is a set of Widgets. The layout manager that is
 * assigned to the Container is responsible for
 * the size and position of its content. In terms of the tree
 * data structure, a container is a node with an arbitrary number
 * of child nodes. 
 * 
 * @author Johannes Schaback
 * @dedicated NOFX - Lazy
 * @see LayoutManager
 */
public class Container extends StandardWidget implements IContainer, Cloneable
{
  private LayoutManager                    layoutManager    = null;
  protected List<IWidget>                  notifyList;
  private boolean                          keyTraversalRoot = false;
  private DefaultAppearance                appearance       = null;
  private List<IWidgetListChangedListener> widgetListChangedHook;
  private boolean                          minSizeUpdated   = false;

  /**
   * Creates a new <code>Container</code>.
   */
  public Container()
  {
    this(new RowLayout());
  }

  public Container(LayoutManager layoutManager)
  {
    super();
    initContainer();
    this.layoutManager = layoutManager;
    appearance = new DefaultAppearance(this);
  }

  /**
   * Copy constructor.
   * 
   * @param container
   */
  public Container(Container container)
  {
    super(container);

    initContainer();
    this.layoutManager = container.layoutManager;
    this.appearance = new DefaultAppearance(this, container.appearance);
  }

  private void initContainer()
  {
    this.notifyList = new CopyOnWriteArrayList<IWidget>();
    this.widgetListChangedHook = new ArrayList<IWidgetListChangedListener>(0);
  }

  public void addWidgetListChangedListener(IWidgetListChangedListener listener)
  {
    widgetListChangedHook.add(listener);
  }

  public void removeWidgetListChangedListener(IWidgetListChangedListener listener)
  {
    widgetListChangedHook.remove(listener);
  }

  public void widgetAdded(WidgetListChangedEvent event)
  {
    for (IWidgetListChangedListener listener : widgetListChangedHook)
    {
      listener.widgetAdded(event);
    }

    Display display = getDisplay();
    if (display != null)
    {
      display.fireGlobalEventListener(event);
    }
  }

  public void widgetRemoved(WidgetListChangedEvent event)
  {
    for (IWidgetListChangedListener listener : widgetListChangedHook)
    {
      listener.widgetRemoved(event);
    }

    Display display = getDisplay();
    if (display != null)
    {
      display.fireGlobalEventListener(event);
    }
  }

  public boolean isKeyTraversalRoot()
  {
    return keyTraversalRoot;
  }

  public void setKeyTraversalRoot(boolean traversalRoot)
  {
    this.keyTraversalRoot = traversalRoot;
  }

  public void setAppearance(DefaultAppearance appearance)
  {
    this.appearance = appearance;
  }

  public DefaultAppearance getAppearance()
  {
    return appearance;
  }

  @Override
  public void focusChanged(FocusEvent focusEvent)
  {
    super.focusChanged(focusEvent);

    if (focusEvent.isFocusGained())
    {
      int i = 0;

      synchronized (notifyList)
      {
        while (i < size() && !notifyList.get(i).isTraversable())
          i++;

        if (i >= size())
          return;

        getDisplay().setFocusedWidget(notifyList.get(i));
      }
    }
  }

  @Override
  public void setSize(Dimension s)
  {
    super.setSize(s);
    this.layout();
  }

  /**
   * If the widget is bigger than the container, trim the
   * clip space size to the size of the container so that
   * the overlapping Widget gets clipped at the Container
   * borders. If the widget fits in the container, simply
   * set the clip space to the size of the widget. You can
   * regard this operation as a logical AND.
   * 
   * The thing is why we do the clipping here and not in 
   * Widget.display is, that Widgets are drawn in their own
   * Widget coordinate system which may have its origin 
   * outside of the Container. Because the clipping is set
   * before the border is drawn, Widgets may overdraw the border,
   * padding and spacing. It is up to the drawing routine of the
   * WidgetAdapter to prevent that.
   * 
   * @todo Widgets that are in a sub-container of this Container are
   * not clipped correctly if they overlap this container! I suggest
   * introducing a Stack for clipping planes and each Container keeps
   * his own clipping plane equations. Then, during rendering, each 
   * Container puts his own clipping equations on the stack. A helper
   * method evaluates the logical AND out of the clipping planes
   * on the stack so that nested, overlapping containers are clipped #
   * 
   * It is not alllowed to place the clipspace outside of the viewport.
   * In this case the widget would not be visible anyway. The returned flag
   * indicates exactely this. Further processing of the widget can be neglected.
   * 
   * @param g graphics
   * @param c widget
   * @return true if valid
   */
  final boolean clipWidget(Graphics g, IWidget c)
  {
    int startX = c.getX() < 0 ? 0 : c.getX();
    int startY = c.getY() < 0 ? 0 : c.getY();

    if (getDisplay() != null)
    {
      Binding b = getDisplay().getBinding();

      if (startX >= b.getCanvasWidth() || startY >= b.getCanvasHeight())
      {
        return false;
      }

      int cWidth = c.getSize().getWidth();
      int cHeight = c.getSize().getHeight();

      g.addClipSpace(startX, startY, c.getX() + cWidth > getWidth() ? getWidth() - startX : cWidth,
        c.getY() + cHeight > getHeight() ? getHeight() - startY : cHeight);

      if (g.getClipSpace() != null)
        return true;
      else
        return false;
    }
    return false;
  }

  /**
   * Returns true as containers are always traversable. Note that
   * the focus gets forwarded to the first widget in the container.
   */
  public boolean isTraversable()
  {
    return true;
  }

  /**
   * Returns the children of this container.
   * @return the children Widgets
   */
  public java.util.List<IWidget> getContent()
  {
    return notifyList;
  }

  /**
   * Adds a Widget to the container.
   * @param c
   *      The Widget to be added.
   */
  public final void addWidget(IWidget c, int position)
  {
    if (c == null)
      return;

    if (c.equals(this))
    {
      throw new IllegalArgumentException("Can't add myself! c.equals(this)");
    }

    if (c.equals(getParent()))
    {
      throw new IllegalArgumentException("Can't add my parent!");
    }

    if (position < 0)
      position = 0;

    addWidgetInternal(c, position);
    updateMinSize();
    widgetAdded(new WidgetListChangedEvent(this, c));
  }

  /* (non-Javadoc)
   * @see org.fenggui.StandardWidget#updateMinSize()
   */
  @Override
  public void updateMinSize()
  {
    minSizeUpdated = false;
    super.updateMinSize();

    if (!minSizeUpdated)
      this.layout();
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#minSizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  @Override
  public void minSizeChanged(SizeChangedEvent event)
  {
    minSizeUpdated = true;
    super.minSizeChanged(event);
  }

  /**
   * Adds a widget to the list. Doesn't refresh the minSize, call the added event or do
   * any layout of the container.
   * @param c
   * @param position
   */
  private void addWidgetInternal(IWidget c, int position)
  {
    synchronized (notifyList)
    {
      if (position > notifyList.size())
        position = notifyList.size();

      if (notifyList.contains(c))
      {
        Log.warn("Container.addWidget: Widget " + c + " is already in the container (" + this + ")");
      }
      else
      {
        //if(relyFocus() == null && c.relyFocus() != null) setRelyFocus(c);
        notifyList.add(position, c);

        c.setParent(this);

        if (getDisplay() != null)
          c.addedToWidgetTree();
      }
    }
  }

  /**
   * Reorders the children such that the given child is drawn last and therefore appears as the top child.
   * @param child the child to bring to top
   */
  public void bringToFront(IWidget child)
  {
    synchronized (notifyList)
    {
      if (!notifyList.contains(child))
        throw new IllegalArgumentException("The given child must be in this container");

      notifyList.remove(child);
      notifyList.add(notifyList.size(), child);
    }
  }

  public void addWidget(IWidget... widgets)
  {
    for (IWidget w : widgets)
    {
      addWidgetInternal(w, notifyList.size());
    }

    updateMinSize();
    widgetAdded(new WidgetListChangedEvent(this, widgets));
  }

  @Override
  public void removedFromWidgetTree()
  {
    super.removedFromWidgetTree();
    synchronized (notifyList)
    {
      for (IWidget w : notifyList)
        w.removedFromWidgetTree();

    }
  }

  @Override
  public void addedToWidgetTree()
  {
    synchronized (notifyList)
    {
      for (IWidget w : notifyList)
        w.addedToWidgetTree();

    }
  }

  /**
   * Sets the layout manager.
   * @param lm layout manager
   */
  public void setLayoutManager(LayoutManager lm)
  {
    if (lm == null)
      return;
    layoutManager = lm;

    updateMinSize();
  }

  /**
   * Returns the currently set layout manager.
   * @return layout manager
   */
  public LayoutManager getLayoutManager()
  {
    return layoutManager;
  }

  /**
   * Layouts this Container according to his layout manager.
   */
  @Override
  public void layout()
  {
    //only use visible widgets
    List<IWidget> widgets = new ArrayList<IWidget>(notifyList.size());

    for (IWidget widget : notifyList)
    {
      if (widget.isVisible())
        widgets.add(widget);
    }

    // layout this container according to the min. sizes of the children
    // and my own size. Since i know the min. size of the children and
    // my final size, I can set the final size of my children as well.

    layoutManager.doLayout(this, widgets);

    synchronized (notifyList)
    {
      // pass layout call to my children.
      for (IWidget c : widgets)
        c.layout();
    }
  }

  /**
   * Updates the min. size and calls layout().
   * @deprecated use layout(), the min. size is kept up to date automatically
   */
  public void updateMinSizeAndLayout()
  {
    updateMinSize();
    layout();
  }

  /**
   * Removes the given Widget from this Container
   * @param c the Widget
   */
  public void removeWidget(IWidget... widgets)
  {

    for (IWidget w : widgets)
    {
      removeWidgetInternal(w);
    }

    updateMinSize();

    if (getDisplay() != null)
      getDisplay().focusedWidgetValityCheck();

    widgetRemoved(new WidgetListChangedEvent(this, widgets));
  }

  public void removeWidgetInternal(IWidget w)
  {
    if (w == null)
      return;
    if (w.equals(this))
      throw new IllegalArgumentException("Cannot remove myself! " + this);

    notifyList.remove(w);
    w.removedFromWidgetTree();
    w.setParent(null);
  }

  /**
   * Removes the specified
   * direct child Widgets from this Container.
   *   
   * @param list list with Widgets to be removed
   * @deprecated use removeWidget(..)
   */
  public void removeWidgets(java.util.List<IWidget> list)
  {
    if (list == null)
      return;

    for (int i = 0; i < list.size(); i++)
    {
      removeWidgetInternal(list.get(i));
    }
    updateMinSize();

    if (getDisplay() != null)
      getDisplay().focusedWidgetValityCheck();

    widgetRemoved(new WidgetListChangedEvent(this, list.toArray(new IWidget[0])));
  }

  /**
   * Removes all Widgets from this Container
   */
  public void removeAllWidgets()
  {
    removeWidget(notifyList.toArray(new IWidget[notifyList.size()]));
  }

  /**
   * Returns the child widget at the specified position.
   * The given position is relative to this Container.
   * @return the child widget or null if no widget has been found
   */
  public IWidget getWidget(int x, int y)
  {
    if (!isVisible() || !getAppearance().insideMargin(x, y))
    {
      return null;
    }

    IWidget ret = null;
    IWidget hit = this;
    x -= getAppearance().getLeftMargins();
    y -= getAppearance().getBottomMargins();

    synchronized (notifyList)
    {
      //go from front (last item) to back (first item)
      for (int i = notifyList.size() - 1; i >= 0; i--)
      {
        IWidget w = notifyList.get(i);
        ret = w.getWidget(x - w.getX(), y - w.getY());

        if (ret != null)
        {
          hit = ret;
          break;
        }
      }
    }
    return hit;
  }

  /**
   * Puts the name of the children in a String.
   */
  @Override
  public String toString()
  {
    if (notifyList == null)
    {
      return super.toString() + " {}";
    }

    String s = super.toString() + " {";

    synchronized (notifyList)
    {
      for (int i = 0; i < notifyList.size(); i++)
      {
        s += notifyList.get(i).getClass().getSimpleName();
        if (i < notifyList.size() - 1)
          s += ", ";
      }
    }
    s += "}";
    return s;
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#positionChanged(org.fenggui.event.PositionChangedEvent)
   */
  @Override
  public void positionChanged(PositionChangedEvent event)
  {
    super.positionChanged(event);

    synchronized (notifyList)
    {
      for (IWidget widget : notifyList)
      {
        widget.positionChanged(event);
      }
    }
  }

  /**
   * Returns the number of direct children.
   * 
   * @return number of children.
   */
  public int size()
  {
    return notifyList.size();
  }

  /**
   * Returns the child Widget with the specified index.
   * @param index the index of the child Widget
   * @return the child Widget
   */
  public IWidget getWidget(int index)
  {
    return notifyList.get(index);
  }

  /**
   * Returns all direct children.
   * 
   * @return children Widgets
   */
  public Iterable<IWidget> getWidgets()
  {
    return notifyList;
  }

  @Override
  public int getDisplayX()
  {
    return super.getDisplayX() + getAppearance().getLeftMargins();
  }

  @Override
  public int getDisplayY()
  {
    return super.getDisplayY() + getAppearance().getBottomMargins();
  }

  /**
   * Layouts and sets the container to the minimum
   * size. Calling this method is equal to calling
   * this sequence of commands.
   * 
   * <code>
   * setSizeToMinSize();
   * layout();
   * </code>
   * 
   */
  public void pack()
  {
    setSizeToMinSize();
    layout();
  }

  /**
   * Returns the predecessor of the given widget.
   * @param currentWidget given widget, or null for last widget in container
   * @return widget the previous widget
   */
  public IWidget getPreviousWidget(IWidget currentWidget)
  {
    int i;
    synchronized (notifyList)
    {
      if (currentWidget == null)
        i = size() - 1;
      else
        i = notifyList.indexOf(currentWidget) - 1;

      if (i < 0)
      {
        if (isKeyTraversalRoot() && 0 != size() - 1)
          i = size() - 1;
        else
          return null;
        //				return getParent().getPreviousWidget(this);
      }

      return notifyList.get(i);
    }
  }

  /**
   * Returns the successor of the given widget.
   * @param currentWidget the given widget, or null to return first in container
   * @return next widget
   */
  public IWidget getNextWidget(IWidget currentWidget)
  {
    int i;
    synchronized (notifyList)
    {
      if (currentWidget == null)
        i = 0;
      else
        i = notifyList.indexOf(currentWidget) + 1;

      if (i > size() - 1)
      {
        if (isKeyTraversalRoot() && (size() - 1) != 0)
          i = 0;
        else
          return null;
      }

      return notifyList.get(i);
    }
  }

  /**
   * Returns the next traversable widget.
   * @param currentWidget the wiget to start searching from
   * @return next traversable widget
   */
  public IWidget getNextTraversableWidget(IWidget currentWidget)
  {
    if (currentWidget != null && !notifyList.contains(currentWidget))
      throw new IllegalArgumentException("currentWidget is not child of this container!");

    IWidget w = getNextWidget(currentWidget);

    //search in this and deeper levels
    while (w != null)
    {
      if (w.isTraversable())
      {
        if (w instanceof IBasicContainer)
        {
          if (!((IBasicContainer) w).isKeyTraversalRoot())
          {
            IWidget tmp = ((IBasicContainer) w).getNextTraversableWidget(null);
            if (tmp != null)
            {
              w = tmp;
              break;
            }
          }
        }
        else
        {
          //found next here
          break;
        }
      }
      w = getNextWidget(w);
    }

    //no widgets here go one level up and search there
    if (w == null && this.getParent() != null && !isKeyTraversalRoot())
    {
      w = this.getParent().getNextTraversableWidget(this);
    }
    else if (w == null && isKeyTraversalRoot())
    {
      w = getNextTraversableWidget(null);
    }

    return w;
  }

  /**
   * Returns the previous trabersable widget.
   * @param currentWidget the wiget to start searching from
   * @return previous traversable widget
   */
  public IWidget getPreviousTraversableWidget(IWidget currentWidget)
  {
    if (currentWidget != null && !notifyList.contains(currentWidget))
      throw new IllegalArgumentException("currentWidget is not child of this container!");

    IWidget w = getPreviousWidget(currentWidget);

    //search in this and deeper levels
    while (w != null)
    {
      if (w.isTraversable())
      {
        if (w instanceof IBasicContainer)
        {
          if (!((IBasicContainer) w).isKeyTraversalRoot())
          {
            IWidget tmp = ((IBasicContainer) w).getPreviousTraversableWidget(null);
            if (tmp != null)
            {
              w = tmp;
              break;
            }
          }
        }
        else
        {
          //found next here
          break;
        }
      }
      w = getPreviousWidget(w);
    }

    //no widgets here go one level up and search there
    if (w == null && this.getParent() != null && !isKeyTraversalRoot())
    {
      w = this.getParent().getPreviousTraversableWidget(this);
    }
    else if (w == null && isKeyTraversalRoot())
    {
      w = getPreviousTraversableWidget(null);
    }

    return w;
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
    try
    {
      layoutManager = stream.processChild(layoutManager, XMLTheme.TYPE_REGISTRY);
    }
    catch (MissingElementException e)
    {
      // we ignore the exception intentionally, because not providing a
      // layout manger means that the default layout manager should remain in
      // place (which is the RowLayoutManager)
    }

    if (stream.startSubcontext("children"))
    {
      stream.processChildren(notifyList, XMLTheme.TYPE_REGISTRY);
      stream.endSubcontext();
    }
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    IOpenGL opengl = g.getOpenGL();

    synchronized (this.notifyList)
    {

      for (IWidget c : notifyList)
      {
        // if widget lays completely outside
        if (c.getX() > this.getWidth() || c.getY() > this.getHeight())
          continue;

        //XXX the upper statement does not recognize the margins of the container!

        boolean valid = this.clipWidget(g, c);

        if (!valid)
        {
          g.removeLastClipSpace();
          continue;
        }

        if (g.getClipSpace() != null)
        {
          opengl.pushMatrix();
          g.translate(c.getX(), c.getY());

          c.paint(g);

          g.translate(-c.getX(), -c.getY());
          opengl.popMatrix();
        }

        g.removeLastClipSpace();
      }

    }

  }

  @Override
  public Dimension getMinContentSize()
  {
    //only use visible widgets
    List<IWidget> widgets = new ArrayList<IWidget>(notifyList.size());

    for (IWidget widget : notifyList)
    {
      if (widget.isVisible())
        widgets.add(widget);
    }

    return this.getLayoutManager().computeMinSize(widgets);
  }

  /* (non-Javadoc)
   * @see org.fenggui.IContainer#getChildWidgetCount()
   */
  public int getChildWidgetCount()
  {
    return notifyList.size();
  }

  /* (non-Javadoc)
   * @see org.fenggui.IContainer#hasChildWidgets()
   */
  public boolean hasChildWidgets()
  {
    return notifyList.size() > 0;
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#clone()
   */
  @Override
  public Container clone()
  {
    Container result = (Container) super.clone();

    result.initContainer();

    result.appearance = this.appearance.clone(result); 
    
    return result;
  }
}
