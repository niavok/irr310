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
 * $Id: Pixmap.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package com.irr310.i3d.fonts;

import java.io.IOException;

import com.irr310.i3d.Texture;

/**
 * A pixmap describes a rectangular area on a texture. The idea
 * behind it is that multiple pixmaps can share the same
 * texture to make the use of
 * texture space more efficient. 
 * A pixmap can also span over the whole
 * texture of course.<br/>
 * <br/>
 * Note thate in contrast to the widget space, the origin of a pixmap
 * is at the upper left corner! That makes it easier to define pixmaps
 * on textures, because the textures are usually created with external
 * tools which regard the image origin in the upper left corner. 
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class Pixmap
{

  /**
   * The texture that holds the pixmap
   */
  private Texture texture = null;

  /**
   * position of the pixmap in the texture
   */
  private int      x, y;

  /**
   * the size of the pixmap
   */
  private int      width, height;

  /**
   * Creates a new Pixmap that covers the complete image that is hold
   * in the texture.
   * @param tex the texture
   */
  public Pixmap(Texture tex)
  {
  }

  /**
   * Creates a new Pixmap that covers an area of the given texture.
   * @param texture the text
   * @param x the x coordinate of the origin of the Pixmap in the Texture
   * @param y the y coordinate of the origin of the Pixmap in the Texture
   * @param width the width of the Pixmap
   * @param height the height of the Pixmap
   */
  public Pixmap(Texture texture, int x, int y, int width, int height)
  {
    this.texture = texture;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    setTexture(texture);
  }

  public Texture getTexture()
  {
    return texture;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  /**
   * Returns the height of the Pixmap in pixels.
   * @return height
   */
  public int getHeight()
  {
    return height;
  }

  /**
   * Returns the width of the Pixmap in pixels.
   * @return width
   */
  public int getWidth()
  {
    return width;
  }

  public String toString()
  {
    return "Pixmap pos: " + x + ", " + y + " size: " + width + ", " + height;
  }

  protected void setTexture(Texture texture)
  {
    this.texture = texture;
  }

  /**
   * Returns the x coordinate of the right hand side of the pixmap
   * in texture coordinates
   * @return x coordinate of right hand side
   */
  public float getEndX()
  {
    return (float) (width + x) / (float) texture.getTextureWidth();
  }

  /**
   * Returns the y coordinate of the top of the pixmap
   * in texture coordinates
   * @return y coordinate of right hand side
   */
  public float getEndY()
  {
    return (float) (height + y) / (float) texture.getTextureHeight();
  }

  /**
   * Returns the x coordinate of this pixmap on the texture.
   * @return xcoordinate
   */
  public float getStartX()
  {
    return (float) x / (float) texture.getTextureWidth();
  }

  /**
   * Returns the y coordinate of this pixmap on the texture.
   * @return y coordinate
   */
  public float getStartY()
  {
    return (float) y / (float) texture.getTextureHeight();
  }
 
}
