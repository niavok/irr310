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
 * $Id: ImageFont.java 630 2009-04-22 20:34:09Z marcmenghin $
 */
package org.fenggui.binding.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.fenggui.IDisposable;
import org.fenggui.theme.xml.DefaultElementName;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alphabet;
import org.fenggui.util.CharacterPixmap;
import org.fenggui.util.Dimension;
import org.fenggui.util.FontSAXHandler;
import org.fenggui.util.Log;
import org.fenggui.util.fonttoolkit.FontFactory;
import org.xml.sax.SAXException;

/**
 * Class that holds small pixmaps for drawing text.<br/> <br/>
 * 
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2007-12-20 15:47:28
 *         +0100 (Do, 20 Dez 2007) $
 * @version $Revision: 630 $
 */
@DefaultElementName("font")
public class ImageFont extends Font implements IDisposable, IImageFont
{
  private static ImageFont                      defaultFont = null;
  private Hashtable<Character, CharacterPixmap> texHashMap  = null;
  private BufferedImage                         image       = null;
  private int                                   height      = 0;

  /**
   * Returns the default font that is used by most text widgets before a specific theme is
   * applied.
   * 
   * @return font
   */
  public static ImageFont getDefaultFont()
  {
    if (defaultFont != null)
      return defaultFont;

    java.awt.Font awtFont = new java.awt.Font("Serif", java.awt.Font.PLAIN, 12);

    defaultFont = FontFactory.renderStandardFont(awtFont);

    return defaultFont;
  }

  /**
   * Set the default font
   * 
   * @param font
   */
  public static void setDefaultFont(ImageFont font)
  {
    defaultFont = font;
  }

  /**
   * Creates a new font.
   * 
   * @param map
   * @param texHashMap
   * @param height
   */
  public ImageFont(BufferedImage map, Hashtable<Character, CharacterPixmap> texHashMap, int height)
  {
    this.texHashMap = texHashMap;
    this.image = map;
    this.height = height;

    /*
     * if(map.getType() != BufferedImage.TYPE_INT_ARGB) { throw new
     * IllegalArgumentException("The image map has to be of type TYPE_INT_ARGB!"); }
     */
  }

  /**
   * Creates a new font out of a saved font. It uses <code>Binding.getResource</code>
   * internally such that this cnstructor is capable to read from a jar as well as
   * directly from a usual file.
   * 
   * @param textureFilename
   *          the name of the pre-rendered font texture (usually the PNG)
   * @param xmlFilename
   *          the name of the font description file (ends usually with .xml)
   * @throws FileNotFoundException
   *           thrown if one of the files cannot be found
   * @throws IOException
   *           thrown ... err... duno
   */
  public ImageFont(String textureFilename, String xmlFilename) throws FileNotFoundException, IOException
  {
    loadFont(textureFilename, xmlFilename);
  }

  /**
   * Loads a Font from an InputOnlyStream
   * 
   * @throws IXMLStreamableException
   * @throws IOException
   */
  public ImageFont(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  public ImageFont(InputStream textureIn, InputStream xmlIn) throws FileNotFoundException, IOException
  {
    loadFont(textureIn, xmlIn);
  }

  private void loadFont(InputStream textureIn, InputStream xmlIn) throws IOException
  {
    image = ImageIO.read(textureIn);
    texHashMap = new Hashtable<Character, CharacterPixmap>();
    FontSAXHandler fontHandler = new FontSAXHandler(texHashMap);

    SAXParserFactory factory = SAXParserFactory.newInstance();

    try
    {
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(xmlIn, fontHandler);

      height = texHashMap.get('a').getHeight();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }

  }

  private void loadFont(String textureFilename, String xmlFilename) throws IOException
  {
    InputStream xmlIn = Binding.getInstance().getResource(xmlFilename);
    InputStream textureIn = Binding.getInstance().getResource(textureFilename);

    loadFont(textureIn, xmlIn);

    xmlIn.close();
    textureIn.close();
  }

  public void writeFontData(String textureFilename, String descriptionFilename) throws IOException
  {
    ImageIO.write(image, "png", new File(textureFilename));

    StringBuilder buffer = new StringBuilder();

    buffer.append("<?xml version='1.0' encoding='utf-8'?>\n\n");
    buffer.append("<!--  Keep in mind that pixmaps have their origin in the upper left corner! -->\n\n");

    buffer.append("<Font>\n");

    for (Character c : texHashMap.keySet())
    {
      CharacterPixmap pixmap = texHashMap.get(c);

      pixmap.toXML("  ", buffer);
    }
    buffer.append("</Font>");
    FileOutputStream fileOut = new FileOutputStream(descriptionFilename, false);
    OutputStreamWriter out = new OutputStreamWriter(fileOut, "UTF-8");
    Log.warn("Writing description file with encoding " + out.getEncoding());
    out.write(buffer.toString());
    out.close();
  }

  /**
   * Uploads the character texture into video ram. This is a time consuming procedure,
   * which is why people may want to call it manually.
   * 
   */
  public void uploadToVideoMemory()
  {
    ITexture tex = Binding.getInstance().getTexture(image);

    for (CharacterPixmap cp : texHashMap.values())
    {
      cp.setTexture(tex);
    }
  }

  public boolean isCharacterMapped(char c)
  {
    return texHashMap.containsKey(c);
  }

  /**
   * Returns the width of the character in pixels.
   * 
   * @param s
   *          the character
   * @return the characters width in pixel
   */
  public int getWidth(char s)
  {
    CharacterPixmap cp = texHashMap.get(s);
    if (cp == null)
      cp = texHashMap.get('?');
    if (cp == null)
      return 0;
    return cp.getWidth();
  }

  /**
   * Returns the length of the string in pixels.
   * 
   * @param s
   *          the string
   * @return the length in pixel of the string
   */
  public int getWidth(String s)
  {
    if (s == null)
      return 0;
    int length = 0;
    for (int i = 0; i < s.length(); i++)
      length += getWidth(s.charAt(i));
    return length;
  }

  public BufferedImage getImage()
  {
    return image;
  }

  /**
   * Returns the length of a chars array in pixels.
   * 
   * @param chars
   *          A chars array
   * @param start
   *          position start in the chars array
   * @param end
   *          position end in the cahrs array
   * @return The length in pixel of a set of chars
   */
  public int getWidth(char[] chars, int start, int end)
  {
    if (end <= start)
      return 0;
    int length = 0;
    for (int i = start; i < end; i++)
    {
      length += getWidth(chars[i]);
    }
    return length;
  }

  /**
   * Returns the height of the font measured in pixel.
   * 
   * @return the height
   */
  public int getHeight()
  {
    return height;
  }

  /**
   * Returns the Pixmap the holds the given character within a texture. The character is
   * drawn in white. The rest of the texture is translucent. Each texture is cached as
   * long as the font object exists.
   * 
   * @param ch
   *          the character to be on the texture
   * @return the texture
   */
  public CharacterPixmap getCharPixMap(char ch)
  {
    //isn't this slower as the current? 
    //		if (texHashMap.get('a').getTexture() == null) {
    //			uploadToVideoMemory();
    //		}

    CharacterPixmap p = texHashMap.get(ch);

    if (p == null)
    {
      // System.err.println("Character '"+ch + "'=" + (int)ch+" has not been pre-rendered
      // in a Pixmap!!!");
      return texHashMap.get('?');
    }
    else
    {

      if (p.getTexture() == null)
      {
        uploadToVideoMemory();
      }

      return p;
    }
  }

  /**
   * Unbinds all character pixmaps of this font.
   */
  public void dispose()
  {
    Hashtable<ITexture, ITexture> set = new Hashtable<ITexture, ITexture>();

    for (Pixmap p : texHashMap.values())
      set.put(p.getTexture(), p.getTexture());

    for (ITexture t : set.values())
      t.dispose();
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    if (stream.isInputStream())
    {
      if (stream.startSubcontext("load"))
      {
        String map = stream.processAttribute("map", "not-set");
        String image = stream.processAttribute("image", "not-set");
        map = ((InputOnlyStream) stream).getResourcePath() + map;
        image = ((InputOnlyStream) stream).getResourcePath() + image;
        loadFont(image, map);
        stream.endSubcontext();
        return;
      }
      else if (stream.startSubcontext("create"))
      {
        String name = stream.processAttribute("fontName", "not-set");
        String typeStr = stream.processAttribute("type", "not-set", "plain");
        int type = java.awt.Font.PLAIN;

        if (typeStr.equalsIgnoreCase("plain"))
          type = java.awt.Font.PLAIN;
        else if (typeStr.equalsIgnoreCase("bold"))
          type = java.awt.Font.BOLD;
        else if (typeStr.equalsIgnoreCase("italic"))
          type = java.awt.Font.ITALIC;
        else
          throw new IllegalArgumentException("Unknwown font type '" + typeStr + "'");

        int size = stream.processAttribute("size", 16);
        boolean antialiasing = stream.processAttribute("antialiasing", true, false);
        // String alphabet = stream.process("alphabet", "not-set");
        java.awt.Font awtFont = new java.awt.Font(name, type, size);
        String alphabetStr = stream.processAttribute("alphabet", "unknown", "english");

        ImageFont f = null;

        if (alphabetStr.equalsIgnoreCase("english"))
          f = FontFactory.renderStandardFont(awtFont, antialiasing, Alphabet.ENGLISH);
        else if (alphabetStr.equalsIgnoreCase("german"))
          f = FontFactory.renderStandardFont(awtFont, antialiasing, Alphabet.GERMAN);
        else if (alphabetStr.equalsIgnoreCase("french"))
          f = FontFactory.renderStandardFont(awtFont, antialiasing, Alphabet.FRENCH);
        else if (alphabetStr.equalsIgnoreCase("danish"))
          f = FontFactory.renderStandardFont(awtFont, antialiasing, Alphabet.DANISH);
        else if (alphabetStr.equalsIgnoreCase("estonian"))
          f = FontFactory.renderStandardFont(awtFont, antialiasing, Alphabet.ESTONIAN);
        
        this.height = f.height;
        this.image = f.image;
        this.texHashMap = f.texHashMap;
        stream.endSubcontext();
      }
      else
        throw new IXMLStreamableException("neither <create> nor <load> found in <Font>", null);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.io.IOStreamSaveable#getUniqueName()
   */
  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.binding.render.IFont#calculateSize(java.lang.String)
   */
  public Dimension calculateSize(String text)
  {
    String[] lines = text.split("\n", -1);
    int width = 0;
    int height = this.getLineHeight();
    for (String line : lines)
    {
      width = Math.max(width, this.getWidth(line));
    }
    return new Dimension(width, height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.binding.render.IFont#getLineHeight()
   */
  public int getLineHeight()
  {
    return height;
  }
}
