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
 * Created on 29.01.2008
 * $Id$
 */
package org.fenggui.actor;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.Event;
import org.fenggui.event.IEventListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyReleasedEvent;

/**
 * This actor can be assigned to a Display and will save a screenshot if the F12
 * key is hit. You have to call the renderToDos(..) method after the
 * yourDisplay.display() method call so the actor is able to save a complete screenshot.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ScreenshotActor implements IActor
{

  private static final int TARGA_HEADER_SIZE = 18;

  private File             screenshotFile    = null;
  private IEventListener   keylistener       = new IEventListener()
                                             {

                                               public void processEvent(Event event)
                                               {
                                                 if (event instanceof KeyReleasedEvent)
                                                   if (((KeyReleasedEvent) event).getKeyClass() == Key.F12)
                                                   {
                                                     takeScreenshot(new File(System.currentTimeMillis()
                                                         + "_screenshot.tga"));
                                                   }
                                               }

                                             };

  /**
   * 
   */
  public ScreenshotActor()
  {

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#hook(org.fenggui.IWidget)
   */
  public void hook(IWidget widget)
  {
    if (widget instanceof Display)
      ((Display) widget).addGlobalEventListener(keylistener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.actor.IActor#unHook(org.fenggui.IWidget)
   */
  public void unHook(IWidget widget)
  {
    if (widget instanceof Display)
      ((Display) widget).removeGlobalEventListener(keylistener);
  }

  /**
   * Takes a screenshot and writes it in the given file.
   * 
   * @param screenshotFile
   *          the file to store the screenshot
   */
  private void takeScreenshot(File screenshotFile)
  {
    this.screenshotFile = screenshotFile;
  }

  /**
   * This method should be called after the Display's display() Method. This creates the
   * screenshot.
   * 
   * @param opengl
   * @param width
   * @param height
   */
  public void renderToDos(IOpenGL opengl, int width, int height)
  {
    if (screenshotFile != null)
    {
      screenshot(opengl, width, height, screenshotFile);
      screenshotFile = null;
    }
  }

  /**
   * Takes a screenshot of the current frame. This method is entirely copied from
   * http://www.javagaming.org/forums/index.php?topic=8747.0
   * 
   * @param gl
   *          FengGUIs opengl interface
   * @param width
   *          the width of the screenshot
   * @param height
   *          the height of the screenhost
   * @param file
   *          the file where to store the screenshot
   */
  private static void screenshot(IOpenGL gl, int width, int height, File file)
  {
    try
    {
      RandomAccessFile out = new RandomAccessFile(file, "rw");
      FileChannel ch = out.getChannel();
      int fileLength = TARGA_HEADER_SIZE + width * height * 3;
      out.setLength(fileLength);
      MappedByteBuffer image = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);

      // write the TARGA header
      image.put(0, (byte) 0).put(1, (byte) 0);
      image.put(2, (byte) 2); // uncompressed type
      image.put(12, (byte) (width & 0xFF)); // width
      image.put(13, (byte) (width >> 8)); // width
      image.put(14, (byte) (height & 0xFF));// height
      image.put(15, (byte) (height >> 8));// height
      image.put(16, (byte) 24); // pixel size

      // go to image data position
      image.position(TARGA_HEADER_SIZE);
      // jogl needs a sliced buffer
      ByteBuffer bgr = image.slice();

      // read the BGR values into the image buffer
      gl.readPixels(0, 0, width, height, bgr);

      // close the file channel
      ch.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
