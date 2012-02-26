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
 * Created on May 3, 2005
 * $Id: PlainBorder.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.decorator.border;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Span;

/**
 * Solid line border type. The border can have arbitrary line width (bottom,
 * top, left, right) but is drawn in only one color.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date:
 *         2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class PaddingBorder extends Border {

    private Color color = Color.BLUE;

    public PaddingBorder(int top, int left, int right, int bottom) {
        this(top, left, right, bottom, Color.BLACK, true, Span.BORDER);
    }

    public PaddingBorder(int top, int left, int right, int bottom, Color color, boolean enabled, Span span) {
        setSpacing(top, left, right, bottom);
        this.color = color;
        setEnabled(enabled);
        setSpan(span);
    }

    /**
     * Returns the line color.
     * 
     * @return the line color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the line color.
     * 
     * @param color line color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void paint(Graphics g, int localX, int localY, int width, int height) {

        IOpenGL gl = g.getOpenGL();
        g.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        int globalX = localX + g.getTranslation().getX();
        int globalY = localY + g.getTranslation().getY();

        gl.startQuads();

        // draw left line
        if (getLeft() > 0) {
            gl.vertex(globalX - getLeft(), globalY + height + getTop());
            gl.vertex(globalX, globalY + height + getTop());
            gl.vertex(globalX, globalY - getBottom());
            gl.vertex(globalX - getLeft(), globalY - getBottom());
        }

        // draw bottom line
        if (getBottom() > 0) {

            gl.vertex(globalX - getLeft(), globalY - getBottom());
            gl.vertex(globalX + width + getRight(), globalY - getBottom());
            gl.vertex(globalX + width + getRight(), globalY);
            gl.vertex(globalX - getLeft(), globalY);

        }

        // draw rigth line
        if (getRight() > 0) {
            gl.vertex(globalX + width + getRight(), globalY + height + getTop());
            gl.vertex(globalX + width, globalY + height + getTop());
            gl.vertex(globalX + width, globalY - getBottom());
            gl.vertex(globalX + width + getRight(), globalY - getBottom());
        }

        // draw top line
        if (getTop() > 0) {

            gl.vertex(globalX - getLeft(), globalY + height + getTop());
            gl.vertex(globalX + width + getRight(), globalY + height + getTop());
            gl.vertex(globalX + width + getRight(), globalY + height);
            gl.vertex(globalX - getLeft(), globalY + height);

        }
        gl.end();
    }

    @Override
    public void process(InputOutputStream stream) throws IOException, IXMLStreamableException {
        super.process(stream);

        if (getTop() == 0 && getLeft() == 0 && getBottom() == 0 && getRight() == 0) {
            setSpacing(1, 1);
        }

        color = stream.processChild("Color", color, Color.BLACK, Color.class);
    }

    /*
     * (non-Javadoc)
     * @see org.fenggui.decorator.border.Border#copy()
     */
    @Override
    public IDecorator copy() {
        PaddingBorder result = new PaddingBorder(getTop(), getLeft(), getRight(), getBottom());
        result.setColor(getColor());
        super.copy(result);
        return result;
    }

}
