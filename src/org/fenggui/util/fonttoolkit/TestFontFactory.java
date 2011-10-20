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
 * Created on Jan 30, 2006
 * $Id: TestFontFactory.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.util.fonttoolkit;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;

import org.fenggui.util.Alphabet;

/**
 * 
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class TestFontFactory
{

  public static void main(String[] args)
  {

    Font font = new Font("Helvetica", Font.BOLD, 24);

    FontFactory ff = new FontFactory(Alphabet.getDefaultAlphabet(), font);
    AssemblyLine line = ff.getAssemblyLine();

    Paint redYellowPaint = new GradientPaint(0, 0, Color.RED, 15, 15, Color.YELLOW, true);
    Paint greenWhitePaint = new GradientPaint(0, 0, Color.BLACK, 15, 0, Color.GREEN, true);

    line.addStage(new Clear());
    line.addStage(new DrawCharacter(Color.WHITE, false));

    //ff.addRenderer();
    //ff.addRenderer(new DrawCharacterOutline(Color.GREEN));

    line.addStage(new BinaryDilation(Color.WHITE, 3));
    //line.addStage(new BinaryErosion(Color.WHITE,3));
    //ff.addRenderer(new Convolution(Kernel.createGaussianKernel(3, 1.5), true));

    //line.addStage(new DrawCharacter(Color.WHITE, false));

    line.addStage(new PixelReplacer(redYellowPaint, Color.WHITE));
    line.addStage(new DrawCharacter(Color.CYAN, false));

    line.addStage(new PixelReplacer(greenWhitePaint, Color.CYAN));
    //ff.addRenderer(new Convolution(Kernel.createGaussianKernel(3, 1.4), true));

    //ff.addRenderer(new BinaryDilation(Color.RED, 3));
    //ff.addRenderer(new AWTFontRenderer());
    //line.addStage(new DrawCharacter(Color.BLACK, true));

    ff.createFont();

  }

}
