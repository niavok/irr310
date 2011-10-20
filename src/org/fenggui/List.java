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
 * $Id: List.java 630 2009-04-22 20:34:09Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;
import org.fenggui.util.Rectangle;

/**
 * Implementation of a vertical list of items with each item be selectable. The items used
 * in this list are <code>Items</code> rather than widgets.
 * 
 * 
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-04-22 22:34:09 +0200 (Mi, 22 Apr 2009) $
 * @version $Revision: 630 $
 * @dedication The Offspring - Defy You
 */
public class List extends ModelWidget<EntryAppearance, IListModel>
{
  private ToggableGroup<Item>  toggableWidgetGroup = null;
  private java.util.List<Item> items               = new ArrayList<Item>();
  private Item                 hoveredItem         = null;
  private int                  mouseOverRow        = -1;
  private boolean              toggleOn            = true;

  /**
   * Creates a new list
   * 
   */
  public List()
  {
    this(ToggableGroup.SINGLE_SELECTION);
  }

  /**
   * Creates a new List object.
   * 
   * @param selectionType
   */
  public List(int selectionType)
  {
    toggableWidgetGroup = new ToggableGroup<Item>(selectionType);

    setAppearance(new EntryAppearance(this));
    updateMinSize();
  }

  /**
   * Function so always true click items like ComboBoxes work properly
   *
   * @param b Set to true for the Item to toggle between selected and
   * deselected as you click it.  false to always set as true, regardless
   * of how many times in a row it is clicked.
   *
   */

  public void setToggle(boolean b)
  {
    toggleOn = b;
  }

  public ToggableGroup<Item> getToggableWidgetGroup()
  {
    return toggableWidgetGroup;
  }

  public void addItem(Item... Items)
  {
    for (Item item : Items)
    {
      items.add(item);
    }
    updateMinSize();
  }

  public void addItem(String... texts)
  {
    java.util.List<Item> items = new ArrayList<Item>(texts.length);
    for (String itemText : texts)
    {
      items.add(new Item(itemText, getAppearance()));
    }

    addItem(items.toArray(new Item[items.size()]));
  }

  public Item getItem(int row)
  {
    return items.get(row);
  }

  public void removeItem(int row)
  {
    removeItem(items.get(row));
  }

  public void removeItem(Item... items)
  {
    for (Item item : items)
    {
      this.items.remove(item);
    }
    updateMinSize();
  }

  public void clear()
  {
    items.clear();
    updateMinSize();
  }

  private void updateHoveredItem(int y, int mouseY)
  {
    hoveredItem = null;
    mouseOverRow = -1;
    int currentY = y;
    
    if (mouseY >= currentY) //only if over a item
    {
      for (int i = (items.size() - 1); i >= 0; i--)
      {
        Item item = items.get(i);
        currentY += item.getPreferredSize().getHeight();

        if (mouseY <= currentY)
        {
          hoveredItem = item;
          mouseOverRow = i;
          break;
        }
      }
    }
  }
  
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    if (stream.startSubcontext("Items"))
    {
      stream.processChildren("Item", items, Item.class);
      stream.endSubcontext();
    }
    updateMinSize();
  }

  @Override
  public void mouseMoved(int displayX, int displayY)
  {
    int mouseY = displayY - getDisplayY();
    int frombottom = this.getSize().getHeight() - this.getMinSize().getHeight();
    int currentY = frombottom > 0 ? frombottom : 0;

    updateHoveredItem(currentY, mouseY);
    
    super.mouseMoved(displayX, displayY);
  }

  @Override
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    mouseOverRow = -1;
    hoveredItem = null;
    Binding.getInstance().getCursorFactory().getDefaultCursor().show();
    super.mouseExited(mouseExitedEvent);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.Widget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
   */
  @Override
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
  {
    Binding.getInstance().getCursorFactory().getHandCursor().show();
    super.mouseEntered(mouseEnteredEvent);
  }

  public boolean isEmpty()
  {
    return items.isEmpty();
  }

  public int size()
  {
    return items.size();
  }

  public void setSelectedIndex(int index)
  {
    if (index < 0 || index >= items.size())
      return;

    Item item = items.get(index);
    boolean selected = !item.isSelected();

    if (!toggleOn)
      selected = true;

    toggableWidgetGroup.setSelected(this, item, selected);
    item.setSelected(selected);
  }

  public void setSelectedIndex(int index, boolean selected)
  {
    if (index < 0 || index >= items.size())
      return;

    Item item = items.get(index);
    toggableWidgetGroup.setSelected(this, item, selected);
    item.setSelected(selected);
  }

  public java.util.List<Item> getItems()
  {
    return items;
  }

  public int getMouseOverRow()
  {
    return mouseOverRow;
  }

  public Item getSelectedItem()
  {
    return (Item) toggableWidgetGroup.getSelectedItem();
  }

  @Override
  public Dimension getMinContentSize()
  {
    if (isEmpty())
      return new Dimension(5, 5);

    int width = 0;
    int height = 0;

    for (Item item : items)
    {
      Dimension size = item.getPreferredSize();
      width = Math.max(width, size.getWidth());
      height += size.getHeight();
    }

    return new Dimension(width, height);
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    if (isEmpty())
      return;
    EntryAppearance appearance = getAppearance();

    int y = appearance.getContentHeight();
    int x = 0;
    int width = appearance.getContentWidth();

    g.setColor(appearance.getColor());

    for (Item item : items)
    {
      Dimension size = item.getPreferredSize();

      y -= size.getHeight();

      //check if visible
      if (g.getClipSpace().getIntersection(
        new Rectangle(this.getDisplayX() + x, this.getDisplayY() + y, width, size.getHeight())) == null)
        continue;

      //draw item
      item.render(x, y, width, size.getHeight(), hoveredItem, appearance, g);

    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event)
  {
    int mouseY = event.getLocalY(this);
    int frombottom = this.getSize().getHeight() - this.getMinSize().getHeight();
    int currentY = frombottom > 0 ? frombottom : 0;
    
    updateHoveredItem(currentY, mouseY);
    
    setSelectedIndex(mouseOverRow);
    super.mouseClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ModelWidget#ModelUpdated(org.fenggui.IModel, boolean)
   */
  @Override
  protected void ModelUpdated(IListModel model, boolean newModel)
  {
    UpdateListFromModel();
  }

  private void UpdateListFromModel()
  {
    items.clear();

    for (int i = 0; i < this.getModel().getSize(); i++)
      items.add(this.getModel().getItem(i, getAppearance()));

    updateMinSize();
  }
}
