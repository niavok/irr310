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
 * Created on 2005-4-13
 * $Id: Point.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui.util;

/**
 * Implementation of a point. This class is read-only.
 *
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-28 14:13:57 +0100 (Sa, 28 Mär 2009) $
 * @version $Revision: 614 $
 * @see org.lwjgl.util.WritablePoint
 */
public class Point implements Cloneable
{

  int x, y;

  /**
   * Creates a new point.
   * 
   * @param p coordinates of this point
   */
  public Point(Point p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  /**
   * Creates a new point.
   *
   * @param x the x coordinate of the point
   * @param y the y coordinate of the point
   */
  public Point(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x coordinate of the point
   *
   * @return x value
   */
  public int getX()
  {
    return x;
  }

  /**
   * Returns the y coordinate of this point.
   *
   * @return y value
   */
  public int getY()
  {
    return y;
  }

  /**
   * Returns a formated string container the x and y coordinate of this point
   *
   * @return string
   */
  public String toString()
  {
    return "(" + x + ", " + y + ")";
  }

  /**
   * Sets the x coordinate of this point.
   *
   * @param x x coodinate
   */
  public void setX(int x)
  {
    this.x = x;
  }

  /**
   * Sets the y coordinate of this point.
   *
   * @param y y coordinate
   */
  public void setY(int y)
  {
    this.y = y;
  }

  /**
   * Sets the x and y coordinate of this point.
   *
   * @param x x coordinate
   * @param y y coordiate
   */
  public void setXY(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Translates this point by the given deltas.
   *
   * @param x value to add on current x coordinate
   * @param y value to add on current y coordinate
   */
  public void translate(int x, int y)
  {
    this.x += x;
    this.y += y;
  }

  /**
   * Translates the point by a given delta point;
   * 
   * @param point
   */
  public void translate(Point point)
  {
    this.x += point.x;
    this.y += point.y;
  }

  public Point diff(Point point)
  {
    return new Point(this.x - point.x, this.y - point.y);
  }

  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Point point = (Point) o;

    if (x != point.x)
      return false;
    if (y != point.y)
      return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = x;
    result = 31 * result + y;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  public Point clone()
  {
    try
    {
      return (Point) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      //can not happen but write out anyway
      Log.error("Couldn't clone Point", e);
      return null;
    }
  }
}
