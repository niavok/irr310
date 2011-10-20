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
 * $Id: Button.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.event.mouse.MouseReleasedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;

/**
 * 
 * Implementation of a Button. The Button is one of the most basic Widegts to interact
 * with the user. This class extends Label which allows the Button to easily render images
 * as well as text.<br/> <br/> Note that toggle buttons are not supported in the current
 * implementation of Button.<br/> <br/>
 * 
 * 
 * A Button has all states from a StatefullWidget and the additional state pressed.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-03-12
 *         12:25:07 +0100 (Mi, 12 Mrz 2008) $
 * @version $Revision: 614 $
 * @dedication Terrorgruppe - Namen vergessen
 */
public class Button extends ObservableLabelWidget
{
  private java.util.List<IButtonPressedListener> buttonPressedHook;

  public static final String                     STATE_PRESSED     = "pressed";
  private boolean                                pressed           = false;

  public Button()
  {
    this("");
  }

  public Button(Button button)
  {
    super(button);
    initDefaults();
  }

  public Button(String text)
  {
    setTraversable(true);
    initDefaults();

    setText(text);
  }

  public Button(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
    setTraversable(true);
    initDefaults();
  }

  private void initDefaults()
  {
    buttonPressedHook = new ArrayList<IButtonPressedListener>();
    pressed = false;

    disableDefaultStates();
    updateState();
    updateMinSize();
  }

  private final void pressed()
  {
    if (!pressed)
    {
      pressed = true;
      this.getDisplay().setFocusedWidget(this);
      //called by focus change already
      //this.updateState(STATE_PRESSED);
    }
  }

  private final void released(Set<Key> modifiers)
  {
    if (pressed)
    {
      pressed = false;
      this.updateState();
      fireButtonPressedEvent(modifiers);
    }
  }

  /* (non-Javadoc)
  * @see org.fenggui.StatefullWidget#updateState(java.lang.String)
  */
  @Override
  protected void updateState(String newActiveState)
  {
    getAppearance().setEnabled(STATE_PRESSED, pressed);
    super.updateState(newActiveState);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#keyPressed(org.fenggui.event.key.KeyPressedEvent)
   */
  @Override
  public void keyPressed(KeyPressedEvent keyPressedEvent)
  {
    if (isEnabled())
    {
      if (!keyPressedEvent.isAlreadyUsed())
      {
        if (keyPressedEvent.getKey() == ' ' || keyPressedEvent.getKeyClass() == Key.ENTER)
        {
          pressed();
          keyPressedEvent.setUsed();
        }
      }
    }
    super.keyPressed(keyPressedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#keyReleased(org.fenggui.event.key.KeyReleasedEvent)
   */
  @Override
  public void keyReleased(KeyReleasedEvent e)
  {
    if (isEnabled())
    {
      if (!e.isAlreadyUsed())
      {
        if (e.getKey() == ' ' || e.getKeyClass() == Key.ENTER)
        {
          released(e.getModifiers());
          e.setUsed();
        }
      }
    }
    super.keyReleased(e);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
   */
  @Override
  public void mousePressed(MousePressedEvent mousePressedEvent)
  {
    if (isEnabled())
    {
      if (!mousePressedEvent.isAlreadyUsed() && mousePressedEvent.getButton() == MouseButton.LEFT)
      {
        pressed();
        mousePressedEvent.setUsed();
      }
    }
    super.mousePressed(mousePressedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseReleased(org.fenggui.event.mouse.MouseReleasedEvent)
   */
  @Override
  public void mouseReleased(MouseReleasedEvent mouseReleasedEvent)
  {
    if (isEnabled())
    {
      if (!mouseReleasedEvent.isAlreadyUsed() && mouseReleasedEvent.getButton() == MouseButton.LEFT)
      {
        if (Button.this == mouseReleasedEvent.getSource())
        {
          released(mouseReleasedEvent.getModifiers());
          mouseReleasedEvent.setUsed();
        }
      }
    }
    super.mouseReleased(mouseReleasedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
   */
  @Override
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
  {
    if (isEnabled())
    {
      if (!pressed)
      {
        if (mouseEnteredEvent.getButton() == MouseButton.LEFT && mouseEnteredEvent.getClickCount() >= 1)
        {
          pressed();
          mouseEnteredEvent.setUsed();
        }
      }
    }
    super.mouseEntered(mouseEnteredEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
   */
  @Override
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    if (isEnabled())
    {
      if (pressed)
      {
        pressed = false;
        this.updateState();
        mouseExitedEvent.setUsed();
      }
    }
    super.mouseExited(mouseExitedEvent);
  }

  public boolean isPressed()
  {
    return pressed;
  }

  public void addButtonPressedListener(IButtonPressedListener l)
  {
    if (!buttonPressedHook.contains(l))
    {
      buttonPressedHook.add(l);
    }
  }

  public void removeButtonPressedListener(IButtonPressedListener l)
  {
    buttonPressedHook.remove(l);
  }

  private void fireButtonPressedEvent(Set<Key> modifiers)
  {
    ButtonPressedEvent e = new ButtonPressedEvent(this, modifiers);

    for (int i = 0; i < buttonPressedHook.size(); i++)
    {
      IButtonPressedListener l = buttonPressedHook.get(i);
      l.buttonPressed(e);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableLabelWidget#clone()
   */
  @Override
  public Button clone()
  {
    Button result = (Button) super.clone();
    
    result.initDefaults();
    
    return result;
  }
}
