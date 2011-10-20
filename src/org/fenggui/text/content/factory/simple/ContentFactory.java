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
 * Created on 22.11.2007
 * $Id$
 */
package org.fenggui.text.content.factory.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.text.ITextCursorRenderer;
import org.fenggui.text.content.IContentFactory;
import org.fenggui.text.content.part.AbstractContentPart;
import org.fenggui.text.content.part.TextPart;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

/**
 * This factory uses one style and creates the content using that. The content here is plain text, no
 * special characters are available for formating. It is a very simple and straight forward
 * implementation of a ContentFactory.
 * 
 * @author marc menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentFactory implements IContentFactory, IXMLStreamable
{
  private static IContentFactory defaultFactory     = null;
  private ITextCursorRenderer    textCursorRenderer = null;

  private char                   character;
  //	private static final char DEFAULT_CHAR = (char) 0x25CF;
  private static final char      DEFAULT_CHAR       = '*';
  private String                 defaultStyleKey;

  public ContentFactory()
  {
    this.character = DEFAULT_CHAR;
    defaultStyleKey = TextStyle.DEFAULTSTYLEKEY;

    textCursorRenderer = new ITextCursorRenderer()
    {

      public void render(int x, int y, int w, int h, Graphics g)
      {
        g.setColor(Color.BLACK);
        g.drawFilledRectangle(x, y, w, h);
      }

      public int getHeight()
      {
        return DYNAMICSIZE;
      }

      public int getWidth()
      {
        return 2;
      }

    };

    if (ContentFactory.defaultFactory == null)
      ContentFactory.setDefaultFactory(this);
  }

  public ContentFactory(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    this.character = DEFAULT_CHAR;
    defaultStyleKey = TextStyle.DEFAULTSTYLEKEY;
    textCursorRenderer = new ITextCursorRenderer()
    {

      public void render(int x, int y, int w, int h, Graphics g)
      {
        g.setColor(Color.BLACK);
        g.drawFilledRectangle(x, y, w, h);
      }

      public int getHeight()
      {
        return DYNAMICSIZE;
      }

      public int getWidth()
      {
        return 2;
      }

    };

    this.process(stream);
    if (ContentFactory.defaultFactory == null)
      ContentFactory.setDefaultFactory(this);
  }

  public static IContentFactory getDefaultFactory()
  {
    if (defaultFactory == null)
      defaultFactory = new ContentFactory();

    return defaultFactory;
  }

  public static void setDefaultFactory(IContentFactory factory)
  {
    defaultFactory = factory;
  }

  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {

  }

  public AbstractContentPart getEmptyContentPart(TextAppearance appearance)
  {
    return new TextPart("", TextStyle.DEFAULTSTYLEKEY, 0, 0, appearance);
  }

  /**
   * @return the character
   */
  public char getCharacter()
  {
    return character;
  }

  /**
   * @param character
   *          the character to set
   */
  public void setCharacter(char character)
  {
    this.character = character;
  }

  public String getContentObject(Object obj)
  {
    return "";
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentFactory#getContentParts(java.lang.Object)
   */
  public List<AbstractContentPart> getContentParts(Object content, TextAppearance appearance)
  {
    if (content instanceof String)
    {
      List<AbstractContentPart> result = new ArrayList<AbstractContentPart>(1);
      result.add(new TextPart((String) content, this.defaultStyleKey, appearance));
      return result;
    }
    else
    {
      throw new IllegalArgumentException("This ContentFactory only supports input of type String.");
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentFactory#getTextCursorRenderer()
   */
  public ITextCursorRenderer getTextCursorRenderer()
  {
    return textCursorRenderer;
  }

  public void setTextCursorRenderer(ITextCursorRenderer renderer)
  {
    textCursorRenderer = renderer;
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getContentLines(java.lang.Object)
   */
  public Object[] getContentLines(Object content)
  {
    if (content instanceof String)
    {
      return ((String) content).split("\n", -1);
    }
    else
    {
      throw new IllegalArgumentException("This ContentFactory only supports input of type String.");
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#addLineEnd(java.lang.StringBuilder)
   */
  public void addLineEnd(StringBuilder result)
  {
    result.append("\n");
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#addLineStart(java.lang.StringBuilder)
   */
  public void addLineStart(StringBuilder result)
  {
    //Nothing to do here		
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#finalContent(java.lang.StringBuilder)
   */
  public void finalContent(StringBuilder result)
  {
  }
}
