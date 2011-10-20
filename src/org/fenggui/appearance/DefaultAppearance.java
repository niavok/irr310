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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details:
 * http://www.gnu.org/copyleft/lesser.html#TOC3
 * 
 * Created on 22.10.2007
 * $Id$
 */
package org.fenggui.appearance;

import java.io.IOException;

import org.fenggui.StandardWidget;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * Provides a general storage for various common appearance elements used by widgets.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class DefaultAppearance extends GenericAppearance implements Cloneable
{

  public DefaultAppearance(StandardWidget w)
  {
    super(w);
  }

  public DefaultAppearance(StandardWidget w, DefaultAppearance appearance)
  {
    super(w, appearance);
  }

  public DefaultAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    super(w, stream);
  }
  
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
  }
  
  /* (non-Javadoc)
   * @see org.fenggui.appearance.GenericAppearance#clone(org.fenggui.StandardWidget)
   */
  @Override
  public DefaultAppearance clone(StandardWidget widget)
  {
    return (DefaultAppearance) super.clone(widget);
  }
}
