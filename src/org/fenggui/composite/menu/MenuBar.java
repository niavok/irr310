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
 * $Id: MenuBar.java 610 2009-03-15 15:16:34Z marcmenghin $
 */
package org.fenggui.composite.menu;

import java.io.IOException;
import java.util.LinkedList;

import org.fenggui.StandardWidget;
import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.IMenuClosedListener;
import org.fenggui.event.MenuClosedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyAdapter;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.mouse.MouseDraggedEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * A Menu Bar is the horizontal thing right below the Window header
 * or the top border of the Display.

 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-15 16:16:34 +0100 (So, 15 Mär 2009) $
 * @author Florian Köberle
 * @version $Revision: 610 $
 */
public class MenuBar extends StandardWidget implements IMenuChainElement
{
  private Menu                 currentlyOpen = null;
  private MenuItem             mouseOver     = null;
  private MenuItem             aktiveMenu    = null;
  private LinkedList<MenuItem> items         = new LinkedList<MenuItem>();
  private EntryAppearance      appearance    = null;
  private int                  GAP           = 10;

  public MenuItem getMouseOver()
  {
    return mouseOver;
  }

  public EntryAppearance getAppearance()
  {
    return appearance;
  }

  public MenuBar()
  {
    super();
    appearance = new EntryAppearance(this);
    updateMinSize();
  }

  /**
   * Sets up a MenuItem in this menu bar and builds the states for the
   * MenuItem
   * @param submenu
   * @param name
   */
  public void registerSubMenu(final Menu submenu, String name)
  {
    MenuItem item = new MenuItem(name, appearance);
    item.menu = submenu;
    items.add(item);

    final MenuBar thizz = this;

    submenu.addMenuClosedListener(new IMenuClosedListener()
    {
      public void menuClosed(MenuClosedEvent menuClosedEvent)
      {
        if (menuClosedEvent.getMenu().equals(currentlyOpen))
          currentlyOpen = null;
      }
    });

    submenu.addKeyListener(new KeyAdapter()
    {

      public void keyPressed(KeyPressedEvent kpe)
      {
        if (kpe.getKeyClass().equals(Key.ESCAPE))
        {
          if (currentlyOpen != null)
          {
            currentlyOpen.closeForward();
            currentlyOpen = null;
            getDisplay().setFocusedWidget(thizz);
          }
        }

      }
    });

    updateMinSize();
  }

  private MenuItem findItem(Menu menu)
  {
    for (int i = 0; i < items.size(); i++)
    {
      if (items.get(i).getMenu().equals(menu))
      {
        return items.get(i);
      }
    }

    return null;
  }

  public Iterable<MenuItem> getMenuBarItems()
  {
    return items;

  }

  public int getMenuBarItemCount()
  {
    return items.size();
  }

  private void openMenu(Menu submenu)
  {
    if (submenu.equals(currentlyOpen))
      return;

    // need to find the x value of the menu bar item in order
    // to open the (drop down) menu at the right position
    int x = getX();

    for (MenuItem item : items)
    {
      if (item.getMenu().equals(submenu))
        break;

      x += item.getPreferredSize().getWidth() + GAP;
    }

    submenu.setSizeToMinSize();
    submenu.setY(getY() - submenu.getHeight());
    submenu.setX(x);
    submenu.setPreviousMenu(this);
    currentlyOpen = submenu;

    getDisplay().displayPopUp(submenu);
    getDisplay().setFocusedWidget(submenu);

  }

  public void closeForward()
  {
    currentlyOpen.closeForward();
    if (getDisplay() != null)
      getDisplay().setFocusedWidget(this);
    aktiveMenu = null;
  }

  public void closeBackward()
  {
    //inSensitiveMode = false;
    currentlyOpen = null;

    /* When the user close the menu, then he don't want it to reopen again.
     * The menu would reopen if the menu bar gets here the keyfocus.
     * It makes on the otherhand no sence that the menu keeps it's key focus.
     * Thats why we give no widget the key focus when the user close the menu.
     */
    if (getDisplay() != null)
      getDisplay().setFocusedWidget(null);  
  }

  @Override
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    mouseOver = null;
  }

  @Override
  public void mouseDragged(MouseDraggedEvent mp)
  {
    this.mouseMoved(mp.getDisplayX(), mp.getDisplayY());
  }

  @Override
  public void mouseMoved(int displayX, int displayY)
  {
    int mouseX = displayX - getDisplayX();
    int x = 0;

    for (MenuItem item : items)
    {
      int itemWidth = item.getPreferredSize().getWidth() + GAP;

      if (mouseX >= x && mouseX - x < itemWidth)
      {
        mouseOver = item;

        if (!mouseOver.getMenu().equals(currentlyOpen) && currentlyOpen != null)
        {
          currentlyOpen.closeForward();
          openMenu(mouseOver.getMenu());
        }
        return;
      }
      else
      {
        mouseOver = null;
      }

      x += itemWidth;
    }
  }

  @Override
  public void keyPressed(KeyPressedEvent kpe)
  {
    super.keyPressed(kpe);
    if (items.size() == 0)
    {
      return;
    }

    if (kpe.getKeyClass().equals(Key.DOWN))
    {
      if (mouseOver != null)
      {
        openMenu(mouseOver.getMenu());
      }
    }
    else if (kpe.getKeyClass().equals(Key.LEFT))
    {
      openMenuOfMenuBarItem(getPreviousMenuBarItem());
    }
    else if (kpe.getKeyClass().equals(Key.RIGHT))
    {
      openMenuOfMenuBarItem(getNextMenuBarItem());
    }
  }

  private void openMenuOfMenuBarItem(final MenuItem menuBarItem)
  {
    if (currentlyOpen != null)
    {
      currentlyOpen.closeForward();
    }
    mouseOver = menuBarItem;
    aktiveMenu = menuBarItem;
    openMenu(menuBarItem.getMenu());
  }

  /**
   * 
   * @return the menubar Item right to the currently open menu.
   */
  private MenuItem getNextMenuBarItem()
  {
    if (currentlyOpen != null)
    {
      final MenuItem currentMenuItem = findItem(currentlyOpen);
      final int oldIndex = items.indexOf(currentMenuItem);
      final int nextIndex;
      if (oldIndex + 1 < items.size())
      {
        nextIndex = oldIndex + 1;
      }
      else
      {
        nextIndex = 0;
      }
      return items.get(nextIndex);
    }
    else
    {
      return items.getFirst();
    }
  }

  /**
   * 
   * @return the menubar Item left to the currently open menu.
   */
  private MenuItem getPreviousMenuBarItem()
  {
    if (currentlyOpen != null)
    {
      final MenuItem currentMenuItem = findItem(currentlyOpen);
      final int oldIndex = items.indexOf(currentMenuItem);
      final int nextIndex;
      if (oldIndex - 1 >= 0)
      {
        nextIndex = oldIndex - 1;
      }
      else
      {
        nextIndex = items.size() - 1;
      }
      return items.get(nextIndex);
    }
    else
    {
      return items.getLast();
    }
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    // XXX implement me!!
  }

  @Override
  public void mousePressed(MousePressedEvent mp)
  {
    if (mouseOver == null)
      return;

    openMenu(mouseOver.getMenu());
  }

  public IMenuChainElement getNextMenu()
  {
    return currentlyOpen;
  }

  public IMenuChainElement getPreviousMenu()
  {
    return null;
  }

  @Override
  public Dimension getMinContentSize()
  {
    int sum = this.getMenuBarItemCount() * GAP;
    int itemWidth = sum;
    int itemHeight = 0;

    for (MenuItem item : this.getMenuBarItems())
    {
      itemWidth += item.getPreferredSize().getWidth();
      itemHeight = Math.max(item.getPreferredSize().getHeight(), itemHeight);
    }

    return new Dimension(itemWidth, itemHeight);
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    MenuBar menuBar = this;
    int x = 0;

    for (MenuItem item : menuBar.getMenuBarItems())
    {

      int itemWidth = item.getPreferredSize().getWidth();

      if (item.equals(aktiveMenu))
      {
        appearance.getSelectionUnderlay().paint(g, x, 0, itemWidth + GAP, appearance.getContentHeight());
      }

      if (item.equals(menuBar.getMouseOver()))
      {
        appearance.getHoverUnderlay().paint(g, x, 0, itemWidth + GAP, appearance.getContentHeight());
      }

      item.render(x + GAP / 2, 0, itemWidth, appearance.getContentHeight(), aktiveMenu, appearance, g);

      x += itemWidth + GAP;
    }
  }
}
