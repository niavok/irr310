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
 * Created on Jan 18, 2007
 * $Id:XMLTheme.java 323 2007-08-11 10:11:38Z Schabby $
 */
package org.fenggui.theme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.Button;
import org.fenggui.Canvas;
import org.fenggui.CheckBox;
import org.fenggui.ComboBox;
import org.fenggui.Container;
import org.fenggui.DecoratorLayer;
import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.Label;
import org.fenggui.ProgressBar;
import org.fenggui.RadioButton;
import org.fenggui.ScrollBar;
import org.fenggui.ScrollContainer;
import org.fenggui.Slider;
import org.fenggui.SplitContainer;
import org.fenggui.StandardWidget;
import org.fenggui.TextEditor;
import org.fenggui.binding.render.AWTFont;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.composite.Window;
import org.fenggui.composite.menu.Menu;
import org.fenggui.composite.menu.MenuBar;
import org.fenggui.decorator.PixmapDecorator;
import org.fenggui.decorator.background.FunnyBackground;
import org.fenggui.decorator.background.GradientBackground;
import org.fenggui.decorator.background.PixmapBackground;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.decorator.border.BevelBorder;
import org.fenggui.decorator.border.PixmapBorder;
import org.fenggui.decorator.border.PixmapBorder16;
import org.fenggui.decorator.border.PixmapBorder16FixedCenters;
import org.fenggui.decorator.border.PlainBorder;
import org.fenggui.decorator.border.RoundedBorder;
import org.fenggui.decorator.border.TitledBorder;
import org.fenggui.decorator.switches.SetMarginSwitch;
import org.fenggui.decorator.switches.SetPixmapSwitch;
import org.fenggui.decorator.switches.SetTextColorSwitch;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.FormLayout;
import org.fenggui.layout.GridLayout;
import org.fenggui.layout.RowLayout;
import org.fenggui.layout.StaticLayout;
import org.fenggui.text.EditableTextContentManager;
import org.fenggui.text.TextContentManager;
import org.fenggui.text.content.factory.simple.ContentFactory;
import org.fenggui.theme.xml.GlobalContextHandler;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.theme.xml.TypeRegister;
import org.fenggui.theme.xml.XMLInputStream;
import org.fenggui.tooltip.LabelTooltipDecorator;
import org.fenggui.tooltip.LabelTooltipManager;
import org.fenggui.util.Color;
import org.fenggui.util.Log;
import org.fenggui.util.jdom.Document;
import org.fenggui.util.jdom.Element;
import org.fenggui.util.jdom.Reader;

/**
 * Loads a theme from a XML file. 
 * 
 * Please note that you can switch to loading via
 * the class loader by enabling Binding.setUseClassLoader(true). 
 * 
 * @author Johannes Schaback, last edited by $Author:Schabby $, $Date:2007-08-11 12:11:38 +0200 (Sat, 11 Aug 2007) $
 * @version $Revision:323 $
 */
public class XMLTheme implements ITheme
{
  private Document                 document                         = null;
  private final List<String>       warnings                         = new ArrayList<String>();
  private String                   resourcePath                     = null;
  private GlobalContextHandler     contextHandler                   = null;

  public static final TypeRegister TYPE_REGISTRY                    = new TypeRegister();
  public static final TypeRegister COMPLEXTEXTRENDERERDATA_REGISTRY = new TypeRegister();
  public static final TypeRegister TEXTRENDERER_REGISTRY            = new TypeRegister();
  public static final TypeRegister FONT_REGISTRY                    = new TypeRegister();
  public static final TypeRegister TOOLTIPDATA_REGISTRY             = new TypeRegister();
  public static final TypeRegister TOOLTIPMANAGER_REGISTRY          = new TypeRegister();

  static
  {
    //Decorators
    TYPE_REGISTRY.register(PixmapDecorator.class);

    TYPE_REGISTRY.register(PixmapBackground.class);
    TYPE_REGISTRY.register(FunnyBackground.class);
    TYPE_REGISTRY.register(GradientBackground.class);
    TYPE_REGISTRY.register(PlainBackground.class);

    TYPE_REGISTRY.register(BevelBorder.class);
    TYPE_REGISTRY.register(PixmapBorder.class);
    TYPE_REGISTRY.register(PixmapBorder16.class);
    TYPE_REGISTRY.register(PixmapBorder16FixedCenters.class);
    TYPE_REGISTRY.register(PlainBorder.class);
    TYPE_REGISTRY.register(TitledBorder.class);
    TYPE_REGISTRY.register(RoundedBorder.class);

    //Tooltip stuff
    TYPE_REGISTRY.register("Tooltip", LabelTooltipDecorator.class);
    TYPE_REGISTRY.register("TooltipManager", LabelTooltipManager.class);

    //Switches
    TYPE_REGISTRY.register("PixmapSwitch", SetPixmapSwitch.class);
    TYPE_REGISTRY.register("MarginSwitch", SetMarginSwitch.class);
    TYPE_REGISTRY.register("TextColorSwitch", SetTextColorSwitch.class);

    //Widgets
    TYPE_REGISTRY.register(Window.class);
    TYPE_REGISTRY.register(Menu.class);
    TYPE_REGISTRY.register(MenuBar.class);
    TYPE_REGISTRY.register(Button.class);
    TYPE_REGISTRY.register(Canvas.class);
    TYPE_REGISTRY.register(CheckBox.class);
    TYPE_REGISTRY.register(ComboBox.class);
    TYPE_REGISTRY.register(Container.class);
    TYPE_REGISTRY.register(Display.class);
    TYPE_REGISTRY.register(Label.class);
    TYPE_REGISTRY.register(org.fenggui.List.class);
    TYPE_REGISTRY.register(ProgressBar.class);
    TYPE_REGISTRY.register(RadioButton.class);
    TYPE_REGISTRY.register(ScrollBar.class);
    TYPE_REGISTRY.register(ScrollContainer.class);
    TYPE_REGISTRY.register(Slider.class);
    TYPE_REGISTRY.register(SplitContainer.class);
    TYPE_REGISTRY.register(TextEditor.class);

    //Other things
    TYPE_REGISTRY.register(DecoratorLayer.class);
    TYPE_REGISTRY.register(Pixmap.class);
    TYPE_REGISTRY.register(Color.class);

    //Layout managers
    TYPE_REGISTRY.register(GridLayout.class);
    TYPE_REGISTRY.register(BorderLayout.class);
    TYPE_REGISTRY.register(FormLayout.class);
    TYPE_REGISTRY.register(RowLayout.class);
    TYPE_REGISTRY.register(StaticLayout.class);

    //Text renderer things
    TYPE_REGISTRY.register(ImageFont.class);
    TYPE_REGISTRY.register(AWTFont.class);

    TYPE_REGISTRY.register(DirectTextRenderer.class);

    TYPE_REGISTRY.register("ComplexTextRendererData", TextContentManager.class);
    TYPE_REGISTRY.register("AdvancedTextRendererData", EditableTextContentManager.class);

    TYPE_REGISTRY.register(ContentFactory.class);

    //SIMPLE TEXT RENDERERS
    TEXTRENDERER_REGISTRY.register(DirectTextRenderer.class);

    //COMPLEX TEXT RENDERER DATA
    COMPLEXTEXTRENDERERDATA_REGISTRY.register("ComplexTextRendererData", TextContentManager.class);
    COMPLEXTEXTRENDERERDATA_REGISTRY.register("AdvancedTextRendererData", EditableTextContentManager.class);

    //FONTS
    FONT_REGISTRY.register(ImageFont.class);
    FONT_REGISTRY.register(AWTFont.class);

    //TOOLTIP
    TOOLTIPDATA_REGISTRY.register("Tooltip", LabelTooltipDecorator.class);
    TOOLTIPMANAGER_REGISTRY.register("TooltipManager", LabelTooltipManager.class);
  }

  public XMLTheme(String xmlThemeFile) throws IOException, IXMLStreamableException
  {
    /* extract path to xml file */
    if (xmlThemeFile.indexOf('/') != -1)
      this.resourcePath = xmlThemeFile.substring(0, xmlThemeFile.lastIndexOf('/') + 1);
    else
      this.resourcePath = xmlThemeFile.substring(0, xmlThemeFile.lastIndexOf('\\') + 1);

    /* parse the XML file in out JDOM */
    Reader r = new Reader();
    document = r.parse(Binding.getInstance().getResource(xmlThemeFile));

    /* will throw an exception if the bnding has not been initialized yet
       see http://www.jmonkeyengine.com/jmeforum/index.php?topic=4483.15 */
    Binding.getInstance();

    Element el = document.getChild("FengGUI:init");

    if (el != null)
    {

      XMLInputStream xis = new XMLInputStream(el);
      xis.setResourcePath(resourcePath);
      List<IXMLStreamable> contents = new ArrayList<IXMLStreamable>();
      xis.processChildren(contents, XMLTheme.TYPE_REGISTRY);
      handleWarnings(xis);
      contextHandler = xis.getContextHandler();
    }
    else
    {
      contextHandler = new GlobalContextHandler();
    }

    contextHandler.setRootDocument(document);
  }

  public Document getRoot()
  {
    return document;
  }

  @SuppressWarnings("unchecked")
  private String findSupertype(IWidget w)
  {
    Class clazz = w.getClass();
    String s = null;
    while (s == null && !clazz.equals(Object.class))
    {
      s = XMLTheme.TYPE_REGISTRY.getName(clazz);
      clazz = clazz.getSuperclass();
    }
    return s;
  }

  public void setUp(IWidget widget)
  {
    if (!(widget instanceof StandardWidget))
    {
      throw new IllegalArgumentException("widget " + widget.getClass().getCanonicalName() + " is not a StandardWidget!");
    }

    StandardWidget w = (StandardWidget) widget;

    String type = findSupertype(widget);

    if (type == null)
    {
      Log.warn(widget.getClass().getCanonicalName()
        + " is not registered in org.theme.XMLTheme.TYPE_REGISTRY");
      return;
    }

    Element el = document.getChild(type);

    if (el == null)
    {
      Log.warn("<" + type + "> could not be found in theme definition file");
      return;
    }

    XMLInputStream xis = new XMLInputStream(el);
    xis.setContextHandler(contextHandler);
    xis.setResourcePath(resourcePath);

    try
    {
      w.process(xis);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (IXMLStreamableException e)
    {
      throw new RuntimeException(e);
    }

    handleWarnings(xis);
  }

  public List<String> getWarnings()
  {
    return warnings;
  }

  private void handleWarnings(InputOutputStream stream)
  {
    String warningsStr = stream.getWarningsAsString().trim();
    if (warningsStr.length() > 0)
      Log.warn(warningsStr);
    warnings.addAll(stream.getWarnings());
  }
}
