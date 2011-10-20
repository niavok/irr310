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
 * Created on 26.09.2007
 * $Id$
 */package org.fenggui.binding.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class AWTClipboard implements IClipboard
{

  private Clipboard clipboard = null;

  public AWTClipboard()
  {
    clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.clipboard.IClipboard#getText()
   */
  public String getText()
  {
    Transferable contents = clipboard.getContents(null);
    String result = "";

    if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
    {
      try
      {
        result = (String) contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch (UnsupportedFlavorException e)
      {
        //do nothing
      }
      catch (IOException e)
      {
        //do nothing
      }
    }
    //System.out.println("Clipboard-get: " + result);
    return result;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.clipboard.IClipboard#setText(java.lang.String)
   */
  public void setText(String text)
  {
    //System.out.println("Clipboard-set: " + text);
    clipboard.setContents(new StringSelection(text), new ClipboardOwner()
    {

      public void lostOwnership(Clipboard clipboard, Transferable contents)
      {
        //do nothing
      }

    });
  }

}
