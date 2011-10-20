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
 * $Id: FengGUI.java 634 2009-05-01 14:38:45Z marcmenghin $
 */
package org.fenggui;

import java.util.HashMap;
import java.util.Map;

import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.composite.Window;
import org.fenggui.composite.menu.Menu;
import org.fenggui.composite.menu.MenuBar;
import org.fenggui.composite.menu.MenuItem;
import org.fenggui.theme.DefaultTheme;
import org.fenggui.theme.ITheme;
import org.fenggui.theme.XMLTheme;
import org.fenggui.util.Util;

/**
 * This class provides some basic information about FengGUI to applications.
 * Furthermore, it acts as a Factory class for Themed Widgets.
 *  
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-05-01 16:38:45 +0200 (Fr, 01 Mai 2009) $
 * @version $Revision: 634 $
 */
public class FengGUI
{
  //Basic FengGUI information for others to read out.
  public static final String            VERSION           = "Alpha 12a";
  public static final String            NAME              = "FengGUI";
  public static final String            WEB               = "http://www.fenggui.org";

  //widget prototypes (already themed)
  private static Button                 ptButton          = null;
  private static Container              ptContainer       = null;
  private static ScrollContainer        ptScrollContainer = null;
  private static Label                  ptLabel           = null;
  private static TextEditor             ptTextEditor      = null;
  private static ScrollBar              ptScrollBarH      = null;
  private static ScrollBar              ptScrollBarV      = null;
  @SuppressWarnings("unchecked")
  private static RadioButton            ptRadioButton     = null;
  @SuppressWarnings("unchecked")
  private static CheckBox               ptCheckBox        = null;

  private static Map<Class<?>, IWidget> ptList            = null;

  private static ITheme                 theme             = null;

  /**
   * Inits the widget prototypes. This can be called to initialize all
   * prototypes at once. Very useful on loading screens.
   */
  public static void initPrototypes()
  {
    if (false)
    {
      Container fake = new Container();
      createButton(fake);
      createContainer(fake);
      createLabel(fake);
      createScrollContainer(fake);
      createTextEditor(fake);
      createScrollBar(true);
      createScrollBar(false);
      createCheckBox();
      createRadioButton(fake, "");
    }
  }

  /**
   * Removes all widget prototypes. This is automatically called after you set a new theme.
   */
  public static void removePrototypes()
  {
    ptButton = null;
    ptContainer = null;
    ptScrollContainer = null;
    ptLabel = null;
    ptTextEditor = null;
    ptScrollBarH = null;
    ptScrollBarV = null;
    if (ptList != null)
      ptList.clear();
  }

  /**
   * Created a themed instance of a Widget. This is the preferred way to create widgets. It
   * handles a cache of prototype widgets to improve speed. Only widgets which implement the
   * {@link Cloneable} interface can be used by caching mechanism.
   * 
   * @param <T>
   * @param widgetClass The class of the Widget to create a instance from.
   * @return a themed instance of a widget.
   */
  @SuppressWarnings("unchecked")
  public static <T extends IWidget> T createWidget(Class<T> widgetClass)
  {
    T widget = null;
    boolean cloneableClass = Cloneable.class.isAssignableFrom(widgetClass);
    if (cloneableClass)
    {
      if (ptList == null)
        ptList = new HashMap<Class<?>, IWidget>(20);

      //check prototype
      if (ptList.containsKey(widgetClass))
      {
        return (T) ptList.get(widgetClass).clone();
      }
    }

    widget = Util.createInstanceOfClass(widgetClass);
    FengGUI.setUpAppearance(widget);

    if (cloneableClass)
    {
      ptList.put(widgetClass, (T) widget.clone());
    }

    return widget;
  }

  //	/**
  //	 * Create a TabContainer widget.
  //	 * 
  //	 * @param parent the parent container
  //	 * @param tabOnTop true if tabs are on top
  //	 * @return new TabContainer widget.
  //	 */
  //	public static org.fenggui.composite.tab.TabContainer createTab(IContainer parent)
  //	{
  //		org.fenggui.composite.tab.TabContainer result = new org.fenggui.composite.tab.TabContainer();
  //		FengGUI.setUpAppearance(result);
  //		parent.addWidget(result);
  //		return result;
  //	}
  //	

  /**
   * Creates a new progress bar.
   * @param parent the parent container 
   * @return new progress bar
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static ProgressBar createProgressBar(IContainer parent)
  {
    ProgressBar btn = new ProgressBar();
    FengGUI.setUpAppearance(btn);
    parent.addWidget(btn);
    return btn;
  }

  /**
   * Creates a new combo box.
   * @param parent the parent container
   * @return new combo box
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static ComboBox createComboBox(IContainer parent)
  {
    ComboBox btn = FengGUI.createComboBox();
    parent.addWidget(btn);
    return btn;
  }

  /**
   * 
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static ComboBox createComboBox()
  {
    ComboBox btn = new ComboBox();
    FengGUI.setUpAppearance(btn);
    return btn;
  }

  /**
   * 
   * @param parent
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Button createButton(IContainer parent)
  {
    if (ptButton == null)
    {
      ptButton = new Button();
      FengGUI.setUpAppearance(ptButton);
    }

    Button btn = new Button(ptButton);
    parent.addWidget(btn);
    return btn;
  }

  /**
   * Creates a new Display object. Note that you usually have only one
   * Display object at run-time and that the Display has no AppearanceAdapter.
   * @param binding the OpenGL binding that is used to render the UI
   * @return the new Display object.
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Display createDisplay(Binding binding)
  {
    Display btn = new Display(binding);
    FengGUI.setUpAppearance(btn);
    return btn;
  }

  /**
   * 
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Container createContainer()
  {
    if (ptContainer == null)
    {
      ptContainer = new Container();
      FengGUI.setUpAppearance(ptContainer);
    }
    Container c = new Container(ptContainer);

    //    Container c = new Container();
    //    FengGUI.setUpAppearance(c);
    return c;
  }

  /**
   * Creates a new container. Uses <code>CONTAINER</code>
   * as the appearance identifier.
   * @param parent the parent container
   * @return new container
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Container createContainer(IContainer parent)
  {
    Container c = createContainer();
    parent.addWidget(c);
    return c;
  }

  /**
   * Creates a new button.
   * @param parent parent container
   * @param text the text on the button
   * @return new button
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Button createButton(IContainer parent, String text)
  {
    if (ptButton == null)
    {
      ptButton = new Button();
      FengGUI.setUpAppearance(ptButton);
    }

    Button btn = new Button(ptButton);

    //    Button btn = new Button();
    //    FengGUI.setUpAppearance(btn);

    parent.addWidget(btn);
    btn.setText(text);
    return btn;
  }

  /**
   * 
   * @param text
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Button createButton(String text)
  {
    if (ptButton == null)
    {
      ptButton = new Button();
      FengGUI.setUpAppearance(ptButton);
    }

    Button btn = new Button(ptButton);

    //    Button btn = new Button();
    //    FengGUI.setUpAppearance(btn);

    btn.setText(text);
    return btn;
  }

  /**
   * Creates a new button.
   * @param parent the parent container
   * @param image an image on the button
   * @return new button
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Button createButton(IContainer parent, Pixmap image)
  {
    Button btn = createButton(parent);
    FengGUI.setUpAppearance(btn);
    btn.setPixmap(image);
    return btn;
  }

  @SuppressWarnings("unchecked")
  public static <T> RadioButton<T> createRadioButton()
  {
    return (RadioButton<T>) FengGUI.createWidget(RadioButton.class);
  }

  /**
   * Creates a new radio button. 
   * @param parent the parent container
   * @param group the button group that manages the mutual exclusive selection
   * @return the new radio button
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static <T> RadioButton<T> createRadioButton(IContainer parent, ToggableGroup<RadioButton<T>> group)
  {
    if (ptRadioButton == null)
    {
      ptRadioButton = new RadioButton();
      FengGUI.setUpAppearance(ptRadioButton);
    }

    RadioButton<T> btn = new RadioButton<T>(ptRadioButton);
    btn.setRadioButtonGroup(group);
    parent.addWidget(btn);
    return btn;
  }

  /**
   * Creates a new radio button.
   * @param parent the parent container
   * @param text the text displayed on the radio button
   * @return the new radio button
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static <T> RadioButton<T> createRadioButton(IContainer parent, String text)
  {
    RadioButton<T> btn = FengGUI.<T> createRadioButton(parent, (ToggableGroup<RadioButton<T>>) null);
    btn.setText(text);
    return btn;
  }

  /**
   * Creates a new radio button.
   * @param parent the parent container
   * @param text the text displayed beside the radio button
   * @param group the button group that manages the mutual exclusive selection
   * @return the new radio button
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static <T> RadioButton<T> createRadioButton(IContainer parent, String text, ToggableGroup<RadioButton<T>> group)
  {
    RadioButton<T> btn = FengGUI.<T> createRadioButton(parent, group);
    btn.setText(text);
    btn.setRadioButtonGroup(group);
    return btn;
  }

  /**
   * Creates a new check box.
   * @param parent the parent container
   * @return the new check box
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static <T> CheckBox<T> createCheckBox(IContainer parent)
  {
    CheckBox<T> btn = FengGUI.<T> createCheckBox();
    parent.addWidget(btn);
    return btn;
  }

  /**
   * 
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> CheckBox<T> createCheckBox()
  {
    return (CheckBox<T>) FengGUI.createWidget(CheckBox.class);
  }

  /**
   * Creates a new check box. 
   * @param parent the parent container
   * @param text the text displayed at the side of the check box
   * @return the new check box
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static <T> CheckBox<T> createCheckBox(IContainer parent, String text)
  {
    CheckBox<T> btn = FengGUI.<T> createCheckBox(parent);
    btn.setText(text);
    return btn;
  }

  /**
   * Creates a new label.
   * @param parent the parent container
   * @return new label
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel(IContainer parent)
  {
    Label l = createLabel();
    parent.addWidget(l);
    return l;
  }

  /**
   * Create a new label.
   * 
   * @return new label widget.
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel()
  {
    if (ptLabel == null)
    {
      ptLabel = new Label();
      FengGUI.setUpAppearance(ptLabel);
    }
    Label l = new Label(ptLabel);

    //    Label l = new Label();
    //    FengGUI.setUpAppearance(l);
    return l;
  }

  /**
   * 
   * @param text
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel(String text)
  {
    Label l = createLabel();
    l.setText(text);
    return l;
  }

  /**
   * Creates a new menu bar.
   * @param parent the parent container
   * @return new menu bar
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static MenuBar createMenuBar(IContainer parent)
  {
    MenuBar menuBar = new MenuBar();
    FengGUI.setUpAppearance(menuBar);
    parent.addWidget(menuBar);
    return menuBar;
  }

  /**
   * Creates a new menu associated with a menu bar.
   * @param parent the parent MenuBar
   * @return new menu
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Menu createMenu(Display parent, boolean display)
  {
    Menu menu = new Menu();
    FengGUI.setUpAppearance(menu);
    if (display)
      parent.addWidget(menu);
    return menu;
  }

  /**
   * Creates a new menu associated with a menu bar.
   * @param parent the parent MenuBar
   * @return new menu
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Menu createMenu(Menu parent, String name, boolean display)
  {
    Menu menu = createMenu(parent.getDisplay(), display);
    parent.registerSubMenu(menu, name);
    return menu;
  }

  /**
   * 
   * @param parent
   * @param name
   * @param display
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Menu createMenu(MenuBar parent, String name, boolean display)
  {
    Menu menu = createMenu(parent.getDisplay(), display);
    parent.registerSubMenu(menu, name);
    return menu;
  }

  /**
   * Creates a new menu item in a menu.
   * @param parent the parent menu.
   * @return new menu item
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static MenuItem createMenuItem(Menu parent, String name)
  {
    MenuItem item = new MenuItem(name, parent.getAppearance());
    parent.addItem(item);
    //FIXME: needs a fix here
    //item.getTextRenderer().setFont(parent.getAppearance().getFont());
    return item;
  }

  /**
   * Creates a new list Widget.
   * @return new list
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static List createList()
  {
    List btn = new List();
    FengGUI.setUpAppearance(btn);
    return btn;
  }

  /**
   * Creates a new list Widget.
   * @param parent the parent container
   * @return new list
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static List createList(IContainer parent)
  {
    List btn = new List();
    FengGUI.setUpAppearance(btn);
    parent.addWidget(btn);
    return btn;
  }

  /**
   * 
   * @param parent
   * @param selectionType
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static List createList(IContainer parent, int selectionType)
  {
    List btn = new List(selectionType);
    FengGUI.setUpAppearance(btn);
    parent.addWidget(btn);
    return btn;
  }

  /**
   * Creates a new label.
   * @param parent parent container
   * @param text the text the label shall display.
   * @return new label
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel(IContainer parent, String text)
  {
    Label btn = createLabel(parent);
    btn.setText(text);
    return btn;
  }

  /**
   * Creates a new label.
   * @param parent the parent container
   * @param image the image to be displayed
   * @return new label
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel(IContainer parent, ITexture image)
  {
    Label btn = createLabel(parent);
    btn.setPixmap(new Pixmap(image));
    return btn;
  }

  /**
   * Creates a new label.
   * @param parent the parent container
   * @param pixmap the image to be displayed
   * @return new label
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel(IContainer parent, Pixmap pixmap)
  {
    Label btn = createLabel(parent);
    btn.setPixmap(pixmap);
    return btn;
  }

  /**
   * Creates a new label. 
   * @param parent the parent container
   * @param text the text to be dislayed
   * @param image the image to be displayed
   * @return new label
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Label createLabel(IContainer parent, String text, ITexture image)
  {
    Label label = createLabel(parent, text);
    label.setPixmap(new Pixmap(image));
    return label;
  }

  /**
   * Creates a new window.
   * @param parent the Display
   * @param closeBtn whether the window has close button or not
   * @param maxBtn whether the window has a maximize button or not
   * @param minBtn whether the windows has a minimize button or not
   * @return new window
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Window createWindow(Display parent, boolean closeBtn, boolean maxBtn, boolean minBtn, boolean autoclose)
  {
    Window frame = new Window(closeBtn, maxBtn, minBtn, autoclose);
    FengGUI.setUpAppearance(frame);
    parent.addWidget(frame);
    return frame;
  }

  /**
   * Creates a window
   * 
   * @param autoclose
   * @param closeBtn whether the window has close button or not
   * @param maxBtn whether the window has a maximize button or not
   * @param minBtn whether the windows has a minimize button or not
   * @return new window
   */
  public static Window createWindow(boolean closeBtn, boolean maxBtn, boolean minBtn, boolean autoclose)
  {
    Window frame = new Window(closeBtn, maxBtn, minBtn, autoclose);
    FengGUI.setUpAppearance(frame);
    return frame;
  }

  /**
   * Creates a new a new frame. A frame is a window with a close button,
   * a minimize button and a maximize button.
   * @param parent the Display
   * @param text the title of the frame
   * @return the new frame
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Window createFrame(Display parent, String text, boolean autoclose)
  {
    Window frame = createWindow(parent, true, true, true, autoclose);
    frame.setTitle(text);
    return frame;
  }

  /**
   * Creates a new Dialog. A Dialog is a Window that only has a close
   * button.
   * @param parent the Display
   * @return new Dialog
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Window createDialog(Display parent)
  {
    Window frame = createWindow(parent, true, false, false, true);
    return frame;
  }

  /**
   * Creates a new Dialog. A Dialog is a Window with only a close button.
   * 
   * @return new Dialog.
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Window createDialog()
  {
    Window frame = createWindow(true, false, false, true);
    return frame;
  }

  /**
   * Creates a new Dialog. A Dialog is a Window that has only a close button.
   * @param parent the Display
   * @param title the title of the Dialog
   * @return new Dialog
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Window createDialog(Display parent, String title)
  {
    Window frame = createDialog(parent);
    frame.setTitle(title);
    return frame;
  }

  /**
   * Creates a new Slider.
   * @param parent the parent container
   * @param horizontal whether the Slider lays horizontal or vertical
   * @return new Slider
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Slider createSlider(boolean horizontal)
  {
    Slider s = new Slider(horizontal);
    FengGUI.setUpAppearance(s);
    return s;
  }

  /**
   * 
   * @param parent
   * @param horizontal
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Slider createSlider(IContainer parent, boolean horizontal)
  {
    Slider s = createSlider(horizontal);
    parent.addWidget(s);
    return s;
  }

  /**
   * Creates a new ScrollContainer.
   * @param parent the parent container.
   * @return new ScrollContainer
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static ScrollContainer createScrollContainer(IContainer parent)
  {
    if (ptScrollContainer == null)
    {
      ptScrollContainer = new ScrollContainer();
      FengGUI.setUpAppearance(ptScrollContainer);
    }
    ScrollContainer c = new ScrollContainer(ptScrollContainer);
    parent.addWidget(c);
    return c;
    //		parent.addWidget(ptScrollContainer);
    //		return ptScrollContainer;
  }

  /**
   * Creates a new TextEditor.
   * @return new TextEditor
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static TextEditor createTextEditor()
  {
    if (ptTextEditor == null)
    {
      ptTextEditor = new TextEditor();
      FengGUI.setUpAppearance(ptTextEditor);
    }
    TextEditor c = new TextEditor(ptTextEditor);
    return c;
  }

  /**
   * Creates a new TextEditor.
   * @param parent the parent container
   * @return new TextEditor
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static TextEditor createTextEditor(IContainer parent)
  {
    TextEditor c = createTextEditor();
    parent.addWidget(c);
    return c;
  }

  /**
   * Creates a new TextEditor.
   * @param parent the parent container
   * @param text the text inside of the TextEditor
   * @return new TextEditor
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static TextEditor createTextEditor(IContainer parent, String text)
  {
    TextEditor c = createTextEditor(parent);
    c.setText(text);
    return c;
  }

  /**
   * Creates a new plain Canvas.
   * @param parent the parent container
   * @return new Canvas
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static Canvas createCanvas(IContainer parent)
  {
    Canvas w = new Canvas();
    FengGUI.setUpAppearance(w);
    parent.addWidget(w);
    return w;
  }

  /**
   * Creates a new plain Widget.
   * @param parent the parent container
   * @return new Widget
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static IWidget createWidget(IContainer parent)
  {
    Widget w = new Widget();
    FengGUI.setUpAppearance(w);
    parent.addWidget(w);
    return w;
  }

  /**
   * Creates a new ScrollBar.
   * @param parent the parent container
   * @param horizontal whether the ScrollBar is horizontal or vertical
   * @return new ScrollBar
   */
  public static ScrollBar createScrollBar(boolean horizontal)
  {
    ScrollBar c;
    if (horizontal)
    {
      if (ptScrollBarH == null)
      {
        ptScrollBarH = new ScrollBar(horizontal);
        FengGUI.setUpAppearance(ptScrollBarH);
      }
      c = ptScrollBarH.clone();
    }
    else
    {
      if (ptScrollBarV == null)
      {
        ptScrollBarV = new ScrollBar(horizontal);
        FengGUI.setUpAppearance(ptScrollBarV);
      }
      c = ptScrollBarV.clone();
    }

    return c;
  }

  /**
   * Creates a new TextField.
   * @param parent parent container
   * @return new text field
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static TextEditor createTextField(IContainer parent)
  {
    TextEditor tf = createTextEditor(parent);
    return tf;
  }

  /**
   * Creates a new TextField.
   * @param parent parent container
   * @param text text set withing text field
   * @return new text field
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static TextEditor createTextField(IContainer parent, String text)
  {
    TextEditor td = createTextField(parent);
    td.setText(text);
    return td;
  }

  /**
   * 
   * @param parent
   * @param horizontal
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static SplitContainer createSplitContainer(IContainer parent, boolean horizontal)
  {
    SplitContainer sc = new SplitContainer(horizontal);
    FengGUI.setUpAppearance(sc);
    parent.addWidget(sc);
    return sc;
  }

  /**
   * 
   * @param horizontal
   * @return
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static SplitContainer createSplitContainer(boolean horizontal)
  {
    SplitContainer sc = new SplitContainer(horizontal);
    FengGUI.setUpAppearance(sc);
    return sc;
  }

  /**
   * Creates a new ViewPort.
   * @param parent the parent container
   * @return new view port
   * @deprecated  Use the generic {@link #createWidget(Class)} instead.
   */
  public static ViewPort createViewPort(IContainer parent)
  {
    ViewPort p = new ViewPort();
    FengGUI.setUpAppearance(p);
    parent.addWidget(p);
    return p;
  }

  /**
   * returns the Theme currently in use. This may be null as well if no theme is set.
   *  
   * @return
   */
  public static ITheme getTheme()
  {
    return theme;
  }

  /**
   * Set the Theme that should be used by this factory class. Set it to null for no theme.
   * 
   * @param theme
   * @see XMLTheme
   * @see DefaultTheme
   */
  public static void setTheme(ITheme theme)
  {
    FengGUI.theme = theme;
    removePrototypes();
  }

  /**
   * Sets up the the appearance of a Widget. It first calls <code>initAppearance</code>
   * and then applies the theme (if available).
   * 
   * @param toBeSetUp the Widget to be set up
   * @return the Widget
   */
  protected static IWidget setUpAppearance(IWidget toBeSetUp)
  {
    if (theme != null)
      theme.setUp(toBeSetUp);
    return toBeSetUp;
  }
}
