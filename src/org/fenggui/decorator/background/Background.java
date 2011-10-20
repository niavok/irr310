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
 * Created on Apr 29, 2005
 * $Id: Background.java 614 2009-03-28 13:13:57Z marcmenghin $
 */
package org.fenggui.decorator.background;

import org.fenggui.decorator.Decorator;
import org.fenggui.util.Span;

/**
 * Basic class for all backgrounds. Its purpose is mainly to
 * provide the <code>paint</code> method which will be overriden
 * by specific background implementations.
 * 
 * <br/>
 * The background spans over the content, padding and border, but
 * not over the margin.<br/>
 * <br/>
 *  
 * @author Johannes Schaback
 * @version $Revision: 614 $
 */
public abstract class Background extends Decorator
{

  /**
   * Every Background class must register itself to the type register
   * if it should be possible to be loaded using InputOutputStream
   */
  //public static final TypeRegister<Background> TYPE_REGISTER =
  //new TypeRegister<Background>();
  public Background()
  {
    this.setSpan(Span.PADDING);
  }
}