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
 * Created on Feb 23, 2007
 * $Id: DirectTextRenderer.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.binding.render.text;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.CharacterPixmap;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Renders lines of text by drawing each character directly as a single
 * quad. Use this TextRenderer if you update your label (or text)
 * frequently.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class DirectTextRenderer implements ITextRenderer
{
  private ImageFont font = null;

  public DirectTextRenderer()
  {
    this(ImageFont.getDefaultFont());
  }

  public DirectTextRenderer(ImageFont font)
  {
    this.font = font;
  }

  public DirectTextRenderer(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    this.process(stream);
  }

  public void render(int x, int y, String[] texts, Color color, Graphics g)
  {
    if (texts == null || texts.length == 0)
      return;

    IOpenGL gl = g.getOpenGL();

    int localX = x + g.getTranslation().getX();
    int localXbase = localX;
    int localY = y + g.getTranslation().getY() - getLineHeight();

    g.setColor(color);
    gl.enableTexture2D(true);

    CharacterPixmap pixmap;

    boolean init = true;

    for (String text : texts)
    {
      for (int i = 0; i < text.length(); i++)
      {
        final char c = text.charAt(i);
        if (c == '\r' || c == '\f' || c == '\t')
          continue;
        else if (c == ' ')
        {
          localX += getFont().getWidth(' ');
          continue;
        }
        pixmap = font.getCharPixMap(c);

        if (init)
        {
          ITexture tex = pixmap.getTexture();

          if (tex.hasAlpha())
          {
            gl.setTexEnvModeModulate();
          }

          tex.bind();
          gl.startQuads();
          init = false;
        }

        final int imgWidth = pixmap.getWidth();
        final int imgHeight = pixmap.getHeight();

        final float endY = pixmap.getEndY();
        final float endX = pixmap.getEndX();

        final float startX = pixmap.getStartX();
        final float startY = pixmap.getStartY();

        gl.texCoord(startX, endY);
        gl.vertex(localX, localY);

        gl.texCoord(startX, startY);
        gl.vertex(localX, imgHeight + localY);

        gl.texCoord(endX, startY);
        gl.vertex(imgWidth + localX, imgHeight + localY);

        gl.texCoord(endX, endY);
        gl.vertex(imgWidth + localX, localY);

        localX += pixmap.getCharWidth();
      }
      //move to start of next line
      localY -= font.getHeight();
      localX = localXbase;
    }
    if (!init)
      gl.end();
    gl.enableTexture2D(false);
  }

  public ImageFont getFont()
  {
    return font;
  }

  public void setFont(ImageFont font)
  {
    this.font = font;
  }

  public DirectTextRenderer copy()
  {
    DirectTextRenderer result = new DirectTextRenderer(this.font);
    return result;
  }

  public Dimension calculateSize(String[] text)
  {
    int fontHeight = font.getHeight();
    int height = fontHeight;
    int width = 0;

    if (text != null && text.length > 0)
    {
      for (String line : text)
      {
        width = Math.max(width, this.font.getWidth(line));
      }
      height = text.length * fontHeight;
    }

    return new Dimension(width, height);
  }

  public int getLineHeight()
  {
    return font.getHeight();
  }

  public boolean isValidChar(char c)
  {
    return font.isCharacterMapped(c);
  }

  public int getWidth(String text)
  {
    return font.getWidth(text);
  }

  public String getUniqueName()
  {
    return IXMLStreamable.GENERATE_NAME;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    setFont(stream.processChild("Font", font, ImageFont.getDefaultFont(), ImageFont.class));
  }

  public int getWidth(char text)
  {
    return font.getWidth(text);
  }

  public void setFont(IFont font)
  {
    if (font instanceof ImageFont)
    {
      this.setFont((ImageFont) font);
    }
    else
    {
      throw new IllegalArgumentException("Only an ImageFont can be used with a DirectTextRenderer.");
    }
  }

  public void render(int x, int y, String text, Color color, Graphics g)
  {
    if (text == null || text.length() == 0 || text.trim().length() == 0)
      return;

    //		Dimension size = this.calculateSize(text);

    IOpenGL gl = g.getOpenGL();
    int localX = x + g.getTranslation().getX();
    int localY = y + g.getTranslation().getY() - getLineHeight();

    g.setColor(color);
    gl.enableTexture2D(true);

    CharacterPixmap pixmap = null;

    boolean init = true;

    for (int i = 0; i < text.length(); i++)
    {
      final char c = text.charAt(i);
      if (c == '\r' || c == '\f' || c == '\t')
        continue;
      else if (c == ' ')
      {
        localX += getFont().getWidth(' ');
        continue;
      }
      pixmap = font.getCharPixMap(c);

      if (init)
      {
        ITexture tex = pixmap.getTexture();

        if (tex.hasAlpha())
        {
          gl.setTexEnvModeModulate();
        }

        tex.bind();
        gl.startQuads();
        init = false;
      }

      final int imgWidth = pixmap.getWidth();
      final int imgHeight = pixmap.getHeight();

      final float endY = pixmap.getEndY();
      final float endX = pixmap.getEndX();

      final float startX = pixmap.getStartX();
      final float startY = pixmap.getStartY();

      gl.texCoord(startX, endY);
      gl.vertex(localX, localY);

      gl.texCoord(startX, startY);
      gl.vertex(localX, imgHeight + localY);

      gl.texCoord(endX, startY);
      gl.vertex(imgWidth + localX, imgHeight + localY);

      gl.texCoord(endX, endY);
      gl.vertex(imgWidth + localX, localY);

      localX += pixmap.getCharWidth();
    }

    if (!init)
      gl.end();
    gl.enableTexture2D(false);
  }

  public int getLineSpacing()
  {
    return 0;
  }

}
