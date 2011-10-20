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
 * Created on 22.11.2007
 * $Id$
 */
package org.fenggui.text.content;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.event.ElementEvent;
import org.fenggui.event.IElementListener;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.text.content.part.AbstractContentPart;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * Manages the content of a complex text string.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentManager implements IContentSelection
{
  public static final int                             UNLIMITEDLINES      = -1;

  private List<ContentUserLine>                       content;
  private List<ISizeChangedListener>                  sizeListeners       = new ArrayList<ISizeChangedListener>();
  private List<IElementListener<AbstractContentPart>> elementListener     = new ArrayList<IElementListener<AbstractContentPart>>();

  //Cache stuff
  private int                                         requestedWidthCache = Integer.MAX_VALUE;
  private int                                         atomCountCache      = 0;
  private Dimension                                   size                = new Dimension(0, 0);

  //informational objects
  private IContentFactory                             factory;
  private boolean                                     autoWarp            = false;
  private boolean                                     multiline           = false;
  private int                                         maxLines            = UNLIMITEDLINES;

  //active part caching
  private long                                        activeAtom          = -1;
  private ContentUserLine                             activeLine          = null;
  private int                                         activeLineY         = 0;
  private int                                         activeLineAtoms     = 0;

  public ContentManager(IContentFactory factory, TextAppearance appearance)
  {
    this.factory = factory;
    this.content = new CopyOnWriteArrayList<ContentUserLine>();
    initEmptyContent(factory, appearance);
  }

  public ContentManager(ContentManager manager)
  {
    this.factory = manager.factory;
    synchronized (manager.content)
    {
      this.content = new ArrayList<ContentUserLine>(manager.content);
    }
    this.size = manager.size;
    this.setActiveAtom(0);
  }

  private void initEmptyContent(IContentFactory factory, TextAppearance appearance)
  {
    synchronized (content)
    {
      this.content.add(new ContentUserLine(factory.getEmptyContentPart(appearance)));
      this.update();
      this.setActiveAtom(0);
    }
  }

  public boolean isEmpty()
  {
    if (content.size() > 1)
    {
      return false;
    }
    else
    {
      return content.get(0).isEmpty();
    }
  }

  public int getAtomsTillActiveLine()
  {
    if (hasActiveLine())
    {
      return this.activeLineAtoms + this.activeLine.getAtomsTillActiveLine();
    }
    else
    {
      return 0;
    }
  }

  public int getAtomsTillActiveLineEnd()
  {
    if (hasActiveLine())
    {
      return this.activeLineAtoms + this.activeLine.getAtomsTillActiveLineEnd();
    }
    else
    {
      return 0;
    }
  }

  public void update()
  {
    int y = 0;
    int x = 0;
    int atomCountCache = 0;

    synchronized (content)
    {
      for (ContentUserLine userline : content)
      {
        x = Math.max(x, userline.getSize().getWidth());
        y += userline.getSize().getHeight();
        atomCountCache += userline.getAtomCount() + 1;
      }
    }
    Dimension oldSize = this.size;
    this.size = new Dimension(x, y);

    if (atomCountCache == 0)
      this.atomCountCache = 0;
    else
      this.atomCountCache = atomCountCache - 1;

    if (!this.size.equals(oldSize))
      fireSizeChangedEvent(new SizeChangedEvent(null, oldSize, this.size));
  }

  public boolean addSizeChangedListener(ISizeChangedListener listener)
  {
    return sizeListeners.add(listener);
  }

  public boolean removeSizeChangedListener(ISizeChangedListener listener)
  {
    return sizeListeners.remove(listener);
  }

  protected void fireSizeChangedEvent(SizeChangedEvent event)
  {
    for (ISizeChangedListener listener : sizeListeners)
    {
      listener.sizeChanged(event);
    }
  }

  public String getContent()
  {
    StringBuilder builder = new StringBuilder(this.getAtomCount());

    for (int i = 0; i < content.size(); i++)
    {
      ContentUserLine line = content.get(i);
      factory.addLineStart(builder);
      line.getContent(builder);
      factory.addLineEnd(builder);
    }

    factory.finalContent(builder);

    return builder.toString();
  }

  public String getSelectedContent()
  {
    StringBuilder builder = new StringBuilder(this.getAtomCount());

    for (ContentUserLine line : content)
    {
      factory.addLineStart(builder);
      line.getSelectedContent(builder);
      factory.addLineEnd(builder);
    }

    return builder.toString();
  }

  /**
   * Updates the contents wordwarping to the new width.
   * 
   * @param width
   */
  public void updateContent(int width, TextAppearance appearance)
  {
    if (width <= 0)
    {
      width = 1;
    }

    requestedWidthCache = width;

    synchronized (content)
    {
      for (ContentUserLine line : content)
      {
        line.UpdateContent(width, autoWarp, factory, appearance);
      }
    }
    this.update();
  }

  /**
   * removes everything from the content. Only one single empty line will remain.
   */
  public void removeAll(TextAppearance appearance)
  {
    synchronized (content)
    {
      for (ContentUserLine line : content)
      {
        line.removeAll(factory, appearance);
      }
      content.clear();

      initEmptyContent(factory, appearance);
    }
  }

  /**
   * Optimizes the content objects. This will remove all auto-warped lines
   * so that an index transformation is correct. This should be called before
   * a call to getContentIndexOfAtom() to ensure that the index is correct.
   */
  public void optimizeContent(TextAppearance appearance)
  {
    for (ContentUserLine line : content)
    {
      line.optimizeContent(appearance);
    }
  }

  public void setActiveAtom(long atomIndex)
  {
    synchronized (content)
    {
      if (this.activeLine != null)
      {
        this.activeLine.setActiveAtom(-1);
        this.activeLine = null;
        this.activeLineAtoms = 0;
        this.activeLineY = 0;
        this.activeAtom = -1;
      }

      long atomCount = 0;
      activeLineY = 0;

      if (atomIndex < 0)
      {
        this.activeLine = null;
        this.activeLineAtoms = 0;
        this.activeLineY = 0;
        this.activeAtom = -1;
      }
      for (ContentUserLine line : content)
      {
        atomCount += line.getAtomCount();
        if (atomCount >= atomIndex)
        {
          this.activeLine = line;
          this.activeAtom = atomIndex;
          this.activeLineAtoms = (int) (atomCount - line.getAtomCount());
          this.activeLine.setActiveAtom((int) (atomIndex - (atomCount - line.getAtomCount())));
          return;
        }
        activeLineY += line.getSize().getHeight();
        atomCount++;
      }
    }
    this.activeLine = null;
    this.activeAtom = -1;
  }

  public long findAtomOnPosition(int x, int y, TextAppearance appearance)
  {
    int yPos = 0;
    long selectionIndex = 0;
    ContentUserLine selectedLine = null;
    synchronized (content)
    {
      if (y < 0)
      {
        // select last line as y pos is too low
        selectedLine = content.get(0);
        selectionIndex = 0;
      }
      else
      {
        for (ContentUserLine line : content)
        {
          if ((yPos + line.getSize().getHeight()) > y)
          {
            selectedLine = line;
            break;
          }
          else
          {
            selectionIndex += line.getAtomCount() + 1;
          }
          yPos += line.getSize().getHeight();
        }

        if (selectedLine == null)
        {
          selectedLine = content.get(content.size() - 1);
          selectionIndex = this.getAtomCount() - selectedLine.getAtomCount();
          yPos -= selectedLine.getSize().getHeight();
          if (yPos < 0)
            yPos = 0;
        }
      }

      // line found, search in content line
      if (selectedLine != null)
      {
        long atoms = selectedLine.findAtomOnPosition(x, y - yPos, appearance);
        if (atoms < 0)
          selectionIndex = -1;
        else
          selectionIndex += atoms;
      }
      else
      {
        // no line found
        selectionIndex = -1;
      }
    }
    return selectionIndex;
  }

  public boolean hasActiveLine()
  {
    synchronized (content)
    {
      return this.activeLine != null && this.activeLine.hasActiveLine();
    }
  }

  /**
   * Adds a character at the selection start index. If a selection is present it will be
   * removed.
   * 
   * @param c
   * @return true if the character could be added.
   */
  public boolean addChar(char c, TextAppearance appearance)
  {
    synchronized (content)
    {
      if (hasActiveLine())
      {
        if ((c != '\n' && c != '\t') || multiline)
        {
          clearSelection(appearance);

          if (c == '\n')
          {
            //split contentline as user wants a newline
            ContentUserLine newline = this.activeLine.splitActive(factory, appearance);
            this.content.add(this.content.indexOf(activeLine) + 1, newline); //insert after active line
            newline.UpdateContent(this.requestedWidthCache, autoWarp, factory, appearance);

            //update active line
            this.activeAtom++;
            this.activeLineAtoms += this.activeLine.getAtomCount();
            this.activeLineY += this.activeLine.getSize().getHeight();
            this.activeLine = newline;
            update();
            return true;
          }
          else
          {
            if (this.activeLine.addChar(c, appearance))
            {
              this.activeAtom++;
              this.activeLine.UpdateContent(this.requestedWidthCache, autoWarp, factory, appearance);
              update();
              return true;
            }
          }
        }

      }
    }

    return false;
  }

  public void removeLastContentLine()
  {
    synchronized (content)
    {
      if (this.content.size() >= 1)
      {
        this.content.remove(this.content.size() - 1);
        update();
      }
    }
  }

  public void removeFirstContentLine()
  {
    synchronized (content)
    {
      if (this.content.size() >= 1)
      {
        this.content.remove(0);
        update();
      }
    }
  }

  public int getContentLineCount()
  {
    return content.size();
  }

  /**
   * Adds the content to the active position.
   * 
   * @param content
   */
  public void addContent(Object content, TextAppearance appearance)
  {
    synchronized (this.content)
    {
      if (hasActiveLine())
      {
        if (hasSelection())
          removeSelection(factory, appearance);

        Object[] lines = factory.getContentLines(content);

        for (int i = 0; i < lines.length; i++)
        {
          Object line = lines[i];
          this.activeLine.addContent(line, this.requestedWidthCache, autoWarp, factory, appearance);
          this.activeLine.UpdateContent(this.requestedWidthCache, autoWarp, factory, appearance);

          if (i < lines.length - 1)
          {
            //on last line add no new line
            this.activeLine.setActiveAtom((int) this.activeLine.getAtomCount());

            ContentUserLine newline = this.activeLine.splitActive(factory, appearance);

            this.content.add(this.content.indexOf(activeLine) + 1, newline); //insert after active line

            //update active line
            this.activeAtom++;
            this.activeLineAtoms += this.activeLine.getAtomCount();
            this.activeLineY += this.activeLine.getSize().getHeight();
            this.activeLine = newline;
          }
        }

        update();
        setActiveAtom(this.activeAtom);
      }
    }
  }

  public Character removeNextChar(TextAppearance appearance)
  {
    synchronized (content)
    {
      if (!hasActiveLine())
        return null;

      clearSelection(appearance);
      Character result = this.activeLine.removeNextChar(appearance);
      if (result != null)
      {
        updateContent(this.requestedWidthCache, appearance);
        setActiveAtom(this.activeAtom);
      }
      else
      {
        // char is null -> nothing to remove in this line at the selected position,
        // remove linebreak
        int index = content.indexOf(this.activeLine) + 1;
        if (index >= content.size())
        {
          result = null;
        }
        else
        {
          ContentUserLine next = content.get(index);
          content.remove(index);

          for (ContentLine line : next.getContentLines())
          {
            this.activeLine.add(line);
          }
          result = '\n';

          updateContent(this.requestedWidthCache, appearance);
          setActiveAtom(this.activeAtom);
        }
      }
      return result;
    }
  }

  public Character removePreviousChar(TextAppearance appearance)
  {
    synchronized (content)
    {
      if (activeLine == null)
        return null;

      clearSelection(appearance);
      Character result = this.activeLine.removePreviousChar(appearance);
      if (result != null)
      {
        this.activeAtom--;
        updateContent(this.requestedWidthCache, appearance);
        setActiveAtom(this.activeAtom);
      }
      else
      {
        // char is null -> nothing to remove in this line at the selected position,
        // remove linebreak
        int index = content.indexOf(this.activeLine) - 1;
        if (index < 0)
        {
          result = null;
        }
        else
        {
          ContentUserLine prev = content.get(index);
          content.remove(index + 1);
          for (ContentLine line : this.activeLine.getContentLines())
          {
            prev.add(line);
          }
          result = '\n';

          this.activeAtom--;
          updateContent(this.requestedWidthCache, appearance);
          setActiveAtom(this.activeAtom);
        }
      }

      return result;
    }
  }

  public void moveLeft()
  {
    if (activeAtom == -1)
      return;

    this.activeAtom--;
    if (activeAtom < 0)
      activeAtom = 0;
    setActiveAtom(this.activeAtom);
  }

  public void moveRight()
  {
    if (activeAtom == -1)
      return;

    this.activeAtom++;
    if (activeAtom >= getAtomCount())
    {
      activeAtom = getAtomCount();
    }
    setActiveAtom(this.activeAtom);
  }

  public void moveUp(TextAppearance appearance)
  {
    synchronized (content)
    {
      if (activeLine == null)
        return;

      Point currentPos = activeLine.getActivePosition(appearance);

      if (activeLine.getSelectedLine() != null)
      {
        currentPos.setY(activeLineY + currentPos.getY() - activeLine.getSelectedLine().getSize().getHeight());
        if (currentPos.getY() < 0)
        {
          currentPos.setY(0);
        }
        activeAtom = findAtomOnPosition(currentPos.getX(), currentPos.getY(), appearance);
        setActiveAtom(this.activeAtom);
      }
    }
  }

  public void moveDown(TextAppearance appearance)
  {
    synchronized (content)
    {
      if (activeLine == null)
        return;

      Point currentPos = activeLine.getActivePosition(appearance);

      if (activeLine.getSelectedLine() != null)
      {
        currentPos.setY(activeLineY + currentPos.getY() + (activeLine.getSelectedLine().getSize().getHeight()));
        if (currentPos.getY() > getSize().getHeight())
        {
          currentPos.setY(getSize().getHeight());
        }
        activeAtom = findAtomOnPosition(currentPos.getX(), currentPos.getY(), appearance);
        setActiveAtom(this.activeAtom);
      }
    }
  }

  public Point getActivePosition(TextAppearance appearance)
  {
    synchronized (content)
    {
      if (activeLine != null)
      {
        Point result = activeLine.getActivePosition(appearance);
        if (result != null)
          result.setY(result.getY() + activeLineY);
        return result;
      }
      else
      {
        return null;
      }
    }
  }

  public Dimension getSize()
  {
    return size;
  }

  /**
   * @return Returns the autoWarp.
   */
  public boolean isAutoWarp()
  {
    return autoWarp;
  }

  /**
   * @param autoWarp
   *          The autoWarp to set.
   */
  public void setAutoWarp(boolean autoWarp, int width, TextAppearance appearance)
  {
    this.autoWarp = autoWarp;
    updateContent(width, appearance);
  }

  /**
   * @return Returns the startSelection.
   */
  public long getActiveAtom()
  {
    return activeAtom;
  }

  /**
   * @return Returns the factory.
   */
  public IContentFactory getFactory()
  {
    return factory;
  }

  /**
   * Sets a new factory to this manager. This will remove all current Content of this manager.
   * 
   * @param factory
   *          The factory to set.
   */
  public void setFactory(IContentFactory factory, TextAppearance appearance)
  {
    this.factory = factory;
    this.requestedWidthCache = Math.max(this.requestedWidthCache, 50);

    removeAll(appearance);
  }

  public int getAtomCount()
  {
    return atomCountCache;
  }

  /**
   * @return Returns the multiline.
   */
  public boolean isMultiline()
  {
    return multiline;
  }

  /**
   * @param multiline
   *          The multiline to set.
   */
  public void setMultiline(boolean multiline)
  {
    this.multiline = multiline;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionEnd()
   */
  public int getSelectionEnd()
  {
    int selectionAtoms = 0;
    boolean inSelection = false;
    ContentUserLine lastLine = null;

    for (ContentUserLine line : content)
    {
      boolean lineSelection = line.hasSelection();

      if (lineSelection)
      {
        inSelection = true;
      }

      if (inSelection)
      {
        if (lineSelection)
        {
          lastLine = line;
        }
        else
        {
          break;
        }
      }

      selectionAtoms += line.getAtomCount() + 1;
    }

    if (inSelection)
    {
      if (lastLine != null)
        selectionAtoms -= (lastLine.getAtomCount() + 1);

      int lineSelection = lastLine.getSelectionEnd();
      //			if (lineSelection == lastLine.getAtomCount())
      //				lineSelection++; //also select '\n'
      selectionAtoms += lineSelection;
      return selectionAtoms;
    }
    return -1;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionStart()
   */
  public int getSelectionStart()
  {
    int selectionAtoms = 0;

    for (ContentUserLine line : content)
    {
      if (line.hasSelection())
      {
        return selectionAtoms + line.getSelectionStart();
      }
      selectionAtoms += line.getAtomCount() + 1;
    }

    return -1;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentSelection#hasSelection()
   */
  public boolean hasSelection()
  {
    for (ContentUserLine line : content)
    {
      if (line.hasSelection())
      {
        return true;
      }
    }
    return false;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentSelection#clearSelection()
   */
  public void clearSelection(TextAppearance appearance)
  {
    for (ContentUserLine line : content)
    {
      line.clearSelection(appearance);
    }
    optimizeContent(appearance);
    updateContent(this.requestedWidthCache, appearance);
  }

  public void removeSelection(TextAppearance appearance)
  {
    this.removeSelection(factory, appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentSelection#removeSelection()
   */
  public void removeSelection(IContentFactory factory, TextAppearance appearance)
  {
    int pos = this.getSelectionStart();
    List<ContentUserLine> removables = new ArrayList<ContentUserLine>();

    synchronized (content)
    {
      for (ContentUserLine line : content)
      {
        line.removeSelection(factory, appearance);
        if (line.isEmpty() && line.hasSelection())
        {
          if (removables.contains(activeLine))
          {
            int index = content.indexOf(removables.get(0)) - 1;

            if (index >= 0)
            {
              activeLine = content.get(index);
            }
            else
            {
              activeLine = null;
            }
          }
          //remove line
          removables.add(line);
        }
      }

      content.removeAll(removables);

      //take first part if none anymore
      if (activeLine == null && content.size() > 0)
      {
        activeLine = content.get(0);
        activeLineAtoms = 0;
        activeLineY = 0;
      }

      if (content.size() <= 0)
      {
        //keep at least one line
        initEmptyContent(factory, appearance);
      }
      this.setActiveAtom(pos);

      update();
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentSelection#setSelection(int, int)
   */
  public void setSelection(int start, int end, TextAppearance appearance)
  {
    int selectionAtoms = 0;

    if (this.hasSelection())
      this.clearSelection(appearance);

    synchronized (content)
    {
      for (ContentUserLine line : content)
      {
        long lineAtoms = line.getAtomCount();
        if (selectionAtoms + lineAtoms > start && selectionAtoms < end)
        {
          line.setSelection(start - selectionAtoms, end - selectionAtoms, appearance);
        }
        else if (selectionAtoms > end)
          break;

        selectionAtoms += line.getAtomCount() + 1;
      }
    }
  }

  public int getMaxLines()
  {
    return maxLines;
  }

  public void setMaxLines(int maxLines)
  {
    this.maxLines = maxLines;
  }

  protected void fireElementEnteredEvent(ElementEvent<AbstractContentPart> event)
  {
    for (IElementListener<AbstractContentPart> l : elementListener)
    {
      l.enteredElement(event);
    }
  }

  protected void fireElementLeftEvent(ElementEvent<AbstractContentPart> event)
  {
    for (IElementListener<AbstractContentPart> l : elementListener)
    {
      l.leftElement(event);
    }
  }

  protected void fireElementActivatedEvent(ElementEvent<AbstractContentPart> event)
  {
    for (IElementListener<AbstractContentPart> l : elementListener)
    {
      l.activatedElement(event);
    }
  }

  public void render(int x, int y, Graphics g, boolean editMode, TextAppearance appearance)
  {
    int yCurrent = y;
    for (ContentUserLine line : content)
    {
      line.render(x, yCurrent, g, factory.getTextCursorRenderer(), editMode, appearance);
      yCurrent -= line.getSize().getHeight();
    }
  }

  public boolean addContentAtBeginning(Object content, TextAppearance appearance)
  {
    synchronized (this.content)
    {
      Object[] lines = factory.getContentLines(content);

      //merge with first line
      this.content.get(0).addContentAtBegining(lines[lines.length - 1], this.requestedWidthCache, this.autoWarp,
        factory, appearance);

      for (int i = lines.length - 2; i >= 0; i--)
      {
        Object line = lines[i];
        List<AbstractContentPart> parts = this.factory.getContentParts(line, appearance);
        ContentLine cLine = new ContentLine(parts);
        this.content.add(0, new ContentUserLine(cLine));
      }

      this.update();
      return true;
    }
  }

  public boolean addContentAtEnd(Object content, TextAppearance appearance)
  {
    synchronized (this.content)
    {
      Object[] lines = factory.getContentLines(content);

      //merge first with last line
      this.content.get(this.content.size() - 1).addContentAtEnd(lines[0], this.requestedWidthCache, this.autoWarp,
        factory, appearance);

      for (int i = 1; i < lines.length; i++)
      {
        Object line = lines[i];
        List<AbstractContentPart> parts = this.factory.getContentParts(line, appearance);
        ContentLine cLine = new ContentLine(parts);
        this.content.add(this.content.size(), new ContentUserLine(cLine));
      }

      this.update();
      return true;
    }
  }

  public boolean isValidCharacter(char character, TextAppearance appearance)
  {
    if (activeLine != null)
    {
      if (character == '\t' || (character == '\n' && multiline))
      {
        return true;
      }
      return activeLine.isValidCharacter(character, appearance);
    }
    else
    {
      return false;
    }
  }
}
