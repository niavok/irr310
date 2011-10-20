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
 * Created on Jul 17, 2005
 * $Id: Rectangle.java 613 2009-03-25 22:02:20Z marcmenghin $
 */
package org.fenggui.util;

/**
 * Implementation of a rectangle. The coordinates x, y denote the point were it
 * is located (usually the lower left corner in the FengGui coordinate system)
 * and width and height specify the size.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-25 23:02:20 +0100 (Mi, 25 Mär 2009) $
 * @version $Revision: 613 $
 * 
 * 
 */
public class Rectangle implements Cloneable
{

  private int width, height, x, y;

  public Rectangle()
  {
  }

  public Rectangle(Rectangle copy)
  {
    width = copy.width;
    height = copy.height;
    x = copy.x;
    y = copy.y;
  }

  public Rectangle(int x, int y, int width, int height)
  {
    super();
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }

  /**
   * Check the intersection with an other <code>Rectangle</code>
   * @param rect
   * @return <code>true</code> if <code>rect</code> instersects this rectangle.
   */
  public boolean intersect(Rectangle rect)
  {
    if (getIntersection(rect) == null)
      return false;
    else
      return true;
  }

  public Rectangle getIntersection(Rectangle rect)
  {
    int x = Math.max(rect.getX(), this.getX());
    int y = Math.max(rect.getY(), this.getY());

    int w = Math.min(rect.getX() + rect.getWidth(), this.getX() + this.getWidth()) - Math.max(rect.getX(), this.getX());

    int h = Math.min(rect.getY() + rect.getHeight(), this.getY() + this.getHeight())
        - Math.max(rect.getY(), this.getY());

    if (w <= 0 || h <= 0)
      return null;
    return new Rectangle(x, y, w, h);

  }

  public void set(Rectangle copy)
  {
    width = copy.width;
    height = copy.height;
    x = copy.x;
    y = copy.y;
  }

  /**
   * Sets all values at once.
   * @param x the x value of the rectangle
   * @param y the y value of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public void set(int x, int y, int width, int height)
  {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }

  public int getHeight()
  {
    return height;
  }

  public void setHeight(int height)
  {
    this.height = height;
  }

  public void setWidth(int width)
  {
    this.width = width;
  }

  public void setX(int x)
  {
    this.x = x;
  }

  public void setY(int y)
  {
    this.y = y;
  }

  public int getWidth()
  {
    return width;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public boolean contains(int x, int y)
  {
    return (x >= this.x && x <= this.x + width) && (y >= this.y && y <= this.y + height);
  }

  public String toString()
  {
    return "[" + x + "," + y + " " + width + "x" + height + "]";
  }

  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Rectangle rectangle = (Rectangle) o;

    if (height != rectangle.height)
      return false;
    if (width != rectangle.width)
      return false;
    if (x != rectangle.x)
      return false;
    if (y != rectangle.y)
      return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = width;
    result = 31 * result + height;
    result = 31 * result + x;
    result = 31 * result + y;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  public Rectangle clone() throws CloneNotSupportedException
  {
    return (Rectangle) super.clone();
  }
  
  
}
