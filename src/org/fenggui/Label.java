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
 * Created on 2005-3-2
 * $Id: Label.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.appearance.LabelAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.text.ITextContentManager;
import org.fenggui.text.TextContentManager;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Widget for displaying a line of text and/or a pixmap. This widget is passive and does not
 * react on anything.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date:
 *         2007-11-28 11:16:38 +0100 (Mi, 28 Nov 2007) $
 * @version $Revision: 614 $
 */
public class Label extends StandardWidget implements ILabel, Cloneable
{
  private Pixmap                 pixmap              = null;
  private LabelAppearance        appearance          = null;
  private ITextContentManager    textData            = null;

  protected ISizeChangedListener sizeChangedListener;

  /**
   * Creates a new empty label
   * 
   */
  public Label()
  {
    this("");
  }

  /**
   * Creates a new label with a given text.
   * 
   * @param text
   *          the text
   */
  public Label(String text)
  {
    initTextManager();
    setAppearance(new LabelAppearance(this));
    setText(text);
  }

  /**
   * Copy constructor.
   * 
   * @param widget
   */
  public Label(Label widget)
  {
    super(widget);
    initTextManager();
    
    this.pixmap = widget.pixmap;
    setAppearance(new LabelAppearance(this, widget.appearance));
  }

  public void setAppearance(LabelAppearance appearance)
  {
    this.appearance = appearance;
    textData.adaptChange(appearance.getContentWidth(), appearance.getContentHeight(), getAppearance());
    updateMinSize();
  }

  private void initTextManager()
  {
    sizeChangedListener = new ISizeChangedListener()
    {

      public void sizeChanged(SizeChangedEvent event)
      {
        Label.this.updateMinSize();
      }

    };
    
    textData = new TextContentManager();
    textData.addSizeChangedListener(sizeChangedListener);
  }
  
  @Override
  public LabelAppearance getAppearance()
  {
    return appearance;
  }

  public Pixmap getPixmap()
  {
    return pixmap;
  }

  public void setPixmap(Pixmap pixmap)
  {
    this.pixmap = pixmap;
    updateMinSize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  @Override
  public void sizeChanged(SizeChangedEvent event)
  {
    textData.adaptChange(getAppearance().getContentWidth(), getAppearance().getContentHeight(), getAppearance());
    super.sizeChanged(event);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.ITextWidget#getText()
   */
  public String getText()
  {
    return textData.getContent();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.ITextWidget#setText(java.lang.String)
   */
  public void setText(String text)
  {
    textData.setContent(text, getAppearance());
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    setText(stream.processAttribute("text", getText(), getText()));

    if (stream.isInputStream()) // XXX: only support read-in at the moment :(
      pixmap = stream.processChild("Pixmap", pixmap, null, Pixmap.class);
  }

  @Override
  public Dimension getMinContentSize()
  {
    int width = 0;
    int height = 0;

    if (!textData.isEmpty())
    {
      width = textData.getSize().getWidth();
      height = textData.getSize().getHeight();

      if (textData.isWordWarping())
      {
        width = 5;
        height = 5;
      }
    }

    if (pixmap != null)
    {
      width += pixmap.getWidth();
      if (!textData.isEmpty())
        width += getAppearance().getGap();
      height = Math.max(pixmap.getHeight(), height);
    }

    return new Dimension(width, height);
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;

    int contentWidth = getAppearance().getContentWidth();
    int contentHeight = getAppearance().getContentHeight();

    if (pixmap != null)
    {
      width = pixmap.getWidth();
      height = pixmap.getHeight();
      if (!textData.isEmpty())
        width += getAppearance().getGap();
    }
    else if (textData.isEmpty())
      return;

    if (!textData.isEmpty())
    {
      width += textData.getSize().getWidth();
      height = Math.max(height, textData.getSize().getHeight());
    }

    x = x + getAppearance().getAlignment().alignX(contentWidth, width);

    if (pixmap != null)
    {
      g.setColor(Color.WHITE);
      y = getAppearance().getAlignment().alignY(contentHeight, pixmap.getHeight());
      g.drawImage(pixmap, x, y);
      x += pixmap.getWidth() + getAppearance().getGap();
    }

    if (!textData.isEmpty())
    {
      y = getAppearance().getAlignment().alignY(contentHeight, textData.getSize().getHeight())
          + textData.getSize().getHeight();
      textData.render(x, y, g, getAppearance());
    }
  }

  public boolean isMultiline()
  {
    return textData.isMultiline();
  }

  public boolean isWordWarping()
  {
    return textData.isWordWarping();
  }

  public void setMultiline(boolean multiline)
  {
    textData.setMultiline(multiline, appearance);
  }

  public void setWordWarping(boolean warp)
  {
    textData.setWordWarping(warp, appearance);
  }

  public ITextContentManager getTextRendererData()
  {
    return textData;
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#clone()
   */
  @Override
  public Label clone()
  {
    Label result = (Label) super.clone();
    
    //TODO: make text clonable
    //result.textData = this.textData.clone();
    result.initTextManager();

    result.appearance = new LabelAppearance(result, this.appearance);
    
    result.setText(this.getText());
    
    updateMinSize();
    return result ;
  }
}
