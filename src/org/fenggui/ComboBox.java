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
 * $Id: ComboBox.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * A list that comes in a popup menu.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2008-01-25 15:58:55
 *         +0100 (Fr, 25 Jan 2008) $
 * @version $Revision: 606 $
 */
public class ComboBox extends StatefullWidget<DefaultAppearance> implements IBasicContainer
{
  private Pixmap          pixmap         = null;
  private Label           label;
  private List            list           = null;
  private ScrollContainer popupContainer = null;

  /**
   * Creates a new <code>ComboBox</code> object.
   * 
   */
  public ComboBox()
  {
    super();
    setAppearance(new DefaultAppearance(this));
    setupWidget();
    updateMinSize();
  }

  public ComboBox(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super();
    setAppearance(new DefaultAppearance(this));
    setupWidget();
    process(stream);
    updateMinSize();
  }

  private void setupWidget()
  {
    setTraversable(true);
    label = new Label();
    label.setText("Space holder....");

    popupContainer = new ScrollContainer();

    list = new List(ToggableGroup.SINGLE_SELECTION);
    list.setToggle(false);
    popupContainer.setInnerWidget(list);

    addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent e)
      {

        // dont listen to de-select events!
        if (!e.isSelected())
          return;

        if (getDisplay() != null)
        {
          getDisplay().removePopup();
        }

        Item item = list.getSelectedItem();

        if (item != null)
        {
          getLabel().setText(item.getText());
          getLabel().setPixmap(item.getPixmap());
        }
        else
        {
          getLabel().setText("");
          getLabel().setPixmap(null);
        }
      }
    });
  }

  /**
   * Sets the pixmap drawn at the right side the combo box.
   * 
   * @param pixmap
   *          the pixmap
   */
  public void setPixmap(Pixmap pixmap)
  {
    this.pixmap = pixmap;
    updateMinSize();
  }

  public ScrollContainer getPopupContainer()
  {
    return popupContainer;
  }

  /**
   * Returns the pixmap.
   * 
   * @return pixmap
   */
  public Pixmap getPixmap()
  {
    return pixmap;
  }

  /**
   * Returns the label used to display the current selection.
   * 
   * @return label
   */
  public Label getLabel()
  {
    return label;
  }

  /**
   * Returns the popup list that appears when the user clicks on the combo box.
   * 
   * @return popup list
   */
  public List getList()
  {
    return list;
  }

  public void addSelectionChangedListener(ISelectionChangedListener l)
  {
    list.getToggableWidgetGroup().addSelectionChangedListener(l);
  }

  public void removeSelectionChangedListener(ISelectionChangedListener l)
  {
    list.getToggableWidgetGroup().removeSelectionChangedListener(l);
  }

  @Override
  public void layout()
  {
    int height = getAppearance().getContentHeight();
    int width = getAppearance().getContentWidth();

    int pixmapWidth = 0;

    if (pixmap != null)
      pixmapWidth = pixmap.getWidth();

    label.setSize(width - pixmapWidth, height);

    label.setXY(0, 0);
  }

  /**
   * Adds a new item to the popup list
   * 
   * @param item
   *          new item
   */
  public void addItem(Item item)
  {
    if (list.isEmpty())
    {
      list.addItem(item);
      // Set the new first item as 'SelectedItem'
      list.setSelectedIndex(0, true);
    }
    else
    {
      list.addItem(item);
      updateMinSize();
    }

  }

  /**
   * Manually selects the item specified by the given string.
   * 
   * @param s
   *          the string
   */
  public void setSelected(String s)
  {
    for (Item item : list.getItems())
    {
      if (s.equals(item.getText()))
      {
        setSelected(item);
        break;
      }
    }
  }

  /**
   * Manually selects the given item.
   * 
   * @param item
   *          the item
   */
  public void setSelected(Item item)
  {
    if (!list.getItems().contains(item))
      return;

    // label.setText(item.getText());
    list.getToggableWidgetGroup().setSelected(this, item, true);
  }

  /**
   * Manually selects the given item index
   * 
   * @param index
   *          The index of the item to select
   * @param selected
   *          <code>true</code> to select, <code>false</code> else.
   */
  public void setSelectedIndex(int index, boolean selected)
  {
    list.setSelectedIndex(index, selected);
  }

  /**
   * Adds a new item to the popup list.
   * 
   * @param s
   *          text of item
   */
  public void addItem(String s)
  {
    addItem(new Item(s, list.getAppearance()));
  }

  /**
   * Opens the popup menu
   */
  private void openPopup()
  {
    list.updateMinSize();
    list.setSizeToMinSize();

    final int displayY = getDisplayY();
    final int displayX = getDisplayX();

    final int horMargins = popupContainer.getAppearance().getLeftMargins()
        + popupContainer.getAppearance().getRightMargins();

    final int verMargins = popupContainer.getAppearance().getTopMargins()
        + popupContainer.getAppearance().getBottomMargins();

    if (displayY - list.getHeight() < 0)
      popupContainer.setHeight(displayY + verMargins);
    else
      popupContainer.setHeight(list.getHeight() + verMargins);

    popupContainer.setWidth(Math.max(list.getWidth(), getWidth()) + horMargins);

    popupContainer.layout();

    popupContainer.setX(displayX);
    popupContainer.setY(displayY - popupContainer.getHeight());

    if (displayY >= 0)
    {

      // the click is processed in org.fenggui.Display afterwards and
      // will place the Frame with the Combo Box inside to the
      // first position in the content list. We have to wait to pass
      // this event and then display the popup.

      //      Thread t = new Thread()
      //      {
      //        public void run()
      //        {
      //          try
      //          {
      //            sleep(50);
      //          }
      //          catch (InterruptedException e)
      //          {
      //          }
      getDisplay().displayPopUp(popupContainer);
      getDisplay().setFocusedWidget(list);
      //        }
      //      };
      //
      //      t.start();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.ObservableWidget#keyReleased(org.fenggui.event.key.KeyReleasedEvent)
   */
  @Override
  public void keyReleased(KeyReleasedEvent keyReleasedEvent)
  {
    if (!keyReleasedEvent.isAlreadyUsed()
        && (keyReleasedEvent.getKeyClass() == Key.ENTER || keyReleasedEvent.getKey() == ' '))
    {
      if (list == null || !list.isInWidgetTree())
      {
        openPopup();
        keyReleasedEvent.setUsed();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.ObservableWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event)
  {
    if (!event.isAlreadyUsed() && event.getButton() == MouseButton.LEFT)
    {
      if (list == null || !list.isInWidgetTree())
      {
        openPopup();
        event.setUsed();
      }
    }
    super.mouseClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
   */
  @Override
  public void mouseDoubleClicked(MouseDoubleClickedEvent event)
  {
    if (!event.isAlreadyUsed() && event.getButton() == MouseButton.LEFT)
    {
      if (list == null || !list.isInWidgetTree())
      {
        openPopup();
        event.setUsed();
      }
    }
    super.mouseDoubleClicked(event);
  }

  public IWidget getNextTraversableWidget(IWidget start)
  {
    if (start == null)
      return this;
    return getParent().getNextTraversableWidget(this);
  }

  public IWidget getPreviousTraversableWidget(IWidget start)
  {
    if (start == null)
      return this;

    return getParent().getPreviousTraversableWidget(this);
  }

  public IWidget getNextWidget(IWidget start)
  {
    return null;
  }

  public IWidget getPreviousWidget(IWidget start)
  {
    return null;
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    stream.processInherentChild("List", list);
    stream.processInherentChild("Label", label);
    stream.processInherentChild("ScrollContainer", popupContainer);

    setPixmap(stream.processChild("Pixmap", getPixmap(), null, Pixmap.class));
  }

  public IToggable<Item> getSelectedItem()
  {
    return list.getToggableWidgetGroup().getSelectedItem();
  }

  /**
   * @return Returns the currently selected value of the ComboBox
   */
  public String getSelectedValue()
  {
    if (label != null)
    {
      return label.getText();
    }

    return null;
  }

  @Override
  public Dimension getMinContentSize()
  {
    int pixmapWidth = 0, pixmapHeight = 0;

    if (pixmap != null)
    {
      pixmapHeight = pixmap.getHeight();
      pixmapWidth = pixmap.getWidth();
    }

    Dimension d = new Dimension(list.getAppearance().getContentMinWidth() + pixmapWidth, Math.max(label.getMinHeight(),
      pixmapHeight));

    // why is this here?
    // if (popupContainer != null && popupContainer.getMinWidth() > d.getWidth())
    // d.setWidth(popupContainer.getMinWidth());
    // else if (list != null && list.getMinWidth() > d.getWidth())
    // d.setWidth(list.getMinWidth());

    return d;
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    g.translate(label.getX(), label.getY());
    label.paint(g);
    g.translate(-label.getX(), -label.getY());

    if (pixmap != null)
    {
      g.setColor(Color.WHITE);
      g.drawImage(pixmap, getAppearance().getContentWidth() - pixmap.getWidth(),
        (getAppearance().getContentHeight() - pixmap.getHeight()) / 2);
    }
  }

  public boolean isKeyTraversalRoot()
  {
    return false;
  }
}
