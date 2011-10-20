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
 * $Id: TextEditor.java 633 2009-04-25 09:54:13Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.CursorFactory;
import org.fenggui.binding.render.CursorFactory.CursorType;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.IDragAndDropListener;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.ITextChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.event.TextChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.text.EditableTextContentManager;
import org.fenggui.text.IEditableTextContentManager;
import org.fenggui.text.ITextContentManager;
import org.fenggui.text.TextContentManager;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * Implementation of a text editor. Text editors come in multiple lines (text area) or
 * single line variants (text field). If it is set to multiline it is able to auto word
 * warp the text.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-08-11
 *         13:20:15 +0200 (Sa, 11 Aug 2007) $
 * @version $Revision: 633 $
 * @dedication No Use For a Name - Invincible
 */
public class TextEditor extends StatefullWidget<TextAppearance> implements ITextWidget, Cloneable
{
  // a few nice definitions to restirct userinput
  public static final String              RESTRICT_NUMBERSONLY        = "[0-9-+]+";
  public static final String              RESTRICT_NUMBERSONLYDECIMAL = "[0-9.,-+]+";
  public static final String              RESTRICT_LETTERSONLY        = "[A-Z]+";
  public static final String              RESTRICT_CHARACTERSOFIP     = "[0-9.:]+";
  public static final String              RESTRICT_LETTERSANDNUMBERS  = "[A-Z0-9]+";
  public static final String              RESTRICT_EMAIL              = "[A-Z0-9\\._%\\+\\-@]+";

  private ArrayList<ITextChangedListener> textChangedHook             = new ArrayList<ITextChangedListener>();

  private TextEditorDnDListener           dndListener                 = null;
  private ISizeChangedListener            textSizeChangedListener;

  /**
   * Define the max number of character that can be added to the TextEditor
   */
  private int                             maxCharacters               = -1;

  private boolean                         passwordField               = false;
  private String                          passwordData                = "";
  private char                            passwordHideCharacter       = '*';

  private IEditableTextContentManager     textData;
  private ITextContentManager             emptyData;
  /**
   * Define a regularExpression representing allowed characters.
   */
  private Pattern                         restrict                    = null;

  /**
   * Define if the regularExpression accepts unicode characters.
   */
  private boolean                         unicodeRestrict             = true;

  public TextEditor()
  {
    setAppearance(new TextAppearance(this));
    setupDefaults();
    updateMinSize();
  }

  public TextEditor(TextEditor widget)
  {
    super(widget);

    setAppearance(new TextAppearance(this, widget.getAppearance()));
    setupDefaults();
    updateMinSize();
  }

  private void setupDefaults()
  {
    textData = new EditableTextContentManager(this.getAppearance());
    emptyData = new TextContentManager();
    dndListener = new TextEditorDnDListener(this);
    setTraversable(true);
    setDefaultHoverCursorType(CursorType.TEXT);
    textSizeChangedListener = new ISizeChangedListener()
    {

      public void sizeChanged(SizeChangedEvent event)
      {
        updateMinSize();
      }

    };

    buildSpecialEvents();
  }

  /* (non-Javadoc)
   * @see org.fenggui.StatefullWidget#focusChanged(org.fenggui.event.FocusEvent)
   */
  @Override
  public void focusChanged(FocusEvent focusGainedEvent)
  {
    textData.setEditMode(focusGainedEvent.isFocusGained());

    super.focusChanged(focusGainedEvent);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  @Override
  public void sizeChanged(SizeChangedEvent event)
  {
    textData.adaptChange(getAppearance().getContentWidth(), getAppearance().getContentHeight(), getAppearance());
    emptyData.adaptChange(getAppearance().getContentWidth(), getAppearance().getContentHeight(), getAppearance());
    super.sizeChanged(event);
  }

  /* (non-Javadoc)
  * @see org.fenggui.ObservableWidget#keyPressed(org.fenggui.event.key.KeyPressedEvent)
  */
  @Override
  public void keyPressed(KeyPressedEvent keyPressedEvent)
  {
    if (isEnabled())
    {

      if (isInWritingState())
        if (handleKeyPressed(keyPressedEvent))
          keyPressedEvent.setUsed();
    }
    super.keyPressed(keyPressedEvent);
  }

  /* (non-Javadoc)
  * @see org.fenggui.ObservableWidget#keyTyped(org.fenggui.event.key.KeyTypedEvent)
  */
  @Override
  public void keyTyped(KeyTypedEvent keyTypedEvent)
  {
    if (isEnabled())
    {
      if (isInWritingState() && !keyTypedEvent.isAlreadyUsed())
        if (handleKeyTyped(keyTypedEvent))
          keyTypedEvent.setUsed();
    }
    super.keyTyped(keyTypedEvent);
  }

  /* (non-Javadoc)
  * @see org.fenggui.ObservableWidget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
  */
  @Override
  public void mouseDoubleClicked(MouseDoubleClickedEvent event)
  {
    if (isEnabled())
    {
      if (event.getButton() == MouseButton.LEFT)
      {
        textData.setSelectionIndex(0, getText().length(), getAppearance());
        event.setUsed();
      }
    }
    super.mouseDoubleClicked(event);
  }

  /* (non-Javadoc)
  * @see org.fenggui.ObservableWidget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
  */
  @Override
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
  {
    if (isEnabled())
    {
      Binding.getInstance().getCursorFactory().getCursor(CursorFactory.CursorType.TEXT).show();
      getDisplay().addDndListener(dndListener);
    }
    super.mouseEntered(mouseEnteredEvent);
  }

  /* (non-Javadoc)
  * @see org.fenggui.ObservableWidget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
  */
  @Override
  public void mouseExited(MouseExitedEvent mouseExitedEvent)
  {
    Binding.getInstance().getCursorFactory().getCursor(CursorFactory.CursorType.DEFAULT).show();
    if (getDisplay() != null)
      getDisplay().removeDndListener(dndListener);

    super.mouseExited(mouseExitedEvent);
  }

  private void buildSpecialEvents()
  {
    textData.removeSizeChangedListener(textSizeChangedListener);
    textData.addSizeChangedListener(textSizeChangedListener);
  }

  private boolean handleKeyTyped(KeyTypedEvent e)
  {
    boolean result = false;

    if (this.maxCharacters >= 0 && this.getText().length() >= this.maxCharacters && !textData.hasSelection())
      return false;

    if (!isReadonly() && Character.isDefined(e.getKey()) && !(e.isPressed(Key.META)))
    {
      char character = e.getKey();
      
      if (restrict != null && !restrict.matcher(Character.toString(character)).matches())
      {
        return false;
      }

      if (textData.isValidChar(character, getAppearance()))
      {
        if (this.isPasswordField())
        {
          this.passwordData += character;
          character = passwordHideCharacter;
        }
        result = textData.handleTextInput(character, getAppearance());

        fireTextChangedEvent(null);
      }
    }
    return result;
  }

  private boolean handleKeyPressed(KeyPressedEvent e)
  {
    switch (e.getKeyClass())
    {
    case LETTER:
      // TODO: move this into event binding somehow (so all benefit from this)
      // also this is not platform independent (maybe awt provides a way to do this?)
      String vers = System.getProperty("os.name").toLowerCase();
      boolean isMac = vers.indexOf("mac") != -1;
      if ((!isMac && e.isPressed(Key.CTRL)) || (isMac && e.isPressed(Key.META)))
      {
        if (e.getKey() == 'C')
        {
          e.setUsed();
          return textData.handleKeyPresses(Key.COPY, e.getModifiers(), getAppearance());
        }
        else if (e.getKey() == 'X')
        {
          e.setUsed();
          return textData.handleKeyPresses(Key.CUT, e.getModifiers(), getAppearance());
        }
        else if (e.getKey() == 'V')
        {
          e.setUsed();
          return textData.handleKeyPresses(Key.PASTE, e.getModifiers(), getAppearance());
        }
        else
        {
          return textData.handleKeyPresses(e.getKeyClass(), e.getModifiers(), getAppearance());
        }
      }
      else
      {
        return textData.handleKeyPresses(e.getKeyClass(), e.getModifiers(), getAppearance());
      }
    default:
      boolean result = textData.handleKeyPresses(e.getKeyClass(), e.getModifiers(), getAppearance());
      //FIXME: textData should fire an event if its text changes 
      if (result) 
      {
        if (e.getKeyClass() == Key.DELETE || e.getKeyClass() == Key.BACKSPACE)
        {
          fireTextChangedEvent(null);
        }
        e.setUsed();
      }
      return result;
    }
  }

  /**
   * 
   * @return the maxCharacters
   */
  public int getMaxCharacters()
  {
    return maxCharacters;
  }

  /**
   * The maximum number of characters a user can enter into this field.
   * 
   * @param maxCharacters
   *          the max number of characters in the textEditor
   */
  public void setMaxCharacters(int maxCharacters)
  {
    this.maxCharacters = maxCharacters;
  }

  /**
   * @return the validCharacters
   */
  public String getRestrict()
  {
    return restrict.pattern();
  }

  /**
   * Sets the valid characters as a RegularExpression. if we want to enable only letters
   * from a to z and numbers, we would set : "[a-zA-Z0-9]+"
   * 
   * @param validCharacters
   *          a regular expression representing valid characters
   */
  public void setRestrict(String restrict)
  {
    if (restrict != null)
    {
      if (unicodeRestrict)
      {
        this.restrict = Pattern.compile(restrict, Pattern.UNICODE_CASE);
      }
      else
      {
        this.restrict = Pattern.compile(restrict);
      }
    }
    else
    {
      this.restrict = null;
    }
  }

  /**
   * @return the unicodeRestrict
   */
  public boolean isUnicodeRestrict()
  {
    return unicodeRestrict;
  }

  /**
   * @param unicodeRestrict
   *          a d�fnir
   */
  public void setUnicodeRestrict(boolean unicodeRestrict)
  {
    this.unicodeRestrict = unicodeRestrict;
  }

  /**
   * @return the passwordField
   */
  public boolean isPasswordField()
  {
    return passwordField;
  }

  /**
   * Sets if this field is a password field. If so instead of the user input the
   * PasswordChar will be displayed to the user. A Entered password will only be stored in
   * one String within this class.
   * 
   * @param passwordField
   *          the passwordField to set
   */
  public void setPasswordField(boolean passwordField)
  {
    if (this.passwordField != passwordField)
      passwordData = "";

    this.passwordField = passwordField;

  }

  /**
   * @return the text editor's text
   */
  public String getText()
  {
    if (this.isPasswordField())
      return this.passwordData;
    else
      return textData.getContent();
  }

  public void setEmptyText(String text)
  {
    emptyData.setContent(text, getAppearance());
    updateMinSize();
  }

  /**
   * Define the textEditor's text
   * 
   * @param text
   */
  public void setText(String text)
  {
    String fittingText = "";
    if (text != null && text.length() != 0)
    {
      if (maxCharacters < 0 || text.length() <= maxCharacters)
      {
        fittingText = text;
      }
      else
      {
        fittingText = text.substring(0, maxCharacters);
      }
    }

    if (isPasswordField())
    {
      passwordData = fittingText;
      char[] hidingText = new char[fittingText.length()];
      Arrays.fill(hidingText, this.passwordHideCharacter);
      fittingText = new String(hidingText);
    }

    textData.setContent(fittingText, getAppearance());
    buildSpecialEvents();
    fireTextChangedEvent(fittingText);
  }

  /**
   * Add content at the end of the current content. This content gets a new line. Also it
   * will remove all lines from the opposite side.
   * 
   * @param content
   */
  public void addContentAtEnd(String content)
  {
    textData.addContentAtEnd(content, getAppearance());
  }

  /**
   * Adds the given content before the current content within the TextEditor. Also it will
   * remove all lines from the opposite side.
   * 
   * @param content
   */
  public void addContentAtBeginning(String content)
  {
    textData.addContentAtBeginning(content, getAppearance());
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
  }

  /*
  * (non-Javadoc)
  * 
  * @see org.fenggui.StatefullWidget#setEnabled(boolean)
  */
  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);

    if (!enabled)
      textData.setEditMode(false);
  }

  private class TextEditorDnDListener implements IDragAndDropListener
  {
    TextEditor parent = null;

    public TextEditorDnDListener(TextEditor parent)
    {
      this.parent = parent;
    }

    public boolean isDndWidget(IWidget w, int displayX, int displayY)
    {
      return w.equals(parent);
    }

    public void select(int displayX, int displayY, Set<Key> modifiers)
    {
      if (!parent.isEnabled())
        return;

      int xPos = displayX - parent.getDisplayX();
      int yPos = displayY - parent.getDisplayY();

      int emptyHeight = getHeight() - textData.getSize().getHeight();
      if (emptyHeight > 0)
        yPos -= emptyHeight;

      if (yPos < 0)
        yPos = 0;

      textData.clickedOn(xPos, yPos, modifiers, getAppearance());
    }

    public void drag(int displayX, int displayY, Set<Key> modifiers)
    {
      if (!parent.isEnabled())
        return;

      int xPos = displayX - parent.getDisplayX();
      int yPos = displayY - parent.getDisplayY();

      int emptyHeight = getHeight() - textData.getSize().getHeight();
      if (emptyHeight > 0)
        yPos -= emptyHeight;

      if (yPos < 0)
        yPos = 0;

      textData.dragedTo(xPos, yPos, modifiers, getAppearance());
    }

    public void drop(int displayX, int displayY, IWidget droppedOn, Set<Key> modifiers)
    {
      if (!parent.isEnabled())
        return;

      int xPos = displayX - parent.getDisplayX();
      int yPos = displayY - parent.getDisplayY();

      int emptyHeight = getHeight() - textData.getSize().getHeight();
      if (emptyHeight > 0)
        yPos -= emptyHeight;

      if (yPos < 0)
        yPos = 0;

      textData.dragedTo(xPos, yPos, modifiers, getAppearance());
    }
  }

  public boolean isInWritingState()
  {
    return textData.isEditMode();
  }

  /**
   * Add a {@link ITextChangedListener} to the widget. The listener can be added only
   * once.
   * 
   * @param l
   *          Listener
   */
  public void addTextChangedListener(ITextChangedListener l)
  {
    if (!textChangedHook.contains(l))
    {
      textChangedHook.add(l);
    }
  }

  /**
   * Add the {@link ITextChangedListener} from the widget
   * 
   * @param l
   *          Listener
   */
  public void removeTextChangedListener(ITextChangedListener l)
  {
    textChangedHook.remove(l);
  }

  /**
   * Fire a {@link TextChangedEvent}
   */
  private void fireTextChangedEvent(String text)
  {
    TextChangedEvent e = new TextChangedEvent(this, text);

    for (ITextChangedListener l : textChangedHook)
    {
      l.textChanged(e);
    }

    // Why should this be fired globally?
    // Display display = getDisplay();
    // if (display != null)
    // {
    // display.fireGlobalEventListener(e);
    // }
  }

  /* (non-Javadoc)
   * @see org.fenggui.StandardWidget#updateMinSize()
   */
  @Override
  public void updateMinSize()
  {
    textData.Update(getAppearance());
    emptyData.Update(getAppearance());
    super.updateMinSize();
  }

  @Override
  public Dimension getMinContentSize()
  {
    Dimension size = textData.getSize();

    if (textData.isWordWarping())
    {
      size.setWidth(5);
    }

    return size;
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    // user contribution to make text appear within visible area
    // if(font.getWidth(text.toCharArray(), 0,
    // text.toCharArray().length)>=(editor.getWidth()-editor.getAppearance().getPadding().getLeftPlusRight()))
    // {
    // x-=font.getWidth(text.toCharArray(), 0,
    // text.toCharArray().length)-(editor.getWidth()-editor.getAppearance().getPadding().getLeftPlusRight());
    // }

    if (textData.isEmpty() && !this.isEditMode())
    {
      Dimension textSize = emptyData.getSize();
      int x = getAppearance().getAlignment().alignX(getAppearance().getContentWidth(), textSize.getWidth());
      int y = getAppearance().getContentHeight();
      if (y > textSize.getHeight())
      {
        y = getAppearance().getAlignment().alignY(y, textSize.getHeight()) + textSize.getHeight();
      }
      emptyData.render(x, y, g, getAppearance());
    }
    else
    {
      Dimension textSize = textData.getSize();
      int x = getAppearance().getAlignment().alignX(getAppearance().getContentWidth(), textSize.getWidth());
      int y = getAppearance().getContentHeight();

      //		g.setColor(new Color(255,0,0,100));
      //		g.drawFilledRectangle(0, 0, getAppearance().getContentWidth(), getAppearance().getContentHeight());

      if (y > textSize.getHeight())
      {
        y = getAppearance().getAlignment().alignY(y, textSize.getHeight()) + textSize.getHeight();
      }
      textData.render(x, y, g, getAppearance());
    }
  }

  public int getMaxLines()
  {
    return textData.getMaxLines();
  }

  public void setMaxLines(int maxLines)
  {
    textData.setMaxLines(maxLines);
  }

  public boolean isReadonly()
  {
    return textData.isReadonly();
  }

  public void setReadonly(boolean readonly)
  {
    textData.setReadonly(readonly);
  }

  /**
   * @return
   * @see org.fenggui.text.IEditableTextContentManager#isEditMode()
   */
  public boolean isEditMode()
  {
    return textData.isEditMode();
  }

  /**
   * @return
   * @see org.fenggui.text.ITextContentManager#isMultiline()
   */
  public boolean isMultiline()
  {
    return textData.isMultiline();
  }

  /**
   * @return
   * @see org.fenggui.text.ITextContentManager#isWordWarping()
   */
  public boolean isWordWarping()
  {
    return textData.isWordWarping();
  }

  /**
   * @param multiline
   * @param appearance
   * @see org.fenggui.text.ITextContentManager#setMultiline(boolean, org.fenggui.appearance.TextAppearance)
   */
  public void setMultiline(boolean multiline)
  {
    textData.setMultiline(multiline, getAppearance());
    emptyData.setMultiline(multiline, getAppearance());
  }

  /**
   * @param warp
   * @param appearance
   * @see org.fenggui.text.ITextContentManager#setWordWarping(boolean, org.fenggui.appearance.TextAppearance)
   */
  public void setWordWarping(boolean warp)
  {
    textData.setWordWarping(warp, getAppearance());
    emptyData.setWordWarping(warp, getAppearance());
  }

  public IEditableTextContentManager getTextRendererData()
  {
    return textData;
  }

  public ITextContentManager getEmptyTextRendererData()
  {
    return emptyData;
  }

  /* (non-Javadoc)
   * @see org.fenggui.StatefullWidget#clone()
   */
  @Override
  public TextEditor clone()
  {
    TextEditor result = (TextEditor) super.clone();

    result.setAppearance(this.getAppearance().clone(result));
    result.setupDefaults();

    return result;
  }

}
