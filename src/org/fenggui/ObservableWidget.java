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
 * $Id: ObservableWidget.java 616 2009-04-07 12:02:56Z marcmenghin $
 */
package org.fenggui;

import java.util.ArrayList;

import org.fenggui.event.ActivationEvent;
import org.fenggui.event.Event;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.IActivationListener;
import org.fenggui.event.IEventListener;
import org.fenggui.event.IFocusListener;
import org.fenggui.event.key.IKeyListener;
import org.fenggui.event.key.IKeyPressedListener;
import org.fenggui.event.key.IKeyReleasedListener;
import org.fenggui.event.key.IKeyTypedListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.IMouseDraggedListener;
import org.fenggui.event.mouse.IMouseEnteredListener;
import org.fenggui.event.mouse.IMouseExitedListener;
import org.fenggui.event.mouse.IMouseListener;
import org.fenggui.event.mouse.IMouseMovedListener;
import org.fenggui.event.mouse.IMousePressedListener;
import org.fenggui.event.mouse.IMouseReleasedListener;
import org.fenggui.event.mouse.IMouseWheelListener;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.event.mouse.MouseDraggedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MouseMovedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.event.mouse.MouseReleasedEvent;
import org.fenggui.event.mouse.MouseWheelEvent;

/**
 * This widget adds various listener methods to all subclasses. It enriches the widgets
 * with common events.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-29
 *         18:32:35 +0100 (Do, 29 Nov 2007) $
 * @version $Revision: 616 $
 */
public abstract class ObservableWidget extends StandardWidget
{
  private boolean        enabled              = true;
  private boolean        traversable          = false;
  private IWidget        nextWidget           = null;
  private IWidget        previousWidget       = null;
  private IEventListener globalListener;
  private boolean        mousePressedOnWidget = false;

  /**
   * cretes a new ObservableWidget object.
   */
  public ObservableWidget()
  {
    super();
  }

  /**
   * Copy constructor.
   * 
   * @param widget
   */
  public ObservableWidget(ObservableWidget widget)
  {
    super(widget);
    this.enabled = widget.enabled;
  }

  /**
   * Returns true if this widget is enabled. If it is enabled it can recieve events.
   * 
   * @return
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * Sets if this widget is focus traversable (usually with the Tab-Key).
   * 
   * @param b
   */
  public void setTraversable(boolean b)
  {
    traversable = b;
  }

  /**
   * returns the next traversable widget.
   * 
   * @return
   */
  public IWidget getNextTraversableWidget()
  {
    return getParent().getNextTraversableWidget(this);
  }

  /**
   * returns the previouse traversable widget.
   * 
   * @return
   */
  public IWidget getPreviousTraversableWidget()
  {
    return getParent().getPreviousTraversableWidget(this);
  }

  @Override
  public boolean isTraversable()
  {
    return traversable && enabled && isVisible();
  }

  /**
   * Enable of disable this widget.,
   * 
   * @param enabled
   */
  public void setEnabled(boolean enabled)
  {
    if (this.enabled == enabled)
    {
      // No need to (des)activate the same widget twice or more
      return;
    }

    this.enabled = enabled;

    this.activated(new ActivationEvent(this, enabled));
  }

  /**
   * Called if this widget is de-/activated (enabled).
   * 
   * @param e
   */
  public void activated(ActivationEvent e)
  {
    if (!isVisible() || !isInWidgetTree())
      return;

    for (IActivationListener l : activationHook)
    {
      l.widgetActivationChanged(e);
    }
  }

  private ArrayList<IActivationListener>    activationHook    = new ArrayList<IActivationListener>(0);
  private ArrayList<IMouseEnteredListener>  mouseEnteredHook  = new ArrayList<IMouseEnteredListener>(0);
  private ArrayList<IMouseMovedListener>    mouseMovedHook    = new ArrayList<IMouseMovedListener>(0);
  private ArrayList<IMouseExitedListener>   mouseExitedHook   = new ArrayList<IMouseExitedListener>(0);
  private ArrayList<IMousePressedListener>  mousePressedHook  = new ArrayList<IMousePressedListener>(0);
  private ArrayList<IMouseReleasedListener> mouseReleasedHook = new ArrayList<IMouseReleasedListener>(0);
  private ArrayList<IFocusListener>         focusGainedHook   = new ArrayList<IFocusListener>(0);
  private ArrayList<IMouseDraggedListener>  mouseDraggedHook  = new ArrayList<IMouseDraggedListener>(0);
  private ArrayList<IMouseWheelListener>    mouseWheeledHook  = new ArrayList<IMouseWheelListener>(0);
  private ArrayList<IKeyPressedListener>    keyPressedHook    = new ArrayList<IKeyPressedListener>(0);
  private ArrayList<IKeyReleasedListener>   keyReleasedHook   = new ArrayList<IKeyReleasedListener>(0);
  private ArrayList<IKeyTypedListener>      keyTypedHook      = new ArrayList<IKeyTypedListener>(0);
  private ArrayList<IMouseListener>         mouseHook         = new ArrayList<IMouseListener>(0);
  private ArrayList<IKeyListener>           keyHook           = new ArrayList<IKeyListener>(0);

  /**
   * adds a keylistener to this widget.
   * @param l
   */
  public void addKeyListener(IKeyListener l)
  {
    keyHook.add(l);
  }

  /**
   * removes a keylistener from this widget
   * @param l
   */
  public void removeKeyListener(IKeyListener l)
  {
    keyHook.remove(l);
  }

  /**
   * adds a mouselistener to this widget
   * @param l
   */
  public void addMouseListener(IMouseListener l)
  {
    mouseHook.add(l);
  }

  /**
   * removes a mouselistener from this widget.
   * @param l
   */
  public void removeMouseListener(IMouseListener l)
  {
    mouseHook.remove(l);
  }

  @Deprecated
  public void addKeyReleasedListener(IKeyReleasedListener l)
  {
    keyReleasedHook.add(l);
  }

  @Deprecated
  public void removeKeyReleasedListener(IKeyReleasedListener l)
  {
    keyReleasedHook.remove(l);
  }

  @Deprecated
  public void addKeyPressedListener(IKeyPressedListener l)
  {
    keyPressedHook.add(l);
  }

  @Deprecated
  public void removeKeyPressedListener(IKeyPressedListener l)
  {
    keyPressedHook.remove(l);
  }

  @Deprecated
  public void addKeyTypedListener(IKeyTypedListener l)
  {
    keyTypedHook.add(l);
  }

  @Deprecated
  public void removeKeyTypedListener(IKeyTypedListener l)
  {
    keyTypedHook.remove(l);
  }

  @Deprecated
  public void addMouseDraggedListener(IMouseDraggedListener l)
  {
    mouseDraggedHook.add(l);
  }

  @Deprecated
  public void removeMouseDraggedListener(IMouseDraggedListener l)
  {
    mouseDraggedHook.remove(l);
  }

  @Deprecated
  public void addMouseMovedListener(IMouseMovedListener l)
  {
    mouseMovedHook.add(l);
  }

  @Deprecated
  public void removeMouseMovedListener(IMouseMovedListener l)
  {
    mouseMovedHook.remove(l);
  }

  @Deprecated
  public void addMouseReleasedListener(IMouseReleasedListener l)
  {
    mouseReleasedHook.add(l);
  }

  @Deprecated
  public void removeMouseReleasedListener(IMouseReleasedListener l)
  {
    mouseReleasedHook.remove(l);
  }

  @Deprecated
  public void addMousePressedListener(IMousePressedListener l)
  {
    mousePressedHook.add(l);
  }

  @Deprecated
  public void removeMousePressedListener(IMousePressedListener l)
  {
    mousePressedHook.remove(l);
  }

  @Deprecated
  public void addMouseExitedListener(IMouseExitedListener l)
  {
    mouseExitedHook.add(l);
  }

  @Deprecated
  public void removeMouseExitedListener(IMouseExitedListener l)
  {
    mouseExitedHook.remove(l);
  }

  @Deprecated
  public void addMouseEnteredListener(IMouseEnteredListener l)
  {
    mouseEnteredHook.add(l);
  }

  @Deprecated
  public void removeMouseEnteredListener(IMouseEnteredListener l)
  {
    mouseEnteredHook.remove(l);
  }

  /**
   * adds a focuslistener to this widget
   * @param l
   */
  public void addFocusListener(IFocusListener l)
  {
    focusGainedHook.add(l);
  }

  /**
   * removes a focuslistener from this widget.
   * @param l
   */
  public void removeFocusListener(IFocusListener l)
  {
    focusGainedHook.remove(l);
  }

  @Deprecated
  public void addMouseWheelListener(IMouseWheelListener l)
  {
    mouseWheeledHook.add(l);
  }

  @Deprecated
  public void removeMouseWheelListener(IMouseWheelListener l)
  {
    mouseWheeledHook.remove(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
   */
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IMouseEnteredListener l : mouseEnteredHook)
    {
      l.mouseEntered(mouseEnteredEvent);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mouseEntered(mouseEnteredEvent);
    }
  }

  /**
   * adds a activationlistsner from this widget.
   * @param l
   */
  public void addActivationListener(IActivationListener l)
  {
    activationHook.add(l);
  }

  /**
   * removes a activationlistener from this widget.
   * @param l
   */
  public void removeActivationListener(IActivationListener l)
  {
    activationHook.remove(l);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
   */
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IMouseExitedListener l : mouseExitedHook)
    {
      l.mouseExited(mouseExitedEvent);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mouseExited(mouseExitedEvent);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
   */
  public void mousePressed(MousePressedEvent mousePressedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    mousePressedOnWidget = true;

    for (IMousePressedListener l : mousePressedHook)
    {
      l.mousePressed(mousePressedEvent);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mousePressed(mousePressedEvent);
    }

    super.mousePressed(mousePressedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseMoved(int, int)
   */
  public void mouseMoved(int displayX, int displayY)
  {
    if ((mouseHook.isEmpty() && mouseMovedHook.isEmpty()) || !enabled || !isVisible() || !isInWidgetTree())
      return;

    MouseMovedEvent e = new MouseMovedEvent(null, displayX, displayY, getDisplay().getKeyPressTracker().getModifiers());

    for (IMouseMovedListener l : mouseMovedHook)
    {
      l.mouseMoved(e);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mouseMoved(e);
    }

    super.mouseMoved(displayX, displayY);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseDragged(org.fenggui.event.mouse.MouseDraggedEvent)
   */
  public void mouseDragged(MouseDraggedEvent mouseDraggedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IMouseDraggedListener l : mouseDraggedHook)
    {
      l.mouseDragged(mouseDraggedEvent);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mouseDragged(mouseDraggedEvent);
    }
    super.mouseDragged(mouseDraggedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IMouseListener l : mouseHook)
    {
      l.mouseClicked(event);
    }

    super.mouseClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
   */
  @Override
  public void mouseDoubleClicked(MouseDoubleClickedEvent event)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IMouseListener l : mouseHook)
    {
      l.mouseDoubleClicked(event);
    }

    super.mouseDoubleClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseReleased(org.fenggui.event.mouse.MouseReleasedEvent)
   */
  public void mouseReleased(MouseReleasedEvent mouseReleasedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    mousePressedOnWidget = false;

    for (IMouseReleasedListener l : mouseReleasedHook)
    {
      l.mouseReleased(mouseReleasedEvent);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mouseReleased(mouseReleasedEvent);
    }
    super.mouseReleased(mouseReleasedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#keyPressed(org.fenggui.event.key.KeyPressedEvent)
   */
  public void keyPressed(KeyPressedEvent keyPressedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    if (this.isTraversable())
    {
      if (keyPressedEvent.getKeyClass() == Key.TAB)
      {
        IWidget w;
        if (keyPressedEvent.isPressed(Key.SHIFT))
        {
          w = getPreviousTraversableWidget();
        }
        else
        {
          w = getNextTraversableWidget();
        }

        Display disp = getDisplay();
        if (disp != null)
          disp.setFocusedWidget(w);
      }
    }

    for (IKeyPressedListener l : keyPressedHook)
    {
      l.keyPressed(keyPressedEvent);
    }

    for (IKeyListener l : keyHook)
    {
      l.keyPressed(keyPressedEvent);
    }
    super.keyPressed(keyPressedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#keyReleased(org.fenggui.event.key.KeyReleasedEvent)
   */
  public void keyReleased(KeyReleasedEvent keyReleasedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IKeyReleasedListener l : keyReleasedHook)
    {
      l.keyReleased(keyReleasedEvent);
    }

    for (IKeyListener l : keyHook)
    {
      l.keyReleased(keyReleasedEvent);
    }
    super.keyReleased(keyReleasedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#keyTyped(org.fenggui.event.key.KeyTypedEvent)
   */
  public void keyTyped(KeyTypedEvent keyTypedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IKeyTypedListener l : keyTypedHook)
    {
      l.keyTyped(keyTypedEvent);
    }

    for (IKeyListener l : keyHook)
    {
      l.keyTyped(keyTypedEvent);
    }
    super.keyTyped(keyTypedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#focusChanged(org.fenggui.event.FocusEvent)
   */
  public void focusChanged(FocusEvent focusGainedEvent)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IFocusListener l : focusGainedHook)
    {
      l.focusChanged(focusGainedEvent);
    }
    super.focusChanged(focusGainedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#mouseWheel(org.fenggui.event.mouse.MouseWheelEvent)
   */
  public void mouseWheel(MouseWheelEvent e)
  {
    if (!enabled || !isVisible() || !isInWidgetTree())
      return;

    for (IMouseWheelListener l : mouseWheeledHook)
    {
      l.mouseWheel(e);
    }

    for (IMouseListener l : mouseHook)
    {
      l.mouseWheel(e);
    }
    super.mouseWheel(e);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer(super.toString());
    sb.append("\n");
    sb.append("enabled : ");
    sb.append(enabled);

    return sb.toString();
  }

  /** @return Returns the nextWidget. */
  public IWidget getNextWidget()
  {
    return nextWidget;
  }

  /** @param nextWidget The nextWidget to set. */
  public void setNextWidget(IWidget nextWidget)
  {
    this.nextWidget = nextWidget;
  }

  /** @return Returns the previousWidget. */
  public IWidget getPreviousWidget()
  {
    return previousWidget;
  }

  /** @param previousWidget The previousWidget to set. */
  public void setPreviousWidget(IWidget previousWidget)
  {
    this.previousWidget = previousWidget;
  }

  /*
    * (non-Javadoc)
    *
    * @see org.fenggui.Widget#addedToWidgetTree()
    */
  @Override
  public void addedToWidgetTree()
  {
    super.addedToWidgetTree();
    if (getDisplay() != null)
    {
      // Send a mouse released event to the widget if the mouse was pressed here
      globalListener = new IEventListener()
      {
        public void processEvent(Event event)
        {
          if (event instanceof MouseReleasedEvent)
          {
            MouseReleasedEvent mouseReleasedEvent = (MouseReleasedEvent) event;
            if (mouseReleasedEvent.getSource() != ObservableWidget.this && mousePressedOnWidget)
            {
              ObservableWidget.this.mouseReleased(mouseReleasedEvent);
            }
          }
        }
      };
      getDisplay().addGlobalEventListener(globalListener);
    }
  }

  /*
    * (non-Javadoc)
    *
    * @see org.fenggui.Widget#removedFromWidgetTree()
    */
  @Override
  public void removedFromWidgetTree()
  {
    super.removedFromWidgetTree();
    if (getDisplay() != null)
    {
      if (globalListener != null)
      {
        getDisplay().removeGlobalEventListener(globalListener);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#clone()
   */
  @Override
  public ObservableWidget clone()
  {
    ObservableWidget result = (ObservableWidget) super.clone();

    result.activationHook = new ArrayList<IActivationListener>(0);
    result.mouseEnteredHook = new ArrayList<IMouseEnteredListener>(0);
    result.mouseMovedHook = new ArrayList<IMouseMovedListener>(0);
    result.mouseExitedHook = new ArrayList<IMouseExitedListener>(0);
    result.mousePressedHook = new ArrayList<IMousePressedListener>(0);
    result.mouseReleasedHook = new ArrayList<IMouseReleasedListener>(0);
    result.focusGainedHook = new ArrayList<IFocusListener>(0);
    result.mouseDraggedHook = new ArrayList<IMouseDraggedListener>(0);
    result.mouseWheeledHook = new ArrayList<IMouseWheelListener>(0);
    result.keyPressedHook = new ArrayList<IKeyPressedListener>(0);
    result.keyReleasedHook = new ArrayList<IKeyReleasedListener>(0);
    result.keyTypedHook = new ArrayList<IKeyTypedListener>(0);
    result.mouseHook = new ArrayList<IMouseListener>(0);
    result.keyHook = new ArrayList<IKeyListener>(0);

    result.globalListener = null;
    result.setTraversable(this.isTraversable());
    result.previousWidget = null;
    result.nextWidget = null;
    result.mousePressedOnWidget = false;

    return result;
  }
}
