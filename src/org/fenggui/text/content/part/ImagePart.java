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
 * Created on 19.02.2008
 * $Id$
 */
package org.fenggui.text.content.part;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.text.content.IContentFactory;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Represents an image within the content.
 * 
 * @author marc menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ImagePart extends AbstractContentPart
{
  public Pixmap          img            = null;
  public int             activePosition = -1;
  public IContentFactory factory;

  public ImagePart(Pixmap image, int beforeLength, int afterLength, String styleKey)
  {
    super(beforeLength, afterLength, styleKey);
    this.img = image;
    setSize(new Dimension(image.getWidth(), image.getHeight()));
  }

  /**
   * 
   */
  public ImagePart(Pixmap image, String styleKey)
  {
    this(image, 0, 0, styleKey);
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#addChar(char)
   */
  @Override
  public boolean addChar(char c, TextAppearance appearance)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#canMerge(org.fenggui.binding.render.text.advanced.AbstractContentPart)
   */
  @Override
  public boolean canMerge(AbstractContentPart part)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#getActivePosition()
   */
  @Override
  public int getActivePosition(TextAppearance appearance)
  {
    return activePosition;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#getAtomCount()
   */
  @Override
  public int getAtomCount()
  {
    if (img == null)
      return 0;
    else
      return 1;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#getAtomPosition(int)
   */
  @Override
  public int getAtomPosition(int atom, TextAppearance appearance)
  {
    if (atom == 0)
    {
      return 0;
    }
    else
    {
      return img.getWidth();
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#getContent()
   */
  @Override
  public void getContent(StringBuilder result)
  {
    if (img != null)
      result.append(factory.getContentObject(img));
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#hasActiveAtom()
   */
  @Override
  public boolean hasActiveAtom()
  {
    return activePosition != -1;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#isBreakable()
   */
  @Override
  public boolean isSplittable()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#isEmpty()
   */
  @Override
  public boolean isEmpty()
  {
    return img == null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#mergePart(org.fenggui.binding.render.text.advanced.AbstractContentPart)
   */
  @Override
  public void mergePart(AbstractContentPart part, TextAppearance appearance)
  {
    throw new UnsupportedOperationException("No Merging supported in ImagePart.");
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#removeNextChar()
   */
  @Override
  public Character removeNextAtom(TextAppearance appearance)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#removePreviousChar()
   */
  @Override
  public Character removePreviousAtom(TextAppearance appearance)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#render(int, int, org.fenggui.binding.render.Graphics)
   */
  @Override
  public void render(int x, int y, Graphics g, TextAppearance appearance)
  {
    g.setColor(Color.WHITE);
    g.drawImage(img, x, y - img.getHeight());
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#setActiveAtom(int)
   */
  @Override
  public void setActiveAtom(int atom)
  {
    activePosition = atom;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#setActivePosition(int)
   */
  @Override
  public int calculatePositionInAtoms(int x, TextAppearance appearance)
  {
    if (x < img.getWidth() / 2)
      return 0;
    else
      return 1;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#splitAt(int, boolean)
   */
  @Override
  public AbstractContentPart splitAtWord(int width, boolean firstPart, TextAppearance appearance)
  {
    throw new UnsupportedOperationException("No split supported in ImagePart.");
  }

  @Override
  public AbstractContentPart splitAtChar(int width, TextAppearance appearance)
  {
    throw new UnsupportedOperationException("No split supported in ImagePart.");
  }

  @Override
  public boolean addContent(String content, TextAppearance appearance)
  {
    return false;
  }

  @Override
  public AbstractContentPart splitAtAtom(int atom, TextAppearance appearance)
  {
    throw new UnsupportedOperationException("No split supported in ImagePart.");
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.part.AbstractContentPart#getActiveAtom()
   */
  @Override
  public int getActiveAtom()
  {
    return activePosition;
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.part.AbstractContentPart#splittAtActivePosition(org.fenggui.appearance.TextAppearance)
   */
  @Override
  public AbstractContentPart splittAtActivePosition(TextAppearance appearance)
  {
    throw new UnsupportedOperationException("No split supported in ImagePart.");
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.part.AbstractContentPart#isValidCharacter(char, org.fenggui.appearance.TextAppearance)
   */
  @Override
  public boolean isValidCharacter(char character, TextAppearance appearance)
  {
    return false;
  }

}
