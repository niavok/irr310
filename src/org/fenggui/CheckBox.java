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
 * $Id: CheckBox.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * Implementation of a check box. A check box can be used to toggle between two states.
 * <br/> <br/> It is currently not supported to disable the check box.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-19
 *         14:53:14 +0100 (Mo, 19 Nov 2007) $
 * @dedication NOFX - Lower
 * @version $Revision: 614 $
 */
public class CheckBox<E> extends ObservableLabelWidget implements IToggable<CheckBox<E>>
{
  private ArrayList<ISelectionChangedListener> selectionChangedHook = new ArrayList<ISelectionChangedListener>();

  private E                                    value                = null;
  private boolean                              selected             = false;

  public static final String                   STATE_SELECTED       = "selected";
  public static final String                   STATE_DESELECTED     = "deselected";

  /**
   * Creates a new <code>CheckBox</code> widget.
   */
  public CheckBox()
  {
    this("");
  }

  /**
   * 
   * Creates a new <code>CheckBox</code> widget.
   * 
   * @param text
   *          the text displayed next to the check box
   */
  public CheckBox(String text)
  {
    super();

    getAppearance().setEnabled(STATE_SELECTED, false);
    getAppearance().setEnabled(STATE_DESELECTED, true);
    setTraversable(true);
    setText(text); // does an updateMinSize()
  }
  
  public CheckBox(CheckBox<E> checkBox)
  {
    super(checkBox);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#keyTyped(org.fenggui.event.key.KeyTypedEvent)
   */
  @Override
  public void keyTyped(KeyTypedEvent keyTypedEvent)
  {
    if (keyTypedEvent.getKeyClass() == Key.ENTER || Character.isSpaceChar(keyTypedEvent.getKey()))
    {
      keyTypedEvent.setUsed();
      
      setSelected(!isSelected());
    }
    
    super.keyTyped(keyTypedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event)
  {
    event.setUsed();
    setSelected(!isSelected());
    
    super.mouseClicked(event);
  }

  /**
   * Returns whether the check box is selected or not
   * 
   * @return true if selected, false otherwise
   */
  public boolean isSelected()
  {
    return selected;
  }

  /**
   * Selects or deselects this check box manually.
   */
  public CheckBox<E> setSelected(boolean b)
  {
    selected = b;

    this.updateState();
    if (b)
    {
      getAppearance().setEnabled(STATE_DESELECTED, false);
      getAppearance().setEnabled(STATE_SELECTED, true);
    }
    else
    {
      getAppearance().setEnabled(STATE_SELECTED, false);
      getAppearance().setEnabled(STATE_DESELECTED, true);
    }

    fireSelectionChangedEvent(this, this, b);

    return this;
  }

  /**
   * Returns the value associated with this check box.
   * 
   * @return value
   */
  public E getValue()
  {
    return value;
  }

  /**
   * Sets the associated value for this check box.
   * 
   * @param value
   *          value
   */
  public void setValue(E value)
  {
    this.value = value;
  }

  /**
   * Add a {@link ISelectionChangedListener} to the widget. The listener can be added only
   * once.
   * 
   * @param l
   *          Listener
   */
  public void addSelectionChangedListener(ISelectionChangedListener l)
  {
    if (!selectionChangedHook.contains(l))
    {
      selectionChangedHook.add(l);
    }
  }

  /**
   * Add the {@link ISelectionChangedListener} from the widget
   * 
   * @param l
   *          Listener
   */
  public void removeSelectionChangedListener(ISelectionChangedListener l)
  {
    selectionChangedHook.remove(l);
  }

  /**
   * Fire a {@link SelectionChangedEvent}
   * 
   * @param source
   * @param t
   * @param s
   */
  private void fireSelectionChangedEvent(IWidget source, CheckBox<E> t, boolean s)
  {
    SelectionChangedEvent e = new SelectionChangedEvent(source, t, s);

    for (ISelectionChangedListener l : selectionChangedHook)
    {
      l.selectionChanged(e);
    }
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
    setSelected(stream.processAttribute("selected", isSelected(), false));
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableLabelWidget#clone()
   */
  @Override
  public CheckBox<E> clone()
  {
    CheckBox<E> result = (CheckBox<E>) super.clone();
    
    result.selectionChangedHook = new ArrayList<ISelectionChangedListener>();
    
    result.getAppearance().setEnabled(STATE_SELECTED, false);
    result.getAppearance().setEnabled(STATE_DESELECTED, true);
    
    return result;
  }

}
