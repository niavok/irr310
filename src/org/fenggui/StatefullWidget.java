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
 * Created on 23.10.2007
 * $Id$
 */
package org.fenggui;

import org.fenggui.appearance.DecoratorAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.CursorFactory.CursorType;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;

/**
 * Implementation of a statefull Widget. All widgets where the user is able to interact
 * with should be derived from this abstract class.
 * 
 * This widget has the following states: default, focused, hovered, error and disabled.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public abstract class StatefullWidget<T extends DecoratorAppearance> extends ObservableWidget
{
  public static final String STATE_DEFAULT          = "default";
  public static final String STATE_NONE             = "none";
  public static final String STATE_FOCUSED          = "focused";
  public static final String STATE_HOVERED          = "hovered";
  public static final String STATE_DISABLED         = "disabled";
  public static final String STATE_ERROR            = "error";

  private T                  appearance             = null;
  private boolean            hovered                = false;
  private boolean            error                  = false;
  private CursorType         defaultHoverCursorType = CursorType.HAND;

  /**
   * creates a new StatefullWidget.
   */
  public StatefullWidget()
  {
    super();
  }

  /**
   * Copy constructor.
   * 
   * @param widget
   */
  public StatefullWidget(StatefullWidget<T> widget)
  {
    super(widget);

    this.hovered = widget.hovered;
    this.error = widget.error;
    this.defaultHoverCursorType = widget.defaultHoverCursorType;
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#focusChanged(org.fenggui.event.FocusEvent)
   */
  @Override
  public void focusChanged(FocusEvent focusGainedEvent)
  {
    updateState();
    super.focusChanged(focusGainedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
   */
  @Override
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
  {
    StatefullWidget.this.setHovered(true);
    Binding.getInstance().getCursorFactory().getCursor(defaultHoverCursorType).show();

    super.mouseEntered(mouseEnteredEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
   */
  @Override
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    StatefullWidget.this.setHovered(false);
    Binding.getInstance().getCursorFactory().getCursor(CursorType.DEFAULT).show();

    super.mouseExited(mouseExitedEvent);
  }

  /**
   * Can be overwritten by subclasses if needed.
   */
  protected void disableDefaultStates()
  {
    getAppearance().setEnabled(STATE_DISABLED, false);
    getAppearance().setEnabled(STATE_FOCUSED, false);
    getAppearance().setEnabled(STATE_DEFAULT, false);
    getAppearance().setEnabled(STATE_ERROR, false);
    getAppearance().setEnabled(STATE_NONE, false);
    getAppearance().setEnabled(STATE_DEFAULT, true);
  }

  protected void updateState()
  {
    updateState(null);
  }

  /**
   * Can be overwritten by subclasses if needed.
   */
  protected void updateState(String newActiveState)
  {
    // re-enable default state so switches get called
    getAppearance().setEnabled(STATE_DEFAULT, true);

    if (isEnabled())
    {
      if (isFocused())
      {
        getAppearance().setEnabled(STATE_DISABLED, false);
        getAppearance().setEnabled(STATE_FOCUSED, true);
        getAppearance().setEnabled(STATE_NONE, false);
      }
      else
      {
        getAppearance().setEnabled(STATE_DISABLED, false);
        getAppearance().setEnabled(STATE_FOCUSED, false);
        getAppearance().setEnabled(STATE_NONE, !isHovered());
      }

      if (hasError())
        getAppearance().setEnabled(STATE_ERROR, true);
      else
        getAppearance().setEnabled(STATE_ERROR, false);
    }
    else
    {
      getAppearance().setEnabled(STATE_DISABLED, true);
      getAppearance().setEnabled(STATE_FOCUSED, false);
      getAppearance().setEnabled(STATE_ERROR, false);
      getAppearance().setEnabled(STATE_NONE, false);
    }

    getAppearance().setEnabled(STATE_HOVERED, isHovered());

    if (newActiveState != null)
    {
      getAppearance().setEnabled(newActiveState, true);
    }
  }

  /**
   * @param appearance
   */
  public void setAppearance(T appearance)
  {
    this.appearance = appearance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.StandardWidget#getAppearance()
   */
  @Override
  public T getAppearance()
  {
    return this.appearance;
  }

  /**
   * @return Returns the focused.
   */
  public boolean isFocused()
  {
    if (this.getDisplay() != null && this.getDisplay().getFocusedWidget() == this)
      return true;
    else
      return false;
  }

  /**
   * @param focused
   *          The focused to set.
   */
  public void setFocused(boolean focused)
  {
    if (this.getDisplay() != null)
    {
      if (focused)
      {
        this.getDisplay().setFocusedWidget(this);
      }
      else
      {
        if (isFocused())
          this.getDisplay().setFocusedWidget(null);
      }
    }

    updateState();
  }

  /**
   * @return Returns the hovered.
   */
  public boolean isHovered()
  {
    return hovered;
  }

  /**
   * @param hovered
   *          The hovered to set.
   */
  public void setHovered(boolean hovered)
  {
    this.hovered = hovered;
    updateState();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.ObservableWidget#setEnabled(boolean)
   */
  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    updateState();
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#toString()
   */
  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer(super.toString());
    sb.append("\n");
    sb.append("hovered : ");
    sb.append(hovered);
    sb.append("\n");
    sb.append("error : ");
    sb.append(error);
    sb.append("\n");
    sb.append("focused : ");
    sb.append(isFocused());

    return sb.toString();
  }

  /**
   * @param defaultHoverCursorType
   *          The defaultHoverCursorType to set.
   */
  public void setDefaultHoverCursorType(CursorType defaultHoverCursorType)
  {
    this.defaultHoverCursorType = defaultHoverCursorType;
  }

  /**
   * @return
   */
  public boolean hasError()
  {
    return error;
  }

  /**
   * @param error
   */
  public void setError(boolean error)
  {
    this.error = error;
    updateState();
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#clone()
   */
  @SuppressWarnings("unchecked")
  @Override
  public StatefullWidget<T> clone()
  {
    StatefullWidget<T> result = (StatefullWidget<T>) super.clone();

    return result;
  }
}
