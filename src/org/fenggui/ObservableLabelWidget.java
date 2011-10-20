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
 * $Id: ObservableLabelWidget.java 616 2009-04-07 12:02:56Z marcmenghin $
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
 * 
 * State-enabled Widget that helps drawing text and images.
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-28
 *         11:16:38 +0100 (Mi, 28 Nov 2007) $
 * @version $Revision: 616 $
 */
public class ObservableLabelWidget extends StatefullWidget<LabelAppearance> implements ILabel, Cloneable
{
  private Pixmap                 pixmap              = null;
  private ITextContentManager    textData;

  private ISizeChangedListener sizeChangedListener;

  /**
   * Creates a new ObservalbeWidget
   */
  public ObservableLabelWidget()
  {
    super();
    initTextData();
    this.setAppearance(new LabelAppearance(this));
    
    updateMinSize();
  }

  /**
   * Copy constructor
   * 
   * @param widget
   */
  public ObservableLabelWidget(ObservableLabelWidget widget)
  {
    super(widget);

    initTextData();
    
    if (widget != null)
    {
      this.pixmap = widget.getPixmap();
      this.setAppearance(new LabelAppearance(this, widget.getAppearance()));
      textData.setContent(widget.textData.getContent(), getAppearance());
    }
    
    updateMinSize();
  }

  private void initTextData() {
    sizeChangedListener = new ISizeChangedListener()
    {

      public void sizeChanged(SizeChangedEvent event)
      {
        ObservableLabelWidget.this.updateMinSize();
      }

    };
    
    textData = new TextContentManager();
    textData.setContent("", getAppearance());
    textData.addSizeChangedListener(this.sizeChangedListener);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.StatefullWidget#setAppearance(org.fenggui.appearance.DecoratorAppearance)
   */
  @Override
  public void setAppearance(LabelAppearance appearance)
  {
    super.setAppearance(appearance);
    textData.adaptChange(getAppearance().getContentWidth(), getAppearance().getContentHeight(), getAppearance());
    updateMinSize();
  }

  /**
   * @return Returns the text.
   */
  public String getText()
  {
    return textData.getContent();
  }

  /**
   * @param text
   *          The text to set.
   */
  public void setText(String text)
  {
    textData.setContent(text, getAppearance());
  }

  /* (non-Javadoc)
   * @see org.fenggui.IPixmapWidget#getPixmap()
   */
  public Pixmap getPixmap()
  {
    return pixmap;
  }

  /* (non-Javadoc)
   * @see org.fenggui.IPixmapWidget#setPixmap(org.fenggui.binding.render.Pixmap)
   */
  public void setPixmap(Pixmap pixmap)
  {
    this.pixmap = pixmap;

    updateMinSize();
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  @Override
  public void sizeChanged(SizeChangedEvent event)
  {
    textData.adaptChange(getAppearance().getContentWidth(), getAppearance().getContentHeight(), getAppearance());
    super.sizeChanged(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.StandardWidget#process(org.fenggui.theme.xml.InputOutputStream)
   */
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    textData.setContent(stream.processAttribute("text", textData.getContent(), ""), getAppearance());

    if (stream.isInputStream()) // XXX: only support read-in at the moment :(
    {
      pixmap = stream.processChild("Pixmap", pixmap, null, Pixmap.class);
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.StandardWidget#getMinContentSize()
   */
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

  /* (non-Javadoc)
   * @see org.fenggui.StandardWidget#paintContent(org.fenggui.binding.render.Graphics, org.fenggui.binding.render.IOpenGL)
   */
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

  /**
   * @return
   * @see org.fenggui.text.ITextContentManager#isMultiline()
   */
  public boolean isMultiline()
  {
    return textData.isMultiline();
  }

  /**
   * @return
   * @see org.fenggui.text.ITextContentManager#isWordWarping()
   */
  public boolean isWordWarping()
  {
    return textData.isWordWarping();
  }

  /**
   * @param multiline
   * @param appearance
   * @see org.fenggui.text.ITextContentManager#setMultiline(boolean, org.fenggui.appearance.TextAppearance)
   */
  public void setMultiline(boolean multiline)
  {
    textData.setMultiline(multiline, getAppearance());
  }

  /**
   * @param warp
   * @param appearance
   * @see org.fenggui.text.ITextContentManager#setWordWarping(boolean, org.fenggui.appearance.TextAppearance)
   */
  public void setWordWarping(boolean warp)
  {
    textData.setWordWarping(warp, getAppearance());
  }

  /**
   * @return
   */
  public ITextContentManager getTextRendererData()
  {
    return textData;
  }

  /* (non-Javadoc)
   * @see org.fenggui.StatefullWidget#clone()
   */
  @Override
  public ObservableLabelWidget clone()
  {
    ObservableLabelWidget result = (ObservableLabelWidget) super.clone();

    LabelAppearance app = this.getAppearance().clone(result);
    
    result.setAppearance(app);
    
    //TODO: make text data clonable as well
    //result.textData = this.textData.clone();
    result.initTextData();

    result.setText(this.getText());

    return result;
  }
}
