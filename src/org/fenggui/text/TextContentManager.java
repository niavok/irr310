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
package org.fenggui.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TextContentManager implements ITextContentManager, IXMLStreamable
{
  private String                     text                 = "";
  private int                        width                = Integer.MAX_VALUE;
  private int                        height               = Integer.MAX_VALUE;

  private String[]                   content;

  private boolean                    multiline;
  private boolean                    wordWarping;
  private String                     endMarker;
  private Dimension                  contentSizeCache;

  private List<ISizeChangedListener> sizeChangedListeners = new ArrayList<ISizeChangedListener>();

  public TextContentManager()
  {
    multiline = false;
    wordWarping = false;
    endMarker = TextUtil.ENDMARKER0;
    content = new String[0];
    contentSizeCache = new Dimension(0, 0);
  }

  public TextContentManager(TextContentManager data)
  {
    if (data != null)
    {
      this.text = data.text;
      this.multiline = data.multiline;
      this.wordWarping = data.wordWarping;
      this.width = data.width;
      this.content = data.content;
      this.endMarker = data.endMarker;
      this.contentSizeCache = data.contentSizeCache;
    }
  }

  private void updateText(ITextRenderer renderer)
  {
    if (text.length() == 0)
    {
      contentSizeCache = new Dimension(0, 0);
      content = new String[0];
      return;
    }

    String content;
    if (!multiline)
    {
      int h = text.indexOf('\n');
      if (h >= 0)
      {
        content = text.substring(0, h);
      }
      else
      {
        content = text;
      }
    }
    else
    {
      content = text;
    }

    String[] lines = content.split("\n");
    List<String> start = Arrays.asList(lines);
    List<String> result = new Vector<String>(lines.length);

    if (wordWarping && width > 0)
    {
      for (String line : start)
      {
        String part1, part2 = line;
        do
        {
          int h = TextUtil.findLineEnd(part2, renderer, width);
          if (h <= 0 || h >= part2.length())
          {
            result.add(part2);
            part2 = "";
          }
          else
          {
            if (line.charAt(h) == ' ')
              h++;

            part1 = part2.substring(0, h);
            part2 = part2.substring(h, part2.length());
            result.add(part1);
          }
        } while (part2.length() > 0);
      }
      start = result;
    }

    // build final string
    String finalResult = "";
    for (String line : start)
    {
      finalResult += line + "\n";
    }
    if (finalResult.length() > 0)
    {
      finalResult = finalResult.substring(0, finalResult.length() - 1);
    }
    this.content = finalResult.split("\n");
    Dimension oldSize = contentSizeCache;
    if (finalResult.length() <= 0)
    {
      this.contentSizeCache = new Dimension(0, 0);
    }
    else
    {
      this.contentSizeCache = renderer.calculateSize(this.content);
    }

    if (!oldSize.equals(contentSizeCache))
      fireSizeChangedListerer(new SizeChangedEvent(null, oldSize, contentSizeCache));
  }

  public void setContent(String text, TextAppearance appearance)
  {
    if (!this.text.equals(text))
    {
      this.text = text;
      updateText(getRenderer(appearance));
    }
  }

  public void setContent(Object text, TextAppearance appearance)
  {
    if (text instanceof String)
    {
      setContent((String) text, appearance);
    }
    else
    {
      throw new IllegalArgumentException("This ComplexTextRenderer only supports input of type String.");
    }
  }

  public String getContent()
  {
    return text;
  }

  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  public boolean isMultiline()
  {
    return multiline;
  }

  public boolean isWordWarping()
  {
    return wordWarping;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    this.text = stream.processAttribute("text", this.getContent(), "");
    this.endMarker = stream.processAttribute("endMarker", this.endMarker, TextUtil.ENDMARKER0);
    this.multiline = stream.processAttribute("multiline", this.multiline, false);
    this.wordWarping = stream.processAttribute("wordwarped", this.wordWarping, false);
  }

  public void setMultiline(boolean multiline, TextAppearance appearance)
  {
    if (multiline != this.multiline)
    {
      this.multiline = multiline;
      this.adaptChange(width, height, appearance);
      updateText(getRenderer(appearance));
    }
  }

  public void setWordWarping(boolean warp, TextAppearance appearance)
  {
    if (warp != this.wordWarping)
    {
      this.wordWarping = warp;
      this.adaptChange(width, height, appearance);
      updateText(getRenderer(appearance));
    }
  }

  protected void fireSizeChangedListerer(SizeChangedEvent event)
  {
    for (ISizeChangedListener sizeChanged : sizeChangedListeners)
    {
      sizeChanged.sizeChanged(event);
    }
  }

  public boolean addSizeChangedListener(ISizeChangedListener listener)
  {
    return sizeChangedListeners.add(listener);
  }

  public boolean removeSizeChangedListener(ISizeChangedListener listener)
  {
    return sizeChangedListeners.remove(listener);

  }

  public Dimension getSize()
  {
    return contentSizeCache;
  }

  public void adaptChange(int x, int y, TextAppearance appearance)
  {
    if (x != this.width)
    {
      this.width = x;
    }

    if (y != this.height)
    {
      this.height = y;
    }

    updateText(getRenderer(appearance));
  }

  private ITextRenderer getRenderer(TextAppearance appearance)
  {
    TextStyle style = appearance.getStyle(TextStyle.DEFAULTSTYLEKEY);
    return style.getTextStyleEntry(TextStyleEntry.DEFAULTSTYLESTATEKEY).resolveRenderer(appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#render(int, int, int, int, org.fenggui.binding.render.Graphics)
   */
  public void render(int x, int y, Graphics g, TextAppearance appearance)
  {
    TextStyle style = appearance.getStyle(TextStyle.DEFAULTSTYLEKEY);
    ITextRenderer renderer = getRenderer(appearance);
    renderer.render(x, y, this.content, style.getTextStyleEntry(TextStyleEntry.DEFAULTSTYLESTATEKEY).getColor(), g);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#isEmpty()
   */
  public boolean isEmpty()
  {
    return this.content.length <= 0;
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.ITextContentManager#Update()
   */
  public void Update(TextAppearance appearance)
  {
    Dimension oldSize = contentSizeCache;
    
    this.contentSizeCache = getRenderer(appearance).calculateSize(this.content);
    
    if (!oldSize.equals(contentSizeCache))
      fireSizeChangedListerer(new SizeChangedEvent(null, oldSize, contentSizeCache));
  }
}
