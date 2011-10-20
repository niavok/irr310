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

import org.fenggui.binding.render.text.ITextRenderer;

/**
 * This class provides different static Text methods.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public final class TextUtil
{
  public static final String ENDMARKER0 = "..";
  public static final String ENDMARKER1 = ">>";
  public static final String ENDMARKER2 = (Character.toString((char) 0x00BB)); //>>
  public static final String ENDMARKER3 = (Character.toString((char) 0x0085)); //'...'
  public static final String ENDMARKER4 = (Character.toString((char) 0x2192)); //->
  public static final String ENDMARKER5 = (Character.toString((char) 0x21A0)); //->>
  public static final String ENDMARKER6 = (Character.toString((char) 0x21E2)); //->
  public static final String ENDMARKER7 = (Character.toString((char) 0x21D2)); //=>
  public static final String ENDMARKER8 = (Character.toString((char) 0x21E8)); //=>
  public static final String ENDMARKER9 = (Character.toString((char) 0x21DD)); //~>

  private TextUtil()
  {
  }

  /**
   * Confines the length of the given string to a certain width. The part
   * that is too long is cut of and replaced with the endMarker;
   * @param s the string
   * @return the resulting string with endMarker;
   */
  public static String confineString(String text, int width, ITextRenderer renderer, String endMarker)
  {
    int length = 0;

    int maxLengthOfChar = renderer.getWidth("M");
    int startChar = (width / maxLengthOfChar) - 1;
    if (startChar < 0)
      startChar = 0;

    for (int i = startChar; i < text.length(); i++)
    {
      length += renderer.getWidth(text.charAt(i) + "");

      if (length >= width)
      {
        int pLength = renderer.getWidth(endMarker);

        while (length + pLength >= width && i >= 0)
        {
          length -= renderer.getWidth(text.charAt(i));
          i--;
        }

        text = text.substring(0, ++i) + endMarker;
        break;
      }
    }

    return text;
  }

  public static String noLineBreaks(String text)
  {
    int h = text.indexOf('\n');
    if (h >= 0)
    {
      return text.substring(0, h);
    }

    return text;
  }

  public static int findLineEndChar(String text, ITextRenderer renderer, int maxWidth)
  {
    int result = text.length();
    int charSize = renderer.getWidth("M") + 1;
    int end = result;

    if (renderer.getWidth(text) > maxWidth)
    {
      // guess start
      end = maxWidth / charSize;

      //add char till over max
      while (end < text.length() && renderer.getWidth(text.substring(0, end)) <= maxWidth)
      {
        end++;
      }

      return end - 1;
    }
    else
    {
      return result;
    }
  }

  public static int findLineEnd(String text, ITextRenderer renderer, int maxWidth)
  {
    int result = text.length();
    int charSize = renderer.getWidth("M") + 1;
    int end = result;

    if (renderer.getWidth(text) > maxWidth)
    {
      // guess start
      end = maxWidth / charSize;

      // get word start position
      int pos = text.indexOf(" ", end); // after end
      if (pos < 0)
      {
        // no end word found after 'end' so use end of string
        end = text.length();
      }
      else
      {
        end = pos;
      }

      int i = 0;
      while (i < 1000)
      {
        int currentLength = renderer.getWidth(text.substring(0, end));
        if (currentLength <= maxWidth)
        {
          // get next word
          int wordEnd = text.indexOf(" ", end + 1);
          if (wordEnd < 0)
            wordEnd = text.length();
          if (wordEnd == end)
          {
            return end;
          }
          String nextWord = text.substring(end, wordEnd);
          int wordLength = renderer.getWidth(nextWord);
          if (currentLength + wordLength < maxWidth)
          {
            if (wordEnd == text.length())
              return wordEnd;
            end = wordEnd;
          }
          else
          {
            if (end == 0)
              return text.length();
            else
              return end;
          }
        }
        else
        {
          int wordEnd = text.lastIndexOf(" ", end - 1);
          if (wordEnd < 0)
            wordEnd = 0;
          if (wordEnd == end)
          {
            return wordEnd;
          }
          String nextWord = text.substring(wordEnd, end);
          int wordLength = renderer.getWidth(nextWord);
          if (currentLength - wordLength <= maxWidth)
          {
            end = wordEnd;
          }
          else
          {
            return wordEnd;
          }
        }
        i++;
      }
      /*
       * should never happen that it gets here, but just in case return end
       */
      return end;
    }
    else
    {
      return result;
    }
  }
}
