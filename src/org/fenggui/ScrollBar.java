/*
 * FengGUI - Java GUIs in OpenGL (http://fenggui.dev.java.net)
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
 * $Id: ScrollBar.java 617 2009-04-07 18:31:55Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.mouse.MouseAdapter;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;
import org.fenggui.util.Timer;

/**
 * Implementation of a scroll bar widget. It consists of two buttons and a slider.
 * 
 * @see org.fenggui.Slider
 * @see org.fenggui.Button
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date:
 *         2007-11-19 14:53:14 +0100 (Mo, 19 Nov 2007) $
 * @version $Revision: 617 $
 */
public class ScrollBar extends StandardWidget implements IBasicContainer, Cloneable
{

  public static final String LABEL_DEFAULT   = "default";
  public static final String LABEL_DISABLED  = "disabled";

  /**
   * Flag indicating whether the scroll bar is horizontal or not.
   */
  private boolean            horizontal      = true;

  /**
   * The two buttons to move the slider
   */
  private Button             increaseBtn, decreaseBtn;
  private DefaultAppearance  appearance;
  /**
   * The silder of the scroll bar
   */
  private Slider             slider;

  private double             buttonJump      = 0.05;

  private Timer              autoScrollDelay = new Timer(2, 500);

  private boolean            enabled         = true;

  public ScrollBar()
  {
    this(true);
  }

  public ScrollBar(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
    updateMinSize();
  }

  /**
   * Copy constructor
   * 
   * @param widget
   */
  public ScrollBar(ScrollBar widget)
  {
    super(widget);
    this.horizontal = widget.horizontal;
    this.buttonJump = widget.buttonJump;

    this.appearance = new DefaultAppearance(this, widget.appearance);
    slider = new Slider(widget.slider);
    increaseBtn = new Button(widget.increaseBtn);
    decreaseBtn = new Button(widget.decreaseBtn);

    setupElements();
    updateMinSize();
  }

  /**
   * Creates a new ScrollBar object.
   * 
   * @param horizontal
   *          true if the Slider of the ScrollBar shall be moved horizontally, false
   *          otherwise.
   */
  public ScrollBar(boolean horizontal)
  {
    this.horizontal = horizontal;
    appearance = new DefaultAppearance(this);

    createElements(horizontal);
    setupElements();

    updateMinSize();
  }

  private void createElements(boolean horizontal)
  {
    slider = new Slider(horizontal);
    increaseBtn = new Button();
    decreaseBtn = new Button();
  }

  private void setupElements()
  {
    appearance.setEnabled(LABEL_DISABLED, false);
    
    slider.setParent(this);
    increaseBtn.setParent(this);
    decreaseBtn.setParent(this);

    increaseBtn.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MousePressedEvent mousePressedEvent)
      {
        autoScrollDelay.reset();
      }
    });

    decreaseBtn.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MousePressedEvent mousePressedEvent)
      {
        autoScrollDelay.reset();
      }
    });

    increaseBtn.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        slider.setValue(slider.getValue() + buttonJump);
      }
    });

    decreaseBtn.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        slider.setValue(slider.getValue() - buttonJump);
      }
    });
  }

  @Override
  public void addedToWidgetTree()
  {
    increaseBtn.addedToWidgetTree();
    slider.addedToWidgetTree();
    decreaseBtn.addedToWidgetTree();
  }

  @Override
  public void removedFromWidgetTree()
  {
    increaseBtn.removedFromWidgetTree();
    slider.removedFromWidgetTree();
    decreaseBtn.removedFromWidgetTree();
  }

  public Slider getSlider()
  {
    return slider;
  }

  public boolean isHorizontal()
  {
    return horizontal;
  }

  public Button getDecreaseButton()
  {
    return decreaseBtn;
  }

  public Button getIncreaseButton()
  {
    return increaseBtn;
  }

  public double getButtonJump()
  {
    return buttonJump;
  }

  public void setButtonJump(double buttonJump)
  {
    this.buttonJump = buttonJump;
  }

  /**
   * @return the enabled
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * @param enabled
   */
  public void setEnabled(boolean enabled)
  {
    if (this.enabled == enabled)
    {
      return;
    }

    this.enabled = enabled;
    increaseBtn.setEnabled(enabled);
    decreaseBtn.setEnabled(enabled);
    slider.setEnabled(enabled);

  }

  @Override
  public void layout()
  {
    decreaseBtn.setSizeToMinSize();
    increaseBtn.setSizeToMinSize();

    int contentHeight = getAppearance().getContentHeight();
    int contentWidth = getAppearance().getContentWidth();

    if (horizontal)
    {
      decreaseBtn.setHeight(contentHeight);
      increaseBtn.setHeight(contentHeight);
      decreaseBtn.setXY(0, 0);
      increaseBtn.setXY(contentWidth - increaseBtn.getWidth(), 0);
      slider.setXY(decreaseBtn.getWidth(), 0);
      slider.setSize(contentWidth - (increaseBtn.getWidth() + decreaseBtn.getWidth()), contentHeight);
    }
    else
    {
      increaseBtn.setWidth(contentWidth);
      decreaseBtn.setWidth(contentWidth);
      decreaseBtn.setXY(0, 0);
      increaseBtn.setXY(0, contentHeight - decreaseBtn.getHeight());
      slider.setXY(0, decreaseBtn.getHeight());
      slider.setSize(contentWidth, contentHeight - (decreaseBtn.getHeight() + increaseBtn.getHeight()));
    }
    slider.layout();
  }

  @Override
  public DefaultAppearance getAppearance()
  {
    return appearance;
  }

  @Override
  public IWidget getWidget(int x, int y)
  {
    if (!getAppearance().insideMargin(x, y))
      return null;

    x -= getAppearance().getLeftMargins();
    y -= getAppearance().getBottomMargins();

    if (decreaseBtn.getSize().contains(x - decreaseBtn.getX(), y - decreaseBtn.getY()))
      return decreaseBtn;
    if (increaseBtn.getSize().contains(x - increaseBtn.getX(), y - increaseBtn.getY()))
      return increaseBtn;
    if (slider.getSize().contains(x - slider.getX(), y - slider.getY()))
      return slider.getWidget(x - slider.getX(), y - slider.getY());

    return this;
  }

  public IWidget getNextTraversableWidget(IWidget start)
  {
    return getParent().getNextTraversableWidget(this);
  }

  public IWidget getPreviousTraversableWidget(IWidget start)
  {
    return getParent().getPreviousTraversableWidget(this);
  }

  public IWidget getNextWidget(IWidget start)
  {
    return getParent().getNextWidget(this);
  }

  public IWidget getPreviousWidget(IWidget start)
  {
    return getParent().getPreviousWidget(this);
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    stream.processInherentChild("Slider", slider);

    if (horizontal)
    {
      stream.processInherentChild("ScrollRightButton", increaseBtn);
      stream.processInherentChild("ScrollLeftButton", decreaseBtn);
    }
    else
    {
      stream.processInherentChild("ScrollUpButton", increaseBtn);
      stream.processInherentChild("ScrollDownButton", decreaseBtn);
    }
  }

  @Override
  public Dimension getMinContentSize()
  {
    //add 1 so its possible to scroll from min to max.
    if (isHorizontal())
    {
      int height = Math.max(getIncreaseButton().getMinHeight(), Math.max(getDecreaseButton().getMinHeight(),
        getSlider().getMinHeight()));
      return new Dimension(getIncreaseButton().getMinWidth() + getDecreaseButton().getMinWidth()
          + getSlider().getMinWidth() + 1, height);
    }
    else
    {
      return new Dimension(Math.max(getIncreaseButton().getMinWidth(), Math.max(getDecreaseButton().getMinWidth(),
        getSlider().getMinWidth())), getIncreaseButton().getMinHeight() + getDecreaseButton().getMinHeight()
          + getSlider().getMinHeight() + 1);
    }
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    g.translate(decreaseBtn.getX(), decreaseBtn.getY());
    decreaseBtn.paint(g);
    g.translate(-decreaseBtn.getX(), -decreaseBtn.getY());

    g.translate(slider.getX(), slider.getY());
    slider.paint(g);
    g.translate(-slider.getX(), -slider.getY());

    g.translate(increaseBtn.getX(), increaseBtn.getY());
    increaseBtn.paint(g);
    g.translate(-increaseBtn.getX(), -increaseBtn.getY());

    //if button is pressed move slider slowly
    if (increaseBtn.isPressed() && autoScrollDelay.getState() == 1)
    {
      slider.setValue(slider.getValue() + buttonJump / 10.0);
      autoScrollDelay.setState(1);
    }
    else if (decreaseBtn.isPressed() && autoScrollDelay.getState() == 1)
    {
      slider.setValue(slider.getValue() - buttonJump / 10.0);
      autoScrollDelay.setState(1);
    }
  }

  public boolean isKeyTraversalRoot()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#clone()
   */
  @Override
  public ScrollBar clone()
  {
    ScrollBar result = (ScrollBar) super.clone();
    
    result.appearance = this.appearance.clone(result);
    
    result.autoScrollDelay = new Timer(2, 500);
    result.slider = this.slider.clone();
    result.increaseBtn = this.increaseBtn.clone();
    result.decreaseBtn = this.decreaseBtn.clone();
    
    result.setupElements();
    
    return result;
  }
}
