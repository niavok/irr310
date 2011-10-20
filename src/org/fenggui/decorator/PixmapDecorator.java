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
 * Created on Jun 28, 2007
 * $Id$
 */
package org.fenggui.decorator;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Span;

/**
 * 
 * @author Johannes Schaback, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class PixmapDecorator extends Decorator
{
  private Pixmap  pixmap               = null;

  private Color   modulationColor      = Color.WHITE;
  private boolean useAlternateBlending = false;

  public PixmapDecorator(String label, Span span, boolean enabled, Pixmap pixmap, Color color, boolean useAlternateBlending)
  {
    super(label, span, enabled);
    this.pixmap = pixmap;
    this.modulationColor = color;
    this.useAlternateBlending = useAlternateBlending;
  }

  public PixmapDecorator(String label, Span span, boolean enabled, Pixmap pixmap, boolean useAlternateBlending)
  {
    super(label, span, enabled);
    this.pixmap = pixmap;
    this.useAlternateBlending = useAlternateBlending;
  }

  public PixmapDecorator(String label, Span span, boolean enabled, Pixmap pixmap)
  {
    this(label, span, enabled, pixmap, false);
  }

  public PixmapDecorator(String label, Pixmap pixmap, boolean useAlternateBlending)
  {
    this(label, Span.PADDING, true, pixmap, useAlternateBlending);
  }

  public PixmapDecorator(String label, Pixmap pixmap)
  {
    this(label, Span.PADDING, true, pixmap);
  }

  public void paint(Graphics g, int localX, int localY, int width, int height)
  {
    if (pixmap == null || !this.isEnabled())
      return;

    int x = localX + (int) ((double) (width - pixmap.getWidth()) / 2d);
    int y = localY + (int) ((double) (height - pixmap.getHeight()) / 2d);

    if (useAlternateBlending)
    {
      g.getOpenGL().enableAlternateBlending(true);
      g.setColor(new Color(modulationColor.getRed() * modulationColor.getAlpha(), modulationColor.getGreen()
          * modulationColor.getAlpha(), modulationColor.getBlue() * modulationColor.getAlpha(), modulationColor
          .getAlpha()));
      g.drawImage(pixmap, x, y);
      g.getOpenGL().enableAlternateBlending(false);
    }
    else
    {
      g.setColor(modulationColor);
      g.drawImage(pixmap, x, y);
    }
  }

  public String getUniqueName()
  {
    return "PixmapDecorator";
  }

  public Pixmap getPixmap()
  {
    return pixmap;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    if (stream.isInputStream()) // XXX: only support read-in at the moment :(
    {
      pixmap = stream.processChild("Pixmap", pixmap, null, Pixmap.class);
    }
    useAlternateBlending = stream.processAttribute("alternateBlending", useAlternateBlending, false);
    modulationColor = stream.processChild("color", modulationColor, Color.WHITE, Color.class);
  }

  public IDecorator copy()
  {
    PixmapDecorator result = new PixmapDecorator(this.getLabel(), this.getSpan(), this.isEnabled(), this.getPixmap());
    result.useAlternateBlending = useAlternateBlending;
    result.modulationColor = modulationColor;
    return result;
  }

  public Color getModulationColor()
  {
    return modulationColor;
  }

  public void setModulationColor(Color modulationColor)
  {
    this.modulationColor = modulationColor;
  }

  public boolean isUseAlternateBlending()
  {
    return useAlternateBlending;
  }

  public void setUseAlternateBlending(boolean useAlternateBlending)
  {
    this.useAlternateBlending = useAlternateBlending;
  }
}
