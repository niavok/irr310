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
 * Created on Dec 6, 2006
 * $Id: DecoratorAppearance.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui.appearance;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.StandardWidget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.decorator.IDecorator;
import org.fenggui.decorator.background.Background;
import org.fenggui.decorator.border.Border;
import org.fenggui.decorator.switches.Switch;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Spacing;
import org.fenggui.util.Span;

/**
 * Specialized appearance definition for widgets that need decorators (mainly
 * borders and backgrounds). Decorators can be enabled and disabled to allow for
 * interaction with the mouse or keyboard.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-28 14:13:57 +0100 (Sa, 28 Mär 2009) $
 * @version $Revision: 614 $
 */
public abstract class DecoratorAppearance extends SpacingAppearance
{
  private ArrayList<IDecorator> backgroundDecorators = new ArrayList<IDecorator>(0);
  private ArrayList<IDecorator> foregroundDecorators = new ArrayList<IDecorator>(0);
  private ArrayList<Switch>     switches             = new ArrayList<Switch>(0);

  public DecoratorAppearance(StandardWidget w)
  {
    super(w);
  }

  public DecoratorAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super(w, stream);
  }

  public DecoratorAppearance(StandardWidget w, DecoratorAppearance appearance)
  {
    super(w, appearance);

    for (IDecorator decorator : appearance.backgroundDecorators)
    {
      IDecorator copy = decorator.copy();
      if (copy != null)
        this.backgroundDecorators.add(copy);
    }

    for (IDecorator decorator : appearance.foregroundDecorators)
    {
      IDecorator copy = decorator.copy();
      if (copy != null)
        this.foregroundDecorators.add(copy);
    }

    for (Switch s : appearance.switches)
    {
      Switch copy = s.copy();
      if (copy != null)
        switches.add(copy);
    }
  }

  public void add(String label, Background background, Span spanType)
  {
    background.setLabel(label);
    background.setSpan(spanType);
    backgroundDecorators.add(background);
  }

  public void add(IDecorator decorator)
  {
    backgroundDecorators.add(decorator);
  }

  public void add(Background background)
  {
    add("default", background, Span.PADDING);
  }

  public void addForeground(IDecorator decorator)
  {
    foregroundDecorators.add(decorator);
  }

  public void add(String label, Background background)
  {
    add(label, background, Span.PADDING);
  }

  public void add(String label, Border border, boolean setAsBorderSpacing)
  {
    border.setLabel(label);
    backgroundDecorators.add(border);

    // we need to set a copy of the border as Spacing type in order to avoid that the
    // XMLOutputStream outputs all the border information.
    if (setAsBorderSpacing)
      setBorder(new Spacing(border.getTop(), border.getLeft(), border.getRight(), border.getBottom()));
  }

  public void add(Border border)
  {
    add("default", border, true);
  }

  public void addForeground(Border border)
  {
    border.setLabel("default");
    foregroundDecorators.add(border);
    setBorder(new Spacing(border.getTop(), border.getLeft(), border.getRight(), border.getBottom()));
  }

  public void addForeground(String label, Border border)
  {
    border.setLabel(label);
    foregroundDecorators.add(border);
    setBorder(new Spacing(border.getTop(), border.getLeft(), border.getRight(), border.getBottom()));
  }

  public void add(String label, Border border)
  {
    add(label, border, true);
  }

  public void add(Switch sw)
  {
    switches.add(sw);
  }

  @Override
  public final void paintBackground(Graphics g, IOpenGL gl)
  {
    for (int i = 0; i < backgroundDecorators.size(); i++)
    {
      int width = getWidget().getSize().getWidth();
      int height = getWidget().getSize().getHeight();

      paintDecorator(backgroundDecorators.get(i), g, gl, this, width, height);
    }
  }

  @Override
  public final void paintForeground(Graphics g, IOpenGL gl)
  {
    for (int i = 0; i < foregroundDecorators.size(); i++)
    {
      int width = getWidget().getSize().getWidth();
      int height = getWidget().getSize().getHeight();

      paintDecorator(foregroundDecorators.get(i), g, gl, this, width, height);
    }
  }

  /**
   * Paints the given decorator. It adjusts the size of the decorator according
   * to the span. E.g. Span.BORDER means that the decorator span over the
   * padding AND the border.
   * @param d the decorators
   * @param g the graphics object
   * @param gl the opengl object
   * @param app the appearance used to calculate the margins
   * @param widgetWidth the widget if the whole widget
   * @param widgetHeight the heigth of the whole widget
   */
  private void paintDecorator(IDecorator d, Graphics g, IOpenGL gl, SpacingAppearance app, int widgetWidth,
      int widgetHeight)
  {
    if (!d.isEnabled())
      return;

    int x = 0;
    int y = 0;

    if (d.getSpan() == Span.PADDING)
    {
      Spacing m = app.getMargin();
      Spacing b = app.getBorder();

      x += m.getLeft() + b.getLeft();
      y += m.getBottom() + b.getBottom();

      widgetWidth -= x + m.getRight() + b.getRight();
      widgetHeight -= y + m.getTop() + b.getTop();
    }
    else if (d.getSpan() == Span.BORDER)
    {
      Spacing m = app.getMargin();

      x += m.getLeft();
      y += m.getBottom();

      widgetWidth -= x + m.getRight();
      widgetHeight -= y + m.getTop();
    }

    d.paint(g, x, y, widgetWidth, widgetHeight);
  }

  public void setEnabled(String label, boolean enable)
  {
    for (IDecorator wrapper : backgroundDecorators)
    {
      if (wrapper.getLabel().equals(label))
        wrapper.setEnabled(enable);
    }

    for (IDecorator wrapper : foregroundDecorators)
    {
      if (wrapper.getLabel().equals(label))
        wrapper.setEnabled(enable);
    }

    for (Switch sw : switches)
    {
      if (sw.getLabel().equals(label) && enable == sw.isReactingOnEnabled())
        sw.setup(getWidget());
    }
  }

  @Override
  public String toString()
  {
    String s = "\nBackground decorators:";

    for (IDecorator wrapper : backgroundDecorators)
    {
      s += "\n- " + wrapper.toString();
    }

    s += "\nForeground decorators:";
    for (IDecorator wrapper : foregroundDecorators)
    {
      s += "\n- " + wrapper.toString();
    }

    return super.toString() + s;
  }

  /**
   * Removes all switches and decorators from this appearance.
   *
   */
  public void removeAll()
  {
    backgroundDecorators.clear();
    foregroundDecorators.clear();
    switches.clear();
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    if (stream.startSubcontext("BackgroundDecorators"))
    {
      if (stream.processAttribute("clear", false, false))
        backgroundDecorators.clear();

      stream.processChildren(backgroundDecorators, XMLTheme.TYPE_REGISTRY);

      stream.endSubcontext();
    }

    if (stream.startSubcontext("ForegroundDecorators"))
    {
      if (stream.processAttribute("clear", false, false))
        foregroundDecorators.clear();

      stream.processChildren(foregroundDecorators, XMLTheme.TYPE_REGISTRY);

      stream.endSubcontext();
    }

    if (stream.startSubcontext("Switches"))
    {
      if (stream.processAttribute("clear", false, false))
        switches.clear();

      stream.processChildren(switches, XMLTheme.TYPE_REGISTRY);

      stream.endSubcontext();
    }

  }

  /* (non-Javadoc)
   * @see org.fenggui.appearance.SpacingAppearance#clone(org.fenggui.StandardWidget)
   */
  @Override
  protected DecoratorAppearance clone(StandardWidget widget)
  {
    DecoratorAppearance result = (DecoratorAppearance) super.clone(widget);

    result.backgroundDecorators = new ArrayList<IDecorator>(this.backgroundDecorators.size());
    result.foregroundDecorators = new ArrayList<IDecorator>(this.foregroundDecorators.size());
    result.switches = new ArrayList<Switch>(this.switches.size());

    for (IDecorator decorator : this.backgroundDecorators)
    {
      IDecorator copy = decorator.copy();
      if (copy != null)
        result.backgroundDecorators.add(copy);
    }

    for (IDecorator decorator : this.foregroundDecorators)
    {
      IDecorator copy = decorator.copy();
      if (copy != null)
        result.foregroundDecorators.add(copy);
    }

    for (Switch s : this.switches)
    {
      Switch copy = s.copy();
      if (copy != null)
        result.switches.add(copy);
    }

    return result;
  }

}
