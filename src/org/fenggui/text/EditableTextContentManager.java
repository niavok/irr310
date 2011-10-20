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
 * Created on 21.11.2007
 * $Id$
 */
package org.fenggui.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.text.content.ContentManager;
import org.fenggui.text.content.factory.simple.ContentFactory;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class EditableTextContentManager implements IEditableTextContentManager, IXMLStreamable
{
  private int                        width                = 10;
  private int                        height               = 10;
  private ContentManager             manager;
  private Pixmap                     cursor;
  private Color                      cursorColor          = Color.BLACK;
  private boolean                    editMode             = false;
  private int                        selectionStart       = -1;
  private IClipboard                 clipboard            = null;
  private boolean                    readonly             = false;

  private List<ISizeChangedListener> sizeChangedListeners = new ArrayList<ISizeChangedListener>();

  private ISizeChangedListener       contentListener      = new ISizeChangedListener()
                                                          {

                                                            public void sizeChanged(SizeChangedEvent event)
                                                            {
                                                              fireMinSizeChangedListerer(event);
                                                            }

                                                          };

  public EditableTextContentManager(TextAppearance appearance)
  {
    manager = new ContentManager(ContentFactory.getDefaultFactory(), appearance);
    manager.addSizeChangedListener(contentListener);
    clipboard = Binding.getInstance().getClipboard();
    this.cursor = null;
  }

  public EditableTextContentManager(EditableTextContentManager data)
  {
    this.width = data.width;
    this.height = data.height;
    this.manager = new ContentManager(data.manager);
    this.cursor = data.cursor;
    this.cursorColor = data.cursorColor;
    this.clipboard = data.clipboard;
    manager.addSizeChangedListener(contentListener);
  }

  public ContentManager getManager()
  {
    return manager;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
   */
  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
   */
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    //		this.setMultiline(stream.processAttribute("multiline", this.isMultiline(), false));
    //		this.setWordWarping(stream.processAttribute("wordwarped", this.isWordWarping(), false));

    cursor = stream.processChild("Cursor", cursor, null, Pixmap.class);
    cursorColor = stream.processChild("CursorColor", cursorColor, Color.BLACK, Color.class);
  }

  public boolean isWordWarping()
  {
    return manager.isAutoWarp();
  }

  public boolean isMultiline()
  {
    return manager.isMultiline();
  }

  public void setWordWarping(boolean warping, TextAppearance appearance)
  {
    if (warping != manager.isAutoWarp())
    {
      manager.setAutoWarp(warping, width, appearance);
    }
  }

  public void setMultiline(boolean multiline, TextAppearance appearance)
  {
    if (multiline != manager.isMultiline())
    {
      manager.setMultiline(multiline);
      manager.updateContent(width, appearance);
    }
  }

  private void setSelection(int pos1, int pos2, TextAppearance appearance)
  {
    if (pos1 == pos2)
      return;

    if (pos1 > pos2)
    {
      manager.setSelection(pos2, pos1, appearance);
    }
    else
    {
      manager.setSelection(pos1, pos2, appearance);
    }
  }

  public void clickedOn(int x, int y, Set<Key> modifiers, TextAppearance appearance)
  {
    manager.clearSelection(appearance);
    long pos = manager.findAtomOnPosition(x, this.getSize().getHeight() - y, appearance);
    if (manager.hasActiveLine() && modifiers.contains(Key.SHIFT))
    {
      selectionStart = (int) manager.getActiveAtom();
    }
    else
    {
      manager.setActiveAtom(pos);
      selectionStart = (int) pos;
    }
    manager.updateContent(width, appearance);
  }

  public void dragedTo(int x, int y, Set<Key> modifiers, TextAppearance appearance)
  {
    manager.clearSelection(appearance);
    long pos = manager.findAtomOnPosition(x, this.getSize().getHeight() - y, appearance);
    // long oldPos = manager.getStartSelection();
    //		
    // if (pos > manager.getStartSelection())
    // {
    // manager.setSelectionLength((int)(pos - oldPos));
    // } else {
    manager.setActiveAtom(pos);
    setSelection(selectionStart, (int) pos, appearance);
    // manager.setSelectionLength((int)oldPos);
    // }
  }

  protected void fireMinSizeChangedListerer(SizeChangedEvent event)
  {
    for (ISizeChangedListener sizeChanged : sizeChangedListeners)
    {
      sizeChanged.sizeChanged(event);
    }
  }

  public boolean addMinSizeChangedListener(ISizeChangedListener listener)
  {
    return sizeChangedListeners.add(listener);
  }

  public boolean removeMinSizeChangedListener(ISizeChangedListener listener)
  {
    return sizeChangedListeners.remove(listener);

  }

  /**
   * @return the editMode
   */
  public boolean isEditMode()
  {
    return editMode;
  }

  /**
   * @param editMode
   *          the editMode to set
   */
  public void setEditMode(boolean editMode)
  {
    if (this.editMode != editMode)
    {
      this.editMode = editMode;
      if (editMode && manager.getActiveAtom() < 0)
      {
        manager.setActiveAtom(0);
      }
    }
  }

  /**
   * @return the cursor
   */
  public Pixmap getCursor()
  {
    return cursor;
  }

  /**
   * @param cursor
   *          the cursor to set
   */
  public void setCursor(Pixmap cursor)
  {
    this.cursor = cursor;
  }

  /**
   * @return the cursorColor
   */
  public Color getCursorColor()
  {
    return cursorColor;
  }

  /**
   * @param cursorColor
   *          the cursorColor to set
   */
  public void setCursorColor(Color cursorColor)
  {
    this.cursorColor = cursorColor;
  }

  public boolean handleKeyPresses(Key key, Set<Key> modifiers, TextAppearance appearance)
  {
    boolean result = false;

    switch (key)
    {
    case BACKSPACE:
      if (!isReadonly())
      {
        if (manager.hasSelection())
        {
          int atom = manager.getSelectionStart();
          manager.removeSelection(appearance);
          manager.setActiveAtom(atom);
        }
        else
        {
          manager.removePreviousChar(appearance);
          selectionStart = (int) manager.getActiveAtom();
        }
        result = true;
      }
      break;
    case SHIFT:
      break;
    case DELETE:
      if (!isReadonly())
      {
        if (manager.hasSelection())
        {
          int atom = manager.getSelectionStart();
          manager.removeSelection(appearance);
          manager.setActiveAtom(atom);
        }
        else
        {
          manager.removeNextChar(appearance);
        }
        result = true;
      }
      break;
    case UP:
      if (!isReadonly() && modifiers.contains(Key.SHIFT))
      {
        manager.clearSelection(appearance);
        manager.moveUp(appearance);
        long atom = manager.getActiveAtom();
        if (atom == -1)
          break;
        setSelection(selectionStart, (int) atom, appearance);
        manager.setActiveAtom(atom);
      }
      else
      {
        if (manager.hasSelection())
        {
          manager.clearSelection(appearance);
        }
        else
        {
          manager.moveUp(appearance);
        }
        selectionStart = (int) manager.getActiveAtom();
      }
      result = true;
      break;
    case RIGHT:
      if (!isReadonly() && modifiers.contains(Key.SHIFT))
      {
        manager.clearSelection(appearance);
        manager.moveRight();
        long atom = manager.getActiveAtom();
        if (atom == -1)
          break;
        setSelection(selectionStart, (int) atom, appearance);
        manager.setActiveAtom(atom);
      }
      else
      {
        if (manager.hasSelection())
        {
          manager.clearSelection(appearance);
        }
        else
        {
          manager.moveRight();
        }
        selectionStart = (int) manager.getActiveAtom();
      }
      result = true;
      break;
    case LEFT:
      if (!isReadonly() && modifiers.contains(Key.SHIFT))
      {
        manager.clearSelection(appearance);
        manager.moveLeft();
        long atom = manager.getActiveAtom();
        if (atom == -1)
          break;
        setSelection(selectionStart, (int) atom, appearance);
        manager.setActiveAtom(atom);
      }
      else
      {
        if (manager.hasSelection())
        {
          manager.clearSelection(appearance);
        }
        else
        {
          manager.moveLeft();
        }
        selectionStart = (int) manager.getActiveAtom();
      }
      result = true;
      break;
    case DOWN:
      if (!isReadonly() && modifiers.contains(Key.SHIFT))
      {
        manager.clearSelection(appearance);
        manager.moveDown(appearance);
        long atom = manager.getActiveAtom();
        setSelection(selectionStart, (int) atom, appearance);
        manager.setActiveAtom(atom);
      }
      else
      {
        if (manager.hasSelection())
        {
          manager.clearSelection(appearance);
        }
        else
        {
          manager.moveDown(appearance);
        }
        selectionStart = (int) manager.getActiveAtom();
      }
      result = true;
      break;
    case END:
      if (!isReadonly() && modifiers.contains(Key.SHIFT))
      {
        manager.clearSelection(appearance);
        manager.setActiveAtom(manager.getAtomsTillActiveLineEnd());
        long atom = manager.getActiveAtom();
        setSelection(selectionStart, (int) atom, appearance);
        manager.setActiveAtom(atom);
      }
      else
      {
        manager.clearSelection(appearance);
        manager.setActiveAtom(manager.getAtomsTillActiveLineEnd());
        selectionStart = (int) manager.getActiveAtom();
      }
      result = true;
      break;
    case HOME:
      if (!isReadonly() && modifiers.contains(Key.SHIFT))
      {
        manager.clearSelection(appearance);
        manager.setActiveAtom(manager.getAtomsTillActiveLine());
        long atom = manager.getActiveAtom();
        setSelection(selectionStart, (int) atom, appearance);
        manager.setActiveAtom(atom);
      }
      else
      {
        manager.clearSelection(appearance);
        manager.setActiveAtom(manager.getAtomsTillActiveLine());
        selectionStart = (int) manager.getActiveAtom();
      }
      result = true;
      break;
    case COPY:
      String selectedContent = manager.getSelectedContent();
      clipboard.setText(selectedContent);
      result = true;
      break;
    case CUT:
      selectedContent = manager.getSelectedContent();
      if (!isReadonly())
        manager.removeSelection(appearance);
      clipboard.setText(selectedContent);
      result = true;
      break;
    case PASTE:
      if (!isReadonly())
      {
        selectedContent = clipboard.getText();
        manager.addContent(selectedContent, appearance);
        result = true;
      }
      break;
    case CTRL:
      break;
    }

    return result;
  }

  public boolean handleTextInput(char character, TextAppearance appearance)
  {
    boolean result = false;

    if (manager.hasSelection())
    {
      manager.removeSelection(appearance);
    }

    result = manager.addChar(character, appearance);
    selectionStart = (int) manager.getActiveAtom();
    return result;
  }

  /**
   * @param listener
   * @return
   * @see org.fenggui.text.content.ContentManager#addSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public boolean addSizeChangedListener(ISizeChangedListener listener)
  {
    return manager.addSizeChangedListener(listener);
  }

  /**
   * @param listener
   * @return
   * @see org.fenggui.text.content.ContentManager#removeSizeChangedListener(org.fenggui.event.ISizeChangedListener)
   */
  public boolean removeSizeChangedListener(ISizeChangedListener listener)
  {
    return manager.removeSizeChangedListener(listener);
  }

  public Dimension getSize()
  {
    return manager.getSize();
  }

  public void addContentAtBeginning(Object content, TextAppearance appearance)
  {
    manager.addContentAtBeginning(content, appearance);
  }

  public void addContentAtEnd(Object content, TextAppearance appearance)
  {
    manager.addContentAtEnd(content, appearance);
  }

  public void removeContentLineFromBeginning()
  {
  }

  public void removeContentLineFromEnd()
  {
  }

  public int getContentLineCount()
  {
    return manager.getContentLineCount();
  }

  public int getMaxLines()
  {
    return manager.getMaxLines();
  }

  public void setMaxLines(int lines)
  {
    manager.setMaxLines(lines);
  }

  public boolean isReadonly()
  {
    return readonly;
  }

  public void setReadonly(boolean readonly)
  {
    this.readonly = readonly;
  }

  public int getActivePositionIndex()
  {
    return (int) manager.getActiveAtom();
  }

  public boolean hasSelection()
  {
    return manager.hasSelection();
  }

  public int getSelectionEndIndex()
  {
    return manager.getSelectionEnd();
  }

  public int getSelectionStartIndex()
  {
    return manager.getSelectionStart();
  }

  public void setActivePositionIndex(int index)
  {
    manager.setActiveAtom(index);
  }

  public void setSelectionIndex(int index1, int index2, TextAppearance appearance)
  {
    manager.setSelection(index1, index2, appearance);
  }

  public void setManager(ContentManager manager)
  {
    this.manager = manager;
  }

  public String getContent()
  {
    return manager.getContent();
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.advanced.IAdvancedTextRendererData#getSelectedContentString()
   */
  public String getSelectedContent()
  {
    return manager.getSelectedContent();
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.advanced.IAdvancedTextRendererData#isValidChar(org.fenggui.text.IComplexTextRendererData, char)
   */
  public boolean isValidChar(char c, TextAppearance appearance)
  {
    return manager.isValidCharacter(c, appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#adaptChange(int, int, org.fenggui.appearance.TextAppearance)
   */
  public void adaptChange(int width, int height, TextAppearance appearance)
  {
    //		boolean updated = false;

    if (width != this.width)
    {
      this.width = width;
      //			updated = true;

      //for now only width is relevant
      manager.updateContent(width, appearance);
    }

    if (height != this.height)
    {
      this.height = height;
      //			updated = true;
    }

    //		if (updated)
    //		{
    //			manager.updateContent(width);
    //		}
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#isEmpty()
   */
  public boolean isEmpty()
  {
    return manager.isEmpty();
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#render(int, int, org.fenggui.binding.render.Graphics, org.fenggui.appearance.TextAppearance)
   */
  public void render(int x, int y, Graphics g, TextAppearance appearance)
  {
    manager.render(x, y, g, isEditMode(), appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#setContent(java.lang.String, org.fenggui.appearance.TextAppearance)
   */
  public void setContent(String text, TextAppearance appearance)
  {
    manager.removeAll(appearance);
    manager.addContent(text, appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.IComplexTextRendererData#setContent(java.lang.Object, org.fenggui.appearance.TextAppearance)
   */
  public void setContent(Object text, TextAppearance appearance)
  {
    manager.removeAll(appearance);
    manager.addContent(text, appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.ITextContentManager#Update(org.fenggui.appearance.TextAppearance)
   */
  public void Update(TextAppearance appearance)
  {
    manager.updateContent(this.width, appearance);
  }
}
