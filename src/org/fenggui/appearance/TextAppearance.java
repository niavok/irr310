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
 * Created on 22.10.2007
 * $Id$
 */
package org.fenggui.appearance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;

import org.fenggui.StandardWidget;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TextAppearance extends DefaultAppearance
{
  public final static String         DEFAULTTEXTRENDERER = "DefaultTextRenderer";

  private Map<String, ITextRenderer> renderers;
  private Alignment                  alignment;
  private Map<String, TextStyle>     styles;

  public TextAppearance(StandardWidget w, TextAppearance appearance)
  {
    super(w, appearance);
    setupDefaults();
    renderers.putAll(appearance.renderers);
    styles.putAll(appearance.styles);
    this.alignment = appearance.getAlignment();

    setDefaultRenderer();
  }

  public TextAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super(w, stream);
  }

  public TextAppearance(StandardWidget w)
  {
    super(w);
    setupDefaults();
    setDefaultRenderer();
  }

  private void setupDefaults()
  {
    renderers = new HashMap<String, ITextRenderer>();
    styles = new HashMap<String, TextStyle>();
    alignment = Alignment.LEFT;
  }

  private void setDefaultRenderer()
  {
    if (!renderers.containsKey(ITextRenderer.DEFAULTTEXTRENDERERKEY))
      renderers.put(ITextRenderer.DEFAULTTEXTRENDERERKEY, ITextRenderer.DEFAULTRENDERER);

    if (!styles.containsKey(TextStyle.DEFAULTSTYLEKEY))
      styles.put(TextStyle.DEFAULTSTYLEKEY, new TextStyle());
  }

  /* (non-Javadoc)
   * @see org.fenggui.appearance.DecoratorAppearance#process(org.fenggui.theme.xml.InputOutputStream)
   */
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    setupDefaults();
    
    alignment = stream.processEnum("alignment", alignment, Alignment.MIDDLE, Alignment.class, Alignment.STORAGE_FORMAT);

    renderers.clear();
    stream.processChildren(renderers, XMLTheme.TEXTRENDERER_REGISTRY);
    styles.clear();
    stream.processChildren("TextStyle", styles, TextStyle.class);

    setDefaultRenderer();
  }

  /**
   * @return Returns the renderer.
   */
  public ITextRenderer getRenderer(String name)
  {
    if (renderers.containsKey(name))
      return renderers.get(name);

    if (ITextRenderer.DEFAULTTEXTRENDERERKEY.equals(name))
      throw new MissingResourceException("Default TextRenderer not defined in appearance.", "ITextRenderer",
          ITextRenderer.DEFAULTTEXTRENDERERKEY);
    else
      return getRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY);
  }

  public TextStyle getStyle(String key)
  {
    if (styles.containsKey(key))
      return styles.get(key);

    if (TextStyle.DEFAULTSTYLEKEY.equals(key))
      throw new MissingResourceException("Default Style not defined in appearance", "TextStyle",
          TextStyle.DEFAULTSTYLEKEY);
    else
      return getStyle(TextStyle.DEFAULTSTYLEKEY);
  }

  public boolean hasDefaultStyle()
  {
    return styles.containsKey(TextStyle.DEFAULTSTYLEKEY);
  }

  public void addStyle(String key, TextStyle style)
  {
    removeStyle(key);

    styles.put(key, style);
  }

  public void removeStyle(String key)
  {
    styles.remove(key);
  }

  /**
   * @param renderer The renderer to set.
   */
  public void addRenderer(String key, ITextRenderer renderer)
  {
    //remove renderers with same name
    removeRenderer(key);

    //add new renderer
    this.renderers.put(key, renderer);
  }

  public void removeRenderer(String key)
  {
    renderers.remove(key);
  }

  /**
   * @return Returns the alignment.
   */
  public Alignment getAlignment()
  {
    return alignment;
  }

  /**
   * @param alignment The alignment to set (not null).
   */
  public void setAlignment(Alignment alignment)
  {
    this.alignment = alignment;
  }

  /* (non-Javadoc)
   * @see org.fenggui.appearance.DefaultAppearance#clone(org.fenggui.StandardWidget)
   */
  @Override
  public TextAppearance clone(StandardWidget widget)
  {
    TextAppearance result = (TextAppearance) super.clone(widget);

    result.renderers = new HashMap<String, ITextRenderer>(this.renderers.size());
    result.styles = new HashMap<String, TextStyle>(this.styles.size());
    
    //Renderers should be fixed objects, for new renderer a new object needs to be created
    result.renderers.putAll(this.renderers);
    
    for (Entry<String, TextStyle> style: this.styles.entrySet())
    {
      result.styles.put(style.getKey(), style.getValue().clone());
    }

    return result;
  }

}
